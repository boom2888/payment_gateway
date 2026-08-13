package com.games.payment.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.games.common.annotation.Excel;
import com.games.common.config.RuoYiConfig;
import com.games.common.constant.Constants;
import com.games.common.enums.MovementImportStatus;
import com.games.common.exception.CustomException;
import com.games.common.utils.MessageUtils;
import com.games.common.utils.bean.BeanUtils;
import com.games.common.utils.file.FileUploadUtils;
import com.games.common.utils.poi.ExcelUtil;
import com.games.pay.dto.DailySummaryDto;
import com.games.pay.dto.NuvieDayDto;
import com.games.pay.dto.ReconReportAllDto;
import com.games.pay.dto.ReconReportMerchantDto;
import com.games.payment.domain.MerchantAccountChangeLog;
import com.games.payment.domain.MovementData;
import com.games.payment.domain.MovementImport;
import com.games.payment.enums.MerchantAccountChangeType;
import com.games.payment.mapper.MovementDataMapper;
import com.games.payment.mapper.MovementImportMapper;
import com.games.payment.service.IMerchantAccountService;
import com.games.payment.service.IMovementImportService;
import com.games.payment.service.executor.MovementImportAsyncExecutor;
import com.games.payment.service.persistence.MovementImportPersistenceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * nuvei报告导入记录Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-21
 */
@Slf4j
@Service
public class MovementImportServiceImpl extends ServiceImpl<MovementImportMapper, MovementImport>
        implements IMovementImportService {
    @Resource
    private MovementDataMapper movementDataMapper;
    @Resource
    private MovementImportMapper movementImportMapper;
    @Resource
    private IMerchantAccountService merchantAccountService;
    @Resource
    private MovementImportAsyncExecutor movementImportAsyncExecutor;
    @Resource
    private MovementImportPersistenceService movementImportPersistenceService;
    @Resource
    private MerchantAccountChangeLogServiceImpl merchantAccountChangeLogService;

    /**
     * 查询nuvei报告导入记录列表
     *
     * @param movementImport nuvei报告导入记录
     * @return nuvei报告导入记录
     */
    @Override
    public List<MovementImport> selectAllList(MovementImport movementImport) {
        return getBaseMapper().selectAllList(movementImport);
    }

    @Override
    public List<MovementImport> queryList(MovementImport movementImport) {
        LambdaQueryWrapper<MovementImport> lqw = Wrappers.lambdaQuery();
        if (movementImport.getStatus() != null) {
            lqw.eq(MovementImport::getStatus, movementImport.getStatus());
        }
        if (movementImport.getCreatedAt() != null) {
            lqw.eq(MovementImport::getCreatedAt, movementImport.getCreatedAt());
        }
        return this.list(lqw);
    }

    @Override
    public String importNuvieReport(MultipartFile file) {
        MovementImport movementImport = null;
        try {
            // 1. 读取和验证 Excel
            ExcelUtil<MovementData> util = new ExcelUtil<>(MovementData.class);
            List<MovementData> dataList = util.importExcel(file.getInputStream());

            List<String> validationErrors = validateExcelData(dataList, file.getOriginalFilename());
            if (!validationErrors.isEmpty()) {
                StringBuilder errorMsg = new StringBuilder("数据验证失败:\r\n");
                for (String error : validationErrors) {
                    errorMsg.append(error).append("\r\n");
                }
                throw new CustomException(errorMsg.toString());
            }

            // 2. 上传文件
            String filePath = RuoYiConfig.getUploadPath();
            String fileName = new FileUploadUtils().upload(filePath, file).realPath;
            String url = RuoYiConfig.getFilehost() + fileName;

            // 3. 在事务中保存/更新导入记录
            movementImport = movementImportPersistenceService
                    .saveOrUpdateImportRecord(file.getOriginalFilename(), fileName, url, dataList.size());

            // 4. 异步处理数据保存和计算（不阻塞用户）
            movementImportAsyncExecutor.processImportDataAsync(movementImport.getId(), dataList);

            return MessageUtils.message("data.import.success.background.processing");
        } catch (IOException e) {
            log.error("文件读取失败", e);
            throw new CustomException("文件读取失败: " + e.getMessage());
        } catch (Exception e) {
            log.error("导入失败", e);
            // 确保已创建的导入记录不被卡在“生成中”
            if (movementImport != null && movementImport.getId() != null) {
                MovementImport failUpdate = new MovementImport();
                failUpdate.setId(movementImport.getId());
                failUpdate.setStatus(MovementImportStatus.GENERATED_FAIL.ordinal());
                failUpdate.setRemark(e.getMessage());
                movementImportMapper.updateById(failUpdate);
            }
            throw new CustomException("导入失败: " + e.getMessage());
        }
    }

    /**
     * 验证Excel导入的数据
     *
     * @param dataList 数据列表
     * @param fileName 文件名
     * @return 验证错误信息列表
     */
    private List<String> validateExcelData(List<MovementData> dataList, String fileName) {
        List<String> errors = new ArrayList<>();

        for (int i = 0; i < dataList.size(); i++) {
            MovementData data = dataList.get(i);
            if (data == null)
                continue;

            // 验证amount字段
            if (data.getAmount() == null) {
                errors.add("第" + (i + 2) + "行 Amount 列不是有效数字");
            }
            if (data.getDiscountFee() == null) {
                errors.add("第" + (i + 2) + "行 Discount Fee 列不是有效数字");
            }
            if (data.getTransactionFee() == null) {
                errors.add("第" + (i + 2) + "行 Transaction Fee 列不是有效数字");
            }
            if (data.getServiceFee() == null) {
                errors.add("第" + (i + 2) + "行 Service Fee 列不是有效数字");
            }
            if (data.getReserve() == null) {
                errors.add("第" + (i + 2) + "行 Reserve 列不是有效数字");
            }
            if (data.getVatOnFees() == null) {
                errors.add("第" + (i + 2) + "行 VAT on Fees 列不是有效数字");
            }
            // 验证netAmount字段
            if (data.getNetAmount() == null) {
                errors.add("第" + (i + 2) + "行 Net Amount 列不是有效数字");
            }
            if (data.getSettlementAmount() == null) {
                errors.add("第" + (i + 2) + "行 Settlement Amount 列不是有效数字");
            }
            if (data.getSettlementReserve() == null) {
                errors.add("第" + (i + 2) + "行 Settlement Reserve 列不是有效数字");
            }
            if (data.getResellerCommission() == null) {
                errors.add("第" + (i + 2) + "行 Reseller Commission 列不是有效数字");
            }
            if (data.getInterchangeFee() == null) {
                errors.add("第" + (i + 2) + "行 Interchange Fee 列不是有效数字");
            }
            if (data.getEndUserAmount() == null) {
                errors.add("第" + (i + 2) + "行 End User Amount 列不是有效数字");
            }
        }

        return errors;
    }

    @Override
    public byte[] exportReconReport(Long movementImportId) throws IOException {
        List<ReconReportAllDto> all = buildReconReportList(movementImportId);
        Map<String, List<ReconReportAllDto>> group = all.stream().filter(e -> e.getMerchantId() != null)
                .collect(Collectors.groupingBy(ReconReportAllDto::getMerchantId));

        ByteArrayOutputStream zipBaos = new ByteArrayOutputStream();
        try (ZipOutputStream zip = new ZipOutputStream(zipBaos)) {
            for (Map.Entry<String, List<ReconReportAllDto>> e : group.entrySet()) {
                // 1 内账
                byte[] innerBytes = buildExcelBytes(e.getValue(), ReconReportAllDto.class,
                        "merchant_inner_" + e.getKey());
                addBytesToZip(zip, "merchant_inner_" + e.getKey() + ".xlsx", innerBytes);

                // 2 外账
                List<ReconReportMerchantDto> outList = e.getValue().stream()
                        .map(src -> {
                            ReconReportMerchantDto dto = new ReconReportMerchantDto();
                            BeanUtils.copyProperties(src, dto);
                            return dto;
                        }).collect(Collectors.toList());
                byte[] outerBytes = buildExcelBytes(outList, ReconReportMerchantDto.class,
                        "merchant_outer_" + e.getKey());
                addBytesToZip(zip, "merchant_outer_" + e.getKey() + ".xlsx", outerBytes);
            }

            // 3 总帐
            byte[] bytes = buildExcelBytes(all, ReconReportAllDto.class, "merchant_all");
            addBytesToZip(zip, "merchant_all.xlsx", bytes);
        }

        return zipBaos.toByteArray();
    }

    /**
     * 把原始 MovementData 明细 → 商户-维度 内账 DTO 列表
     */
    private List<ReconReportAllDto> buildReconReportList(Long movementImportId) {
        List<MovementData> raw = movementDataMapper.exportRawData(movementImportId);

        // 按 client_unique_id 分组
        Map<String, List<MovementData>> clientGroup = raw.stream()
                .collect(Collectors.groupingBy(m -> m.getClientUniqueId() == null ? "blank" : m.getClientUniqueId()));

        List<ReconReportAllDto> result = new ArrayList<>();

        clientGroup.forEach((clientId, rows) -> {
            ReconReportAllDto dto = new ReconReportAllDto();

            // === 公共字段 ===
            MovementData first = rows.get(0);
            dto.setRowLabels(String.valueOf(clientId));
            dto.setPayoutId(String.valueOf(first.getPayoutId()));
            dto.setMerchantId(first.getMerchantId());
            dto.setOrderId(String.valueOf(clientId));
            dto.setCurrencyId(first.getCurrency());
            dto.setCryptoId(first.getCryptoId());
            dto.setOrderStatus(mapStatus(first.getOrderStatusRaw()));
            dto.setCreatedAt(DateUtil.parse(first.getMovementDate()));
            dto.setSsoOrderId(first.getSsoOrderId());
            dto.setFxRate(first.getFxRate() == null ? null : first.getFxRate());
            dto.setEurUsd(first.getFxRate() == null ? null : first.getFxRate());
            dto.setIssuingCountry(first.getCountry());
            dto.setRegion(first.getRegionFlag() != null ? "EU" : "NON-EU");
            dto.setFeeType(first.getFeeType());
            dto.setCreatedAt(first.getCreatedAt());
            dto.setCurrencyId(first.getCurrencyId());

            boolean refund = rows.stream().anyMatch(r -> "Credits".equals(r.getMovementType()));
            boolean rdr = rows.stream().anyMatch(r -> "Pre-Dispute Resolution".equals(r.getMovementType()));
            boolean cb = rows.stream().anyMatch(r -> "Chargeback".equals(r.getMovementType()));
            boolean cbCancelled = rows.stream().anyMatch(r -> "Chargeback Cancelled".equals(r.getMovementType()));
            dto.setRefund(refund ? 1 : 0);
            dto.setRdr(rdr ? 1 : 0);
            dto.setCb(cb ? 1 : 0);
            dto.setCbCancelled(cbCancelled ? 1 : 0);

            // === 金额汇总 ===
            dto.setSumOfAmount(sum(rows, MovementData::getAmount));
            dto.setSumOfDiscountFee(sum(rows, MovementData::getDiscountFee));
            dto.setSumOfTransactionFee(sum(rows, MovementData::getTransactionFee));
            dto.setSumOfReserve(sum(rows, MovementData::getReserve));
            dto.setSumOfNetAmount(sum(rows, MovementData::getNetAmount));
            dto.setSumOfSettlementAmount(sum(rows, MovementData::getSettlementAmount));
            dto.setSumOfSettlementReserve(sum(rows, MovementData::getSettlementReserve));
            dto.setOrderAmount(first.getFiatAmount());
            dto.setTransferAmount(sum(rows, MovementData::getTransferAmount));
            dto.setCryptoAmount(sum(rows, MovementData::getCryptoAmount));

            // === 费用类 ===
            dto.setRefundFee(sum(rows, MovementData::getMerchantRefundFee));
            dto.setRdrFee(sum(rows, MovementData::getMerchantRdrFee));
            dto.setChargebackFee(sum(rows, MovementData::getMerchantChargebackFee));
            dto.setProcessingFee(sum(rows, MovementData::getMerchantProcessingFee));
            dto.setRollingReserve(sum(rows, MovementData::getMerchantRollingReserve).toPlainString());
            dto.setGatewayFeeUsd(sum(rows, MovementData::getMerchantGatewayFee));
            dto.setSettlement(sum(rows, MovementData::getMerchantSettlementFee));

            // === 按 movement_type 标记 & 金额 ===
            Map<String, BigDecimal> typeAmount = rows.stream()
                    .filter(d -> d.getTransferAmount() != null)
                    .collect(Collectors.groupingBy(
                            MovementData::getMovementType,
                            Collectors.collectingAndThen(
                                    Collectors.reducing(BigDecimal.ZERO, MovementData::getTransferAmount,
                                            BigDecimal::add),
                                    sum -> sum.setScale(2, RoundingMode.HALF_UP))));

            dto.setRefundAmount(typeAmount.getOrDefault("Credits", BigDecimal.ZERO));
            dto.setRdrAmount(typeAmount.getOrDefault("Pre-Dispute Resolution", BigDecimal.ZERO));
            dto.setCbAmount(typeAmount.getOrDefault("Chargeback", BigDecimal.ZERO));
            dto.setCbCancelledAmount(typeAmount.getOrDefault("Chargeback Cancelled", BigDecimal.ZERO));

            result.add(dto);
        });

        return result.stream()
                .sorted(Comparator.comparing(ReconReportAllDto::getRowLabels))
                .collect(Collectors.toList());
    }

    /* ========== 工具 ========== */
    private BigDecimal sum(List<MovementData> list, java.util.function.Function<MovementData, BigDecimal> mapper) {
        return list.stream()
                .map(mapper)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private String mapStatus(Integer raw) {
        if (raw == null)
            return "Unknown";
        switch (raw) {
            case 0:
                return "Created";
            case 1:
                return "AML Start";
            case 2:
                return "AML Pause";
            case 3:
                return "AML Declined";
            case 4:
                return "Waiting for Deposit";
            case 5:
                return "Processing";
            case 6:
                return "Deposit Declined";
            case 7:
                return "Order Cancelled";
            case 8:
                return "Fulfilled";
            case 9:
                return "Finished";
            case 10:
                return "Refunded";
            case 16:
                return "Blacklist";
            case 17:
                return "3DS Pending";
            default:
                return "Unknown";
        }
    }

    private Integer bool(Map<String, BigDecimal> map, String type) {
        return map.getOrDefault(type, BigDecimal.ZERO).signum() > 0 ? 1 : 0;
    }

    private <T> byte[] buildExcelBytes(List<T> data, Class<T> clazz, String sheetName) {
        ExcelUtil<T> util = new ExcelUtil<>(clazz);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        util.init(data, sheetName, Excel.Type.EXPORT);
        // 直接写流
        util.exportExcel(bos);
        return bos.toByteArray();
    }

    private void addBytesToZip(ZipOutputStream zip, String entryName, byte[] bytes) throws IOException {
        zip.putNextEntry(new ZipEntry(entryName));
        IOUtils.write(bytes, zip);
        zip.closeEntry();
    }

    @Override
    public String exportDailySummary(List<String> movementImportIds) {
        // 查询这些报告的月份
        String month = movementDataMapper.selectMonths(movementImportIds);
        // 查询这些报告的日期，每份报告只取其最后一日，当作列名
        List<NuvieDayDto> days = movementDataMapper.selectBatchDays(movementImportIds);

        // 分组查询商户每份报告的汇总数据
        // List<DailySummaryDto> dailySummaryList =
        // movementDataMapper.selectDailySummarys(movementImportIds);

        List<Long> ids = movementImportIds.stream()
                .map(Long::valueOf)
                .collect(Collectors.toList());
        // 3. 使用本地方法替代原来的 SQL
        List<DailySummaryDto> dailySummaryList = buildDailySummaryList(ids);
        // 构建表头
        List<String> title = new ArrayList<>(Arrays.asList("Merchant", month));
        title.addAll(days.stream().map(NuvieDayDto::getDay).collect(Collectors.toList()));

        List<List<Object>> data = CollUtil.newArrayList(
                new ArrayList<>(Arrays.asList("B2C2 fx rate", "")),
                new ArrayList<>(Arrays.asList("Payout ID", "")));
        for (NuvieDayDto ignored : days) {
            data.get(0).add(ignored.getB2c2FxRate());
            // 同一份报告的payoutId一样，随便取一条数据即可
            data.get(1).add(dailySummaryList.get(0).getPayoutId() == null ? "" : dailySummaryList.get(0).getPayoutId());
        }

        // 汇总所有商户数据形成单独一行
        List<Object> merchantTotalSettlementRow = CollUtil.newArrayList("Merchant Total", "Settlement");
        List<Object> merchantTotalOrderAmountUsdRow = CollUtil.newArrayList("Merchant Total", "Order Amount USD");
        List<Object> merchantTotalOrderAmountEurRow = CollUtil.newArrayList("Merchant Total", "Order Amount EUR");
        List<Object> merchantTotalRollingReserveUsdRow = CollUtil.newArrayList("Merchant Total", "Rolling Reserve USD");
        List<Object> merchantTotalRollingReserveEurRow = CollUtil.newArrayList("Merchant Total", "Rolling Reserve EUR");
        List<Object> merchantTotalChargebackRefundsRow = CollUtil.newArrayList("Merchant Total",
                "Chargeback & Refunds");
        List<Object> merchantTotalFeesRow = CollUtil.newArrayList("Merchant Total", "Fees");
        List<Object> merchantTotalRdrEthocaFromMerchantsRow = CollUtil.newArrayList("Merchant Total",
                "RDR&Ethoca from Merchants");
        List<Object> merchantTotalRdrEthocaFromSuppliersRow = CollUtil.newArrayList("Merchant Total",
                "RDR&Ethoca from Suppliers");
        List<Object> merchantTotalFxRateRow = CollUtil.newArrayList("Merchant Total", "XE.com FX Rate");
        List<Object> merchantTotalRollingReservePayoutRow = CollUtil.newArrayList("Merchant Total",
                "Rolling Reserve Payout");
        List<Object> merchantTotalDiscrepancyRow = CollUtil.newArrayList("Merchant Total", "Discrepancy");

        // nuvie报告行
        List<Object> nuvieTotalRollingReserveRow = CollUtil.newArrayList("Nuvie", "Rolling Reserve");
        List<Object> nuvieTotalOrderAmountRow = CollUtil.newArrayList("Nuvie", "Order Amount");
        List<Object> nuvieTotalBlankRow = CollUtil.newArrayList("Nuvie", "");
        List<Object> nuvieTotalFeesRow = CollUtil.newArrayList("Nuvie", "Fees");
        List<Object> nuvieTotalRollingReservePayoutRow = CollUtil.newArrayList("Nuvie", "RR Paid Out");
        List<Object> nuvieTotalSettlementInBankRow = CollUtil.newArrayList("Nuvie", "Settlement in Bank");

        // 其他行
        List<Object> otherFxOnOrderAmountsRow = CollUtil.newArrayList("Other", "FX on Order Amounts");
        List<Object> otherRdrEthocaRow = CollUtil.newArrayList("Other", "RDR&Ethoca");
        List<Object> otherFeeDiffRow = CollUtil.newArrayList("Other", "Fee Difference");
        List<Object> otherPLRow = CollUtil.newArrayList("Other", "P&L");

        // 按商户分组，组内为商户的每份报告数据
        Map<String, Map<String, DailySummaryDto>> merchantDayMap = dailySummaryList.stream()
                .collect(Collectors.groupingBy(
                        DailySummaryDto::getBusinessName,
                        Collectors.toMap(DailySummaryDto::getMovementImportId, dto -> dto, (a, b) -> b)));
        // 填充商户数据
        for (String merchant : merchantDayMap.keySet()) {
            Map<String, DailySummaryDto> dayMap = merchantDayMap.get(merchant);

            List<Object> settlementRow = CollUtil.newArrayList(merchant, "Settlement");
            List<Object> orderAmountUsdRow = CollUtil.newArrayList(merchant, "Order Amount USD");
            List<Object> orderAmountEurRow = CollUtil.newArrayList(merchant, "Order Amount EUR");
            List<Object> rollingReserveUsdRow = CollUtil.newArrayList(merchant, "Rolling Reserve USD");
            List<Object> rollingReserveEurRow = CollUtil.newArrayList(merchant, "Rolling Reserve EUR");
            List<Object> chargebackRefundsRow = CollUtil.newArrayList(merchant, "Chargeback & Refunds");
            List<Object> feesRow = CollUtil.newArrayList(merchant, "Fees");
            List<Object> rDREthocaRow = CollUtil.newArrayList(merchant, "RDR&Ethoca");
            List<Object> fxRateRow = CollUtil.newArrayList(merchant, "XE.com FX Rate");
            List<Object> rollingReservePayoutRow = CollUtil.newArrayList(merchant, "Rolling Reserve Payout");
            List<Object> discrepancyRow = CollUtil.newArrayList(merchant, "Discrepancy");

            for (NuvieDayDto day : days) {
                DailySummaryDto dto = dayMap.get(day.getMovementImportId());
                if (dto != null) {
                    settlementRow.add(formatCurrency(Constants.USDS, dto.getSettlement()));
                    orderAmountUsdRow.add(formatCurrency(Constants.USDS, dto.getOrderAmountUsd()));
                    orderAmountEurRow.add(formatCurrency(Constants.EURS, dto.getOrderAmountEur()));
                    rollingReserveUsdRow.add(formatCurrency(Constants.USDS, dto.getRollingReserveUsd()));
                    rollingReserveEurRow.add(formatCurrency(Constants.EURS, dto.getRollingReserveEur()));
                    chargebackRefundsRow.add(formatCurrency(Constants.USDS, dto.getChargebackRefunds()));
                    feesRow.add(formatCurrency(Constants.USDS, dto.getFees()));
                    fxRateRow.add(dto.getFxRate().toPlainString());
                } else {
                    settlementRow.add(Constants.USD_EMPTY);
                    orderAmountUsdRow.add(Constants.USD_EMPTY);
                    orderAmountEurRow.add(Constants.EUR_EMPTY);
                    rollingReserveUsdRow.add(Constants.USD_EMPTY);
                    rollingReserveEurRow.add(Constants.EUR_EMPTY);
                    chargebackRefundsRow.add(Constants.USD_EMPTY);
                    feesRow.add(Constants.USD_EMPTY);
                    fxRateRow.add("");
                }
                rollingReservePayoutRow.add("");
                discrepancyRow.add("");
                rDREthocaRow.add("");
            }

            data.add(settlementRow);
            data.add(orderAmountUsdRow);
            data.add(orderAmountEurRow);
            data.add(rollingReserveUsdRow);
            data.add(rollingReserveEurRow);
            data.add(chargebackRefundsRow);
            data.add(feesRow);
            data.add(rDREthocaRow);
            data.add(fxRateRow);
            data.add(rollingReservePayoutRow);
            data.add(discrepancyRow);
        }

        // === 追加：所有商户总和统计行 ===
        for (NuvieDayDto day : days) {
            BigDecimal totalSettlement = dailySummaryList.stream()
                    .filter(dto -> day.getMovementImportId().equals(dto.getMovementImportId()))
                    .map(DailySummaryDto::getSettlement)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal merchantTotalOrderAmountUsd = dailySummaryList.stream()
                    .filter(dto -> day.getMovementImportId().equals(dto.getMovementImportId()))
                    .map(DailySummaryDto::getOrderAmountUsd)
                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totalOrderAmountEur = dailySummaryList.stream()
                    .filter(dto -> day.getMovementImportId().equals(dto.getMovementImportId()))
                    .map(DailySummaryDto::getOrderAmountEur)
                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal nuvieTotalOrderAmount = dailySummaryList.stream()
                    .filter(dto -> day.getMovementImportId().equals(dto.getMovementImportId()))
                    .map(DailySummaryDto::getOrderAmount)
                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totalRollingReserveUsd = dailySummaryList.stream()
                    .filter(dto -> day.getMovementImportId().equals(dto.getMovementImportId()))
                    .map(DailySummaryDto::getRollingReserveUsd)
                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totalRollingReserveEur = dailySummaryList.stream()
                    .filter(dto -> day.getMovementImportId().equals(dto.getMovementImportId()))
                    .map(DailySummaryDto::getRollingReserveEur)
                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totalRollingReserve = dailySummaryList.stream()
                    .filter(dto -> day.getMovementImportId().equals(dto.getMovementImportId()))
                    .map(DailySummaryDto::getRollingReserve)
                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal merchantTotalChargebackRefunds = dailySummaryList.stream()
                    .filter(dto -> day.getMovementImportId().equals(dto.getMovementImportId()))
                    .map(DailySummaryDto::getChargebackRefunds)
                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totalNuvieChargebackRefunds = dailySummaryList.stream()
                    .filter(dto -> day.getMovementImportId().equals(dto.getMovementImportId()))
                    .map(DailySummaryDto::getNuvieChargebackRefunds)
                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal merchantTotalFees = dailySummaryList.stream()
                    .filter(dto -> day.getMovementImportId().equals(dto.getMovementImportId()))
                    .map(DailySummaryDto::getFees)
                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal nuvieTotalFees = dailySummaryList.stream()
                    .filter(dto -> day.getMovementImportId().equals(dto.getMovementImportId()))
                    .map(DailySummaryDto::getNuvieFees)
                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totalRollingReservePayout = dailySummaryList.stream()
                    .filter(dto -> day.getMovementImportId().equals(dto.getMovementImportId()))
                    .map(DailySummaryDto::getRollingReservePayout)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totalSettlementInBank = dailySummaryList.stream()
                    .filter(dto -> day.getMovementImportId().equals(dto.getMovementImportId()))
                    .map(DailySummaryDto::getSettlementInBank)
                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal otherTotalFxOnOrderAmounts = day.getB2c2FxRate().multiply(nuvieTotalOrderAmount)
                    .subtract(merchantTotalOrderAmountUsd).setScale(2, RoundingMode.HALF_UP);
            BigDecimal otherTotalGain = merchantTotalChargebackRefunds.subtract(totalNuvieChargebackRefunds);
            BigDecimal otherTotalFeeDiff = merchantTotalFees.subtract(nuvieTotalFees.multiply(day.getB2c2FxRate()))
                    .add(merchantTotalChargebackRefunds).setScale(2, RoundingMode.HALF_UP);
            BigDecimal otherTotalPL = otherTotalFxOnOrderAmounts.add(otherTotalGain).add(otherTotalFeeDiff);

            // 商户行添加数据
            merchantTotalSettlementRow.add(formatCurrency(Constants.USDS, totalSettlement));
            merchantTotalOrderAmountUsdRow.add(formatCurrency(Constants.USDS, merchantTotalOrderAmountUsd));
            merchantTotalOrderAmountEurRow.add(formatCurrency(Constants.EURS, totalOrderAmountEur));
            merchantTotalRollingReserveUsdRow.add(formatCurrency(Constants.USDS, totalRollingReserveUsd));
            merchantTotalRollingReserveEurRow.add(formatCurrency(Constants.EURS, totalRollingReserveEur));
            merchantTotalChargebackRefundsRow.add(formatCurrency(Constants.USDS, merchantTotalChargebackRefunds));
            merchantTotalFeesRow.add(formatCurrency(Constants.USDS, merchantTotalFees));
            merchantTotalRdrEthocaFromMerchantsRow.add(formatCurrency(Constants.USDS, null));
            merchantTotalRdrEthocaFromSuppliersRow.add(formatCurrency(Constants.USDS, null));
            merchantTotalFxRateRow.add(""); // 汇总行不显示汇率
            merchantTotalRollingReservePayoutRow.add(formatCurrency(Constants.USDS, null));
            merchantTotalDiscrepancyRow.add("");

            // nuvie行添加数据
            nuvieTotalRollingReserveRow.add(formatCurrency(Constants.EURS, totalRollingReserve));
            nuvieTotalOrderAmountRow.add(formatCurrency(Constants.EURS, nuvieTotalOrderAmount));
            nuvieTotalBlankRow.add("");
            nuvieTotalFeesRow.add(formatCurrency(Constants.EURS, nuvieTotalFees));
            nuvieTotalRollingReservePayoutRow.add(formatCurrency(Constants.USDS, null));
            nuvieTotalSettlementInBankRow.add(formatCurrency(Constants.EURS, totalSettlementInBank));

            // 其他行添加数据
            otherFxOnOrderAmountsRow.add(otherTotalFxOnOrderAmounts);
            otherRdrEthocaRow.add("");
            otherFeeDiffRow.add(formatCurrency(Constants.USDS, otherTotalFeeDiff));
            otherPLRow.add(formatCurrency(Constants.USDS, otherTotalPL));
        }

        // 添加商户汇总行
        data.add(merchantTotalSettlementRow);
        data.add(merchantTotalOrderAmountUsdRow);
        data.add(merchantTotalOrderAmountEurRow);
        data.add(merchantTotalRollingReserveUsdRow);
        data.add(merchantTotalRollingReserveEurRow);
        data.add(merchantTotalChargebackRefundsRow);
        data.add(merchantTotalFeesRow);
        data.add(merchantTotalRollingReservePayoutRow);
        data.add(merchantTotalDiscrepancyRow);

        // 添加nuvie汇总行
        data.add(nuvieTotalRollingReserveRow);
        data.add(nuvieTotalOrderAmountRow);
        data.add(nuvieTotalBlankRow);
        data.add(nuvieTotalFeesRow);
        data.add(nuvieTotalRollingReservePayoutRow);
        data.add(nuvieTotalSettlementInBankRow);

        // 添加其他行
        data.add(otherFxOnOrderAmountsRow);
        data.add(otherRdrEthocaRow);
        data.add(otherFeeDiffRow);
        data.add(otherPLRow);

        // 确保输出目录存在
        String filename = "DailySummary.xlsx";
        String downloadPath = RuoYiConfig.getDownloadPath() + filename;
        File desc = new File(downloadPath);
        if (!desc.getParentFile().exists()) {
            desc.getParentFile().mkdirs();
        }

        // 创建ExcelWriter（使用指定路径）
        ExcelWriter writer = new ExcelWriter(downloadPath);
        // 写入表头
        writer.writeRow(title);

        // 写入内容
        writer.write(data, true);

        // 合并第一列（索引 0）
        mergeSameColumnValues(writer, data, 0);

        // 关闭writer，写入磁盘
        writer.close();

        return filename;
    }

    // ===== 1. 新增：把原始行映射为 DailySummaryDto 的本地方法 =====
    private List<DailySummaryDto> buildDailySummaryList(List<Long> movementImportIds) {
        List<MovementData> rawList = movementDataMapper.selectRawData(movementImportIds);

        // 按 merchantId + movementImportId 分组
        Map<String, List<MovementData>> group = rawList.stream()
                .collect(Collectors.groupingBy(r -> r.getMerchantId() + "_" + r.getMovementImportId()));

        List<DailySummaryDto> summaryList = new ArrayList<>();

        group.forEach((key, rows) -> {
            String importId = key.split("_")[1];

            DailySummaryDto dto = new DailySummaryDto();
            // 获取汇率，如果为null则使用默认值1.0
            BigDecimal fxRate = Optional.ofNullable(rows.get(0).getFxRate()).orElse(BigDecimal.ONE);
            dto.setB2c2FxRate(rows.get(0).getB2c2FxRate());
            dto.setBusinessName(rows.get(0).getBusinessName());
            dto.setMovementImportId(importId);
            dto.setDay(
                    DateUtil.format(
                            DateUtil.parse(rows.stream()
                                    .map(MovementData::getMovementDate)
                                    .max(String::compareTo)
                                    .orElse("")),
                            "yyyy-MM-dd"));
            dto.setPayoutId(rows.get(0).getPayoutId());
            dto.setFxRate(rows.get(0).getFxRate());

            /* ---------- 计算各字段 ---------- */
            BigDecimal settlementFeeSum = rows.stream()
                    .map(MovementData::getMerchantSettlementFee)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            dto.setSettlement(settlementFeeSum.signum() >= 0
                    ? settlementFeeSum.setScale(2, RoundingMode.DOWN)
                    : settlementFeeSum.abs()
                            .setScale(2, RoundingMode.UP)
                            .negate());

            BigDecimal amountUsd = sumAmountByCurrency(rows, "USD");
            dto.setOrderAmountUsd(amountUsd);
            dto.setOrderAmountEur(amountUsd.divide(fxRate, 2, RoundingMode.HALF_DOWN));

            BigDecimal rollingReserveUsd = sumBig(rows, MovementData::getMerchantRollingReserve);
            dto.setRollingReserveUsd(rollingReserveUsd);
            dto.setRollingReserveEur(rollingReserveUsd.divide(fxRate, 2, RoundingMode.HALF_DOWN));

            BigDecimal orderAmount = rows.stream()
                    .filter(r -> "Sales".equals(r.getMovementType()))
                    .map(r -> {
                        BigDecimal netAmount = r.getNetAmount();
                        // 避免除以null或0
                        if (netAmount == null || netAmount.compareTo(BigDecimal.ZERO) == 0) {
                            return BigDecimal.ZERO;
                        }
                        return Optional.ofNullable(r.getSettlementAmount()).orElse(BigDecimal.ZERO)
                                .multiply(Optional.ofNullable(r.getAmount()).orElse(BigDecimal.ZERO))
                                .divide(netAmount, 2, RoundingMode.HALF_UP);
                    })
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            dto.setOrderAmount(orderAmount);

            dto.setRollingReserve(sumBig(rows, MovementData::getSettlementReserve));

            BigDecimal chargeback = rows.stream()
                    .filter(r -> "Chargeback".equals(r.getMovementType()))
                    .map(r -> Optional.ofNullable(r.getTransferAmount()).orElse(BigDecimal.ZERO).abs()
                            .add(Optional.ofNullable(r.getCbFee()).orElse(BigDecimal.ZERO)))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal credits = rows.stream()
                    .filter(r -> "Credits".equals(r.getMovementType()))
                    .map(r -> Optional.ofNullable(r.getTransferAmount()).orElse(BigDecimal.ZERO).abs()
                            .add(Optional.ofNullable(r.getRefundFee()).orElse(BigDecimal.ZERO)))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            dto.setChargebackRefunds(chargeback.add(credits).setScale(2, RoundingMode.HALF_UP));

            BigDecimal cbSettle = rows.stream()
                    .filter(r -> "Chargeback".equals(r.getMovementType()))
                    .map(r -> Optional.ofNullable(r.getSettlementAmount()).orElse(BigDecimal.ZERO).abs()
                            .multiply(Optional.ofNullable(r.getFxRate()).orElse(BigDecimal.ONE)))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal creditsSettle = rows.stream()
                    .filter(r -> "Credits".equals(r.getMovementType()))
                    .map(r -> Optional.ofNullable(r.getSettlementAmount()).orElse(BigDecimal.ZERO).abs()
                            .multiply(Optional.ofNullable(r.getFxRate()).orElse(BigDecimal.ONE)))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            dto.setNuvieChargebackRefunds(cbSettle.add(creditsSettle).setScale(2, RoundingMode.HALF_UP));

            BigDecimal fees = rows.stream()
                    .map(r -> Optional.ofNullable(r.getMerchantProcessingFee()).orElse(BigDecimal.ZERO)
                            .add(Optional.ofNullable(r.getMerchantGatewayFee()).orElse(BigDecimal.ZERO)))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            dto.setFees(fees.setScale(2, RoundingMode.HALF_UP));

            dto.setRollingReservePayout(sumBig(rows, MovementData::getRollingFee));

            dto.setSettlementInBank(sumBig(rows, MovementData::getSettlementAmount));

            BigDecimal nuvieFees = rows.stream()
                    .map(r -> {
                        BigDecimal amt = Optional.ofNullable(r.getSettlementAmount()).orElse(BigDecimal.ZERO);
                        BigDecimal disc = Optional.ofNullable(r.getDiscountFee()).orElse(BigDecimal.ZERO);
                        BigDecimal trans = Optional.ofNullable(r.getTransactionFee()).orElse(BigDecimal.ZERO);
                        BigDecimal net = r.getNetAmount();
                        BigDecimal amount = Optional.ofNullable(r.getAmount()).orElse(BigDecimal.ZERO);

                        // 避免除以null或0
                        if (net == null || net.compareTo(BigDecimal.ZERO) == 0) {
                            return BigDecimal.ZERO;
                        }

                        BigDecimal part1 = amt.negate()
                                .multiply(disc)
                                .divide(net, 10, RoundingMode.HALF_UP);
                        BigDecimal part2 = amt.negate()
                                .multiply(trans)
                                .divide(net, 10, RoundingMode.HALF_UP);
                        BigDecimal part3 = amt.negate()
                                .multiply(amount)
                                .divide(net, 10, RoundingMode.HALF_UP);

                        return part1.add(part2).add(part3);
                    })
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            dto.setNuvieFees(nuvieFees.add(orderAmount).setScale(2, RoundingMode.HALF_UP));

            BigDecimal fxOnAmount = rows.stream()
                    .map(r -> Optional.ofNullable(r.getAmount()).orElse(BigDecimal.ZERO).abs()
                            .multiply(Optional.ofNullable(r.getBaseRate()).orElse(BigDecimal.ZERO))
                            .subtract(Optional.ofNullable(r.getTransferAmount()).orElse(BigDecimal.ZERO).abs()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            dto.setFxOnOrderAmounts(fxOnAmount.setScale(2, RoundingMode.HALF_UP));

            summaryList.add(dto);
        });

        return summaryList;
    }

    // ===== 2. 工具：按币种汇总 amount =====
    private BigDecimal sumAmountByCurrency(List<MovementData> rows, String currency) {
        return rows.stream()
                // .filter(r -> currency.equals(r.getCurrency()))
                .map(MovementData::getTransferAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // ===== 3. 工具：汇总任意 BigDecimal 字段 =====
    private BigDecimal sumBig(List<MovementData> rows, java.util.function.Function<MovementData, BigDecimal> mapper) {
        return rows.stream()
                .map(mapper)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 合并相同值的列（仅限第一列）
     */
    private void mergeSameColumnValues(ExcelWriter writer, List<List<Object>> rows, int colIndex) {
        if (CollUtil.isEmpty(rows))
            return;

        Object prevValue = null;
        int startRow = 1; // 数据从第1行开始（第0行为表头）

        for (int i = 0; i < rows.size(); i++) {
            Object currentValue = rows.get(i).get(colIndex);
            if (!Objects.equals(currentValue, prevValue)) {
                int endRow = i;
                if (endRow - startRow >= 1) {
                    try {
                        writer.merge(startRow, endRow, colIndex, colIndex, prevValue, false);
                    } catch (IllegalStateException e) {
                        // 如果区域已经合并，则跳过
                        if (!e.getMessage().contains("overlaps with an existing merged region")) {
                            // 如果是其他异常，则记录日志
                            log.error("mergeSameColumnValues error: " + e.getMessage());
                        }
                    } catch (Exception e) {
                        log.error("mergeSameColumnValues error: " + e.getMessage());
                    }
                }
                startRow = i + 1;
                prevValue = currentValue;
            }
        }

        // 合并最后一段（防止漏掉）
        int endRow = rows.size();
        if (endRow - startRow >= 1) {
            try {
                writer.merge(startRow, endRow, colIndex, colIndex, prevValue, false);
            } catch (IllegalStateException e) {
                // 如果区域已经合并，则跳过
                if (!e.getMessage().contains("overlaps with an existing merged region")) {
                    // 如果是其他异常，则记录日志
                    log.error("mergeSameColumnValues error: " + e.getMessage());
                }
            } catch (Exception e) {
                log.error("mergeSameColumnValues error: " + e.getMessage());
            }
        }
    }

    private String formatCurrency(String symbol, BigDecimal value) {
        if (value == null) {
            return symbol + "    -";
        }
        return symbol + value;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean invalid(Long movementImportId) {
        MovementImport movementImport = getById(movementImportId);
        log.info("开始作废对账报告，movementImportId: {}，当前状态: {}，导入时间: {}",
                movementImportId,
                movementImport == null ? null : movementImport.getStatus(),
                movementImport == null ? null : movementImport.getCreatedAt());

        if (movementImport == null) {
            throw new CustomException(MessageUtils.message("movement.import.invalid.failed"));
        }

        // 仅允许对 已生成 / 生成失败 的报告执行作废操作
        if (!movementImport.getStatus().equals(MovementImportStatus.GENERATED.ordinal())
                && !movementImport.getStatus().equals(MovementImportStatus.GENERATED_FAIL.ordinal())) {
            throw new CustomException(MessageUtils.message("operation.only.success.status"));
        }

        boolean needRollBack = true;
        List<MerchantAccountChangeLog> changeLogs = new ArrayList<>();

        // 对于生成失败的报告，只有在发现导入时间之后存在资产变更记录时才回滚
        if (movementImport.getStatus().equals(MovementImportStatus.GENERATED_FAIL.ordinal())) {
            changeLogs = merchantAccountChangeLogService.selectByMovementImportId(movementImportId,
                    MerchantAccountChangeType.UPDATE_BALANCE.getCode());
            log.info("作废生成失败的对账报告时查询到资产变更记录条数: {}", changeLogs == null ? 0 : changeLogs.size());

            if (movementImport.getCreatedAt() != null && changeLogs != null && !changeLogs.isEmpty()) {
                needRollBack = changeLogs.stream()
                        .filter(log -> log.getCreatedAt() != null)
                        .anyMatch(log -> log.getCreatedAt().after(movementImport.getCreatedAt()));
            } else {
                needRollBack = false;
            }
        }

        log.info("作废对账报告时是否需要回滚商户资产 needRollBack: {}", needRollBack);

        int rows = movementImportMapper.invalid(movementImportId);
        if (rows == 0) {
            throw new CustomException(MessageUtils.message("movement.import.invalid.failed"));
        }
        List<MovementData> merchantSettlementList = movementDataMapper.merchantSettlement(movementImportId);
        log.info("作废对账报告时查询到用于回滚的商户结算记录数: {}", merchantSettlementList == null ? 0 : merchantSettlementList.size());
        // 回滚商户资产
        if (needRollBack) {
            merchantAccountService.rollbackBalance(merchantSettlementList);
        }
        return true;
    }

}
