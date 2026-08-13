package com.games.payment.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.games.common.constant.Constants;
import com.games.common.enums.MovementImportStatus;
import com.games.common.enums.NuvieOrderStatus;
import com.games.pay.dto.OrderWithFeeModelDto;
import com.games.payment.domain.MovementData;
import com.games.payment.domain.MovementImport;
import com.games.payment.domain.ReportQuote;
import com.games.payment.mapper.MovementDataMapper;
import com.games.payment.mapper.OrderMapper;
import com.games.payment.service.*;
import com.games.system.service.ISysConfigService;
import com.neovisionaries.i18n.CountryCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * nuvei导入数据明细Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class MovementDataServiceImpl extends ServiceImpl<MovementDataMapper, MovementData>
        implements IMovementDataService {
    private static final Map<String, CountryCode> COUNTRY_ALIAS_MAP = new HashMap<>();
    private static final Set<CountryCode> EU_COUNTRIES = EnumSet.of(
            CountryCode.AT, CountryCode.BE, CountryCode.BG, CountryCode.HR, CountryCode.CY, CountryCode.CZ,
            CountryCode.DK, CountryCode.EE, CountryCode.FI, CountryCode.FR, CountryCode.DE, CountryCode.GR,
            CountryCode.HU, CountryCode.IE, CountryCode.IT, CountryCode.LV, CountryCode.LT, CountryCode.LU,
            CountryCode.MT, CountryCode.NL, CountryCode.PL, CountryCode.PT, CountryCode.RO, CountryCode.SK,
            CountryCode.SI, CountryCode.ES, CountryCode.SE
    );

    static {
        COUNTRY_ALIAS_MAP.put("united states", CountryCode.US);
        COUNTRY_ALIAS_MAP.put("united states of america", CountryCode.US);
        COUNTRY_ALIAS_MAP.put("usa", CountryCode.US);
        COUNTRY_ALIAS_MAP.put("u.s.", CountryCode.US);
        COUNTRY_ALIAS_MAP.put("u.s.a.", CountryCode.US);
        COUNTRY_ALIAS_MAP.put("uk", CountryCode.GB);
        COUNTRY_ALIAS_MAP.put("united kingdom", CountryCode.GB);
    }

    @Resource
    @Lazy
    private IMovementImportService movementImportService;
    @Resource
    private IOrderService orderService;
    @Resource
    private IReportQuoteService reportQuoteService;
    @Resource
    private ISysConfigService sysConfigService;
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private IMerchantAccountService merchantAccountService;
    @Resource
    private MovementDataMapper movementDataMapper;

    /**
     * 查询nuvei导入数据明细列表
     *
     * @param movementData nuvei导入数据明细
     * @return nuvei导入数据明细
     */
    @Override
    public List<MovementData> selectAllList(MovementData movementData) {
        return getBaseMapper().selectAllList(movementData);
    }

    @Override
    public List<MovementData> queryList(MovementData movementData) {
        LambdaQueryWrapper<MovementData> lqw = Wrappers.lambdaQuery();
        return this.list(lqw);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void calculateMerchantSettlementFee(Long movementImportId) {
        log.info("========== 开始计算商户结算费用，报告ID: {} ==========", movementImportId);
        long startTime = System.currentTimeMillis();

        MovementImport movementImport = new MovementImport();
        movementImport.setId(movementImportId);
        try {
            MovementData query = new MovementData();
            query.setMovementImportId(movementImportId);
            List<MovementData> movementDataList = selectAllList(query);
            log.info("查询到 {} 条 MovementData 记录", movementDataList.size());
            // 将movementType为"Sales"的记录排在最前面
            movementDataList.sort((md1, md2) -> {
                if (NuvieOrderStatus.Sales.getCode().equals(md1.getMovementType())
                        && !NuvieOrderStatus.Sales.getCode().equals(md2.getMovementType())) {
                    return -1;
                } else if (!"Sales".equals(md1.getMovementType()) && "Sales".equals(md2.getMovementType())) {
                    return 1;
                } else {
                    // 如果都是Sales或者都不是Sales，则保持原有顺序(根据id排序)
                    return md1.getId().compareTo(md2.getId());
                }
            });

            // 已收取网关费的退款订单id的set集合
            Set<String> refundOrderIdSet = new HashSet<>();
            // 已收取网关费的成功订单id的集合
            Set<String> saleOrderIdSet = new HashSet<>();

            // 批量获取所有订单的费用模型，避免循环中逐个查询
            Set<String> orderIds = movementDataList.stream()
                    .map(MovementData::getClientUniqueId)
                    .filter(Objects::nonNull).collect(Collectors.toSet());
            Map<Long, OrderWithFeeModelDto> orderFeeModelMap = new HashMap<>();
            if (!orderIds.isEmpty()) {
                List<OrderWithFeeModelDto> orderWithFeeModels = orderMapper.getOrdersWithFeeModelByIds(orderIds);
                orderFeeModelMap = orderWithFeeModels.stream()
                        .collect(Collectors.toMap(OrderWithFeeModelDto::getId, Function.identity(),
                                (existing, replacement) -> existing));
            }

            // 查询所有币种转化美元的汇率
            LambdaQueryWrapper<ReportQuote> rateQueryWrapper = Wrappers.lambdaQuery();
            rateQueryWrapper.eq(ReportQuote::getToCode, Constants.USD);
            List<ReportQuote> reportQuotes = reportQuoteService.list(rateQueryWrapper);
            Map<String, BigDecimal> currencyRateMap = reportQuotes.stream()
                    .collect(Collectors.toMap(ReportQuote::getFromCode, ReportQuote::getQuote));

            // 查询默认汇率
            String defaultRate = sysConfigService.selectConfigByKey(Constants.DEFAULT_RATE);

            // 失败订单id集合
            Set<String> declinedOrderIds = movementDataList.stream()
                    .filter(d -> d.getClientUniqueId() != null)
                    .collect(Collectors.groupingBy(MovementData::getClientUniqueId))
                    .entrySet().stream()
                    .filter(entry -> entry.getValue().stream()
                            .allMatch(data -> NuvieOrderStatus.Auth3D.getCode().equals(data.getMovementType()) ||
                                    NuvieOrderStatus.Declined.getCode().equals(data.getMovementType())))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toSet());

            // 收集需要批量更新的数据
            List<MovementData> toUpdateList = new ArrayList<>();

            // 统计各种订单状态的数量
            Map<String, Integer> statusCountMap = new HashMap<>();
            int skipCount = 0; // 跳过的记录数（没有clientUniqueId）
            int noFeeModelCount = 0; // 没有收费模型的记录数
            int noRateCount = 0; // 没有汇率的记录数
            int noProcessingFeeCount = 0; // 没有processing fee设置的记录数

            // 循环处理每笔订单
            for (MovementData movementData : movementDataList) {
                if (movementData.getClientUniqueId() == null) {
                    skipCount++;
                    continue;
                }

                String movementType = movementData.getMovementType();
                statusCountMap.put(movementType, statusCountMap.getOrDefault(movementType, 0) + 1);
                StringBuilder remark = new StringBuilder();

                // 从map中取收费模型
                OrderWithFeeModelDto orderWithFeeModel = orderFeeModelMap
                        .get(Long.parseLong(movementData.getClientUniqueId()));
                if (orderWithFeeModel == null) {
                    noFeeModelCount++;
                    remark.append("未查询到订单商户的收费模型;");
                    movementData.setMerchantSettlementRemark(remark.toString());
                    toUpdateList.add(movementData);
                    continue;
                }
                // nuvie中订单金额 其他状态取
                BigDecimal nuvieOriginAmount = movementData.getAmount();
                // bps计算后汇率入库
                Map<String, BigDecimal> nuvieAmountTransferResult = oderAmountTransfer(nuvieOriginAmount,
                        orderWithFeeModel.getExchangeRateBps(), movementData.getCurrency(), currencyRateMap,
                        defaultRate);
                if (nuvieAmountTransferResult == null) {
                    noRateCount++;
                    remark.append("未查询到汇率;");
                    movementData.setMerchantSettlementRemark(remark.toString());
                    toUpdateList.add(movementData);
                    continue;
                }

                // 订单表转换后订单金额 成功状态取
                BigDecimal fiatAmount = orderWithFeeModel.getFiatAmount();
                // bps计算后汇率入库
                Map<String, BigDecimal> fiatAmountTransferResult = oderAmountTransfer(fiatAmount,
                        orderWithFeeModel.getExchangeRateBps(), movementData.getCurrency(), currencyRateMap,
                        defaultRate);
                assert fiatAmountTransferResult != null;
                BigDecimal fiatTransferAmount = fiatAmountTransferResult.get("orderAmount");

                BigDecimal orderAmount = nuvieAmountTransferResult.get("orderAmount");
                movementData.setTransferAmount(orderAmount);
                movementData.setBaseRate(nuvieAmountTransferResult.get("baseRate"));
                movementData.setFxRate(nuvieAmountTransferResult.get("fxRate"));
                movementData.setMerchantSettlementRemark(null);

                if (movementType.equals(NuvieOrderStatus.Sales.getCode())) {
                    // 成功的订单，计算逻辑：Settlement Amount = Amount – Processing Fee – Rolling Reserve –
                    // Gateway Fee
                    // 计算Processing Fee
                    boolean isEu = isEuropeanCountry(movementData.getCountry());
                    BigDecimal processingFee = calculateProcessingFee(isEu,
                            movementData.getCurrency(),
                            orderWithFeeModel, fiatTransferAmount);
                    if (processingFee == null) {
                        noProcessingFeeCount++;
                        remark.append("未查询到订单的processing fee设置;");
                        movementData.setMerchantSettlementRemark(remark.toString());
                        toUpdateList.add(movementData);
                        continue;
                    }
                    movementData.setMerchantProcessingFee(processingFee);

                    // 获取rolling reserve
                    // BigDecimal rollingReserveFee = calculateRollingReserveFee(orderWithFeeModel,
                    // orderAmount);
                    BigDecimal rollingReserveFee = orderWithFeeModel.getRollingFee();
                    movementData.setMerchantRollingReserve(rollingReserveFee);

                    // 计算gateway fee
                    BigDecimal gatewayFee = calculateGatewayFee(orderWithFeeModel, fiatTransferAmount);
                    saleOrderIdSet.add(movementData.getClientUniqueId());
                    movementData.setMerchantGatewayFee(gatewayFee);

                    BigDecimal settlementAmount = fiatTransferAmount.subtract(processingFee).subtract(rollingReserveFee)
                            .subtract(gatewayFee);
                    movementData.setTransferAmount(fiatTransferAmount);
                    movementData.setMerchantSettlementFee(settlementAmount);
                } else if (declinedOrderIds.contains(movementData.getClientUniqueId())) {
                    // 失败订单：Settlement Amount = - GTW fee
                    BigDecimal gatewayFee;
                    // 计算gateway fee 如果该订单之前成功过已经收取了网关费，则网关费为0
                    if (declinedOrderIds.contains(movementData.getClientUniqueId())) {
                        gatewayFee = BigDecimal.valueOf(0);
                    } else {
                        gatewayFee = calculateGatewayFee(orderWithFeeModel, fiatTransferAmount);
                    }
                    movementData.setMerchantGatewayFee(gatewayFee);

                    BigDecimal settlementAmount = gatewayFee.negate();
                    movementData.setMerchantSettlementFee(settlementAmount);
                } else if (movementType.equals(NuvieOrderStatus.Refund.getCode())) {
                    // Settlement amount = Amount - Refund fee (某些商户会收退款费用: PW) - Gateway fee
                    BigDecimal refundFee = orderWithFeeModel.getRefundFee();
                    movementData.setMerchantRefundFee(refundFee);

                    // 同一个订单多次退款时，网关费只收一次。
                    BigDecimal gatewayFee;
                    if (refundOrderIdSet.contains(movementData.getClientUniqueId())) {
                        gatewayFee = BigDecimal.valueOf(0);
                    } else {
                        gatewayFee = calculateGatewayFee(orderWithFeeModel, fiatTransferAmount);
                        refundOrderIdSet.add(movementData.getClientUniqueId());
                    }
                    movementData.setMerchantGatewayFee(gatewayFee);

                    BigDecimal settlementAmount = orderAmount.subtract(refundFee).subtract(gatewayFee);
                    movementData.setMerchantSettlementFee(settlementAmount);
                } else if (movementType.equals(NuvieOrderStatus.RDR.getCode())) {
                    // Settlement amount = Amount - RDR fee (某些商户会收RDR费用: PW) - Gateway fee
                    BigDecimal rdrFee = orderWithFeeModel.getRdrFee();
                    movementData.setMerchantRdrFee(rdrFee);

                    BigDecimal gatewayFee = calculateGatewayFee(orderWithFeeModel, fiatTransferAmount);
                    movementData.setMerchantGatewayFee(gatewayFee);

                    BigDecimal settlementAmount = orderAmount.subtract(rdrFee).subtract(gatewayFee);
                    movementData.setMerchantSettlementFee(settlementAmount);
                } else if (movementType.equals(NuvieOrderStatus.CB.getCode())) {
                    // Settlement amount = Amount - Chargeback fee - Gateway fee
                    BigDecimal chargebackFee = orderWithFeeModel.getCbFee();
                    movementData.setMerchantChargebackFee(chargebackFee);

                    BigDecimal gatewayFee = calculateGatewayFee(orderWithFeeModel, fiatTransferAmount);
                    movementData.setMerchantGatewayFee(gatewayFee);

                    BigDecimal settlementAmount = orderAmount.subtract(chargebackFee).subtract(gatewayFee);
                    movementData.setMerchantSettlementFee(settlementAmount);
                } else if (movementType.equals(NuvieOrderStatus.CB_CANCELLED.getCode())) {
                    // Settlement amount = Amount - Gateway fee
                    BigDecimal gatewayFee = calculateGatewayFee(orderWithFeeModel, fiatTransferAmount);
                    movementData.setMerchantGatewayFee(gatewayFee);

                    BigDecimal settlementAmount = orderAmount.subtract(gatewayFee);
                    movementData.setMerchantSettlementFee(settlementAmount);
                }
                // 订单结账
                // orderService.settlementOrder(Long.parseLong(movementData.getClientUniqueId()),
                // orderAmount, MovementImportStatus.GENERATED.ordinal());
                // 添加到批量更新列表
                toUpdateList.add(movementData);
            }

            // 打印统计信息
            log.info("---------- 订单状态统计 ----------");
            statusCountMap.forEach((status, count) -> log.info("  {} : {} 条", status, count));
            log.info("  跳过记录（无clientUniqueId）: {} 条", skipCount);
            if (noFeeModelCount > 0) {
                log.warn("  警告：{} 条记录未查询到收费模型", noFeeModelCount);
            }
            if (noRateCount > 0) {
                log.warn("  警告：{} 条记录未查询到汇率", noRateCount);
            }
            if (noProcessingFeeCount > 0) {
                log.warn("  警告：{} 条记录未查询到processing fee设置", noProcessingFeeCount);
            }
            log.info("  待更新记录总数: {} 条", toUpdateList.size());

            // 批量更新所有数据（每次最多1000条）
            if (!toUpdateList.isEmpty()) {
                int batchSize = 1000;
                int totalBatches = (int) Math.ceil((double) toUpdateList.size() / batchSize);
                log.info("开始批量更新数据，共 {} 批次", totalBatches);

                for (int i = 0; i < toUpdateList.size(); i += batchSize) {
                    int end = Math.min(i + batchSize, toUpdateList.size());
                    List<MovementData> batch = toUpdateList.subList(i, end);
                    updateBatchById(batch);
                    log.info("  完成第 {}/{} 批次，更新 {} 条记录",
                            (i / batchSize + 1), totalBatches, batch.size());
                }
                log.info("批量更新完成，共更新 {} 条记录", toUpdateList.size());
            } else {
                log.warn("没有需要更新的记录");
            }

            // 统计商户的资产
            log.info("开始统计商户资产");
            List<MovementData> merchantSettlementList = movementDataMapper.merchantSettlement(movementImportId);
            log.info("查询到 {} 条商户结算记录", merchantSettlementList.size());

            // 更新商户资产
            log.info("开始更新商户资产");
            merchantAccountService.updateBalance(merchantSettlementList);
            log.info("商户资产更新完成");

            // 更新对账报告
            movementImport.setStatus(MovementImportStatus.GENERATED.ordinal());
            boolean updated = movementImportService.updateById(movementImport);
            log.info("更新报告状态为已生成，报告ID: {}，结果: {}", movementImportId, updated);

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            log.info("========== 计算完成，报告ID: {}，耗时: {} ms ({} 秒) ==========",
                    movementImportId, duration, duration / 1000.0);
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            log.error("========== 计算失败，报告ID: {}，耗时: {} ms，错误: {} ==========",
                    movementImportId, duration, e.getMessage(), e);

            movementImport.setRemark(e.getMessage());
            movementImport.setStatus(MovementImportStatus.GENERATED_FAIL.ordinal());
            movementImportService.updateById(movementImport);
            throw e;
        }
    }

    /**
     * 订单金额转换成USD
     *
     * @param originAmount 原始订单金额
     * @param bps          汇率折扣基点
     * @param currency     订单币种
     * @return 换算后订单金额
     */
    private Map<String, BigDecimal> oderAmountTransfer(BigDecimal originAmount, Long bps, String currency,
            Map<String, BigDecimal> currencyRateMap,
            String defaultRate) {
        BigDecimal baseRate;
        // 根据汇率和bps转换成USD
        if (currencyRateMap == null) {
            baseRate = new BigDecimal(defaultRate);
        } else {
            baseRate = currencyRateMap.get(currency);
        }
        if (baseRate == null) {
            return null;
        }

        BigDecimal bpsDecimal = BigDecimal.valueOf(bps);
        BigDecimal rateDiscount = bpsDecimal.divide(Constants.BPS_BASE, 4, RoundingMode.CEILING);
        BigDecimal fxRate = baseRate.multiply(
                BigDecimal.valueOf(1).subtract(rateDiscount)).setScale(4, RoundingMode.FLOOR);

        Map<String, BigDecimal> resultMap = new HashMap<>();
        resultMap.put("baseRate", baseRate);
        resultMap.put("fxRate", fxRate);
        resultMap.put("orderAmount", originAmount.multiply(fxRate).setScale(2, RoundingMode.FLOOR));
        return resultMap;

    }

    private boolean isEuropeanCountry(String country) {
        CountryCode countryCode = resolveCountryCode(country);
        return countryCode != null && EU_COUNTRIES.contains(countryCode);
    }

    private CountryCode resolveCountryCode(String country) {
        if (StrUtil.isBlank(country)) {
            return null;
        }
        String trimmed = country.trim();
        CountryCode aliasCode = COUNTRY_ALIAS_MAP.get(trimmed.toLowerCase(Locale.ROOT));
        if (aliasCode != null) {
            return aliasCode;
        }
        CountryCode byCode = CountryCode.getByCode(trimmed, false);
        if (byCode != null) {
            return byCode;
        }
        List<CountryCode> found = CountryCode.findByName(trimmed);
        if (!found.isEmpty()) {
            return found.get(0);
        }
        return null;
    }

    /**
     * 计算processing fee
     *
     * @param isEu              是否欧盟国家
     * @param currency          币种
     * @param orderWithFeeModel 订单模型
     * @param orderAmount       订单金额
     * @return processing fee
     */
    private BigDecimal calculateProcessingFee(boolean isEu, String currency,
            OrderWithFeeModelDto orderWithFeeModel,
            BigDecimal orderAmount) {
        BigDecimal processingFeePercent;

        if (isEu && StrUtil.equals(Constants.USD, currency)) {
            processingFeePercent = orderWithFeeModel.getProcessFeeEuUsd();
        } else if (isEu && !StrUtil.equals(Constants.USD, currency)) {
            processingFeePercent = orderWithFeeModel.getProcessFeeEuNonUsd();
        } else if (!isEu && StrUtil.equals(Constants.USD, currency)) {
            processingFeePercent = orderWithFeeModel.getProcessFeeNonEuUsd();
        } else if (!isEu && !StrUtil.equals(Constants.USD, currency)) {
            processingFeePercent = orderWithFeeModel.getProcessFeeNonEuNonUsd();
        } else {
            return null;
        }
        // 计算processing fee
        return orderAmount.multiply(processingFeePercent).divide(BigDecimal.valueOf(100), 4, RoundingMode.CEILING)
                .setScale(2, RoundingMode.CEILING);
    }

    /**
     * 计算rolling reserve fee
     *
     * @param orderWithFeeModel 收费模型
     * @param orderAmount       订单金额
     * @return rolling reserve fee
     */
    private BigDecimal calculateRollingReserveFee(OrderWithFeeModelDto orderWithFeeModel, BigDecimal orderAmount) {
        return orderAmount.multiply(orderWithFeeModel.getRollingReserveRate())
                .divide(BigDecimal.valueOf(100), 4, RoundingMode.CEILING).setScale(4, RoundingMode.CEILING);
    }

    /**
     * 计算gateway fee
     *
     * @param orderWithFeeModel 收费模型
     * @return 网关费用
     */
    private BigDecimal calculateGatewayFee(OrderWithFeeModelDto orderWithFeeModel, BigDecimal orderAmount) {
        BigDecimal gatewayFee;
        BigDecimal gatewayFeeThreshold = orderWithFeeModel.getGatewayFeeThreshold();
        // 订单金额的绝对值和网关费阈值比较
        if (orderAmount.compareTo(gatewayFeeThreshold) >= 0) {
            gatewayFee = orderWithFeeModel.getGatewayFeeAboveThreshold();
        } else {
            gatewayFee = orderWithFeeModel.getGatewayFeeBelowThreshold();
        }
        return gatewayFee;
    }

    @Override
    public BigDecimal calMerchantSettlementFeeTotal(Long merchantId) {
        return movementDataMapper.calMerchantSettlementFeeTotal(merchantId);
    }

    @Override
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void removeImportAndMovementData(List<String> movementImportIds) {
        if (movementImportIds == null || movementImportIds.isEmpty()) {
            log.warn("删除操作：movementImportIds 为空");
            return;
        }

        log.info("========== 开始异步删除报告数据，报告ID列表: {} ==========", movementImportIds);
        long startTime = System.currentTimeMillis();
        int totalDeleted = 0;

        for (String idStr : movementImportIds) {
            try {
                Long movementImportId = Long.parseLong(idStr);

                log.info("正在删除报告 ID: {} 的 MovementData 数据...", movementImportId);

                // 删除 MovementData 数据
                int deletedRows = movementDataMapper.deleteByMovementImportId(movementImportId);
                totalDeleted += deletedRows;

                log.info("删除报告 ID: {} 的 MovementData 数据完成，共 {} 条", movementImportId, deletedRows);
            } catch (NumberFormatException e) {
                log.error("无效的报告ID: {}", idStr, e);
            } catch (Exception e) {
                log.error("删除报告 ID: {} 的数据时发生错误", idStr, e);
                // 继续处理其他记录，不抛出异常
            }
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        log.info("========== 异步删除完成，共删除 {} 条 MovementData 记录，耗时: {} ms ({} 秒) ==========",
                totalDeleted, duration, duration / 1000.0);
    }
}
