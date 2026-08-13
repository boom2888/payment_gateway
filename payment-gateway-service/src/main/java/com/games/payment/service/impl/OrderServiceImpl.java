package com.games.payment.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.games.api.enums.OrderStatus;
import com.games.common.enums.FailReasonType;
import com.games.common.enums.NuvieOrderStatus;
import com.games.common.enums.TradeCallBackStatus;
import com.games.common.utils.DateUtils;
import com.games.common.utils.MessageUtils;
import com.games.framework.web.service.TokenService;
import com.games.pay.dto.OrderStatusStatistics;
import com.games.pay.dto.OrderWithFeeModelDto;
import com.games.pay.vo.*;
import cn.hutool.json.JSONUtil;
import com.games.api.vo.PayOrderVo;
import com.games.common.core.domain.AjaxResult;
import com.games.common.exception.CustomException;
import com.games.common.utils.poi.ExcelUtil;
import com.games.payment.domain.*;
import com.games.payment.mapper.MovementDataMapper;
import com.games.payment.mapper.OrderMapper;
import com.games.payment.service.*;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;
import java.util.LinkedHashSet;
import java.util.TimeZone;

/**
 * 订单Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Resource
    private OrderMapper orderMapper;
    @Resource
    private TokenService tokenService;
    @Resource
    private MovementDataMapper movementDataMapper;
    @Resource
    private IRefundOrderService refundOrderService;
    @Resource
    private IShopService shopService;
    @Resource
    private ICustomerWalletService customerWalletService;
    @Resource
    private ICustomerService customerService;
    @Resource
    private IUserService userService;

    @Override
    public Order getByAcquirerId(String acquirerId) {
        LambdaQueryWrapper<Order> lqw = Wrappers.lambdaQuery();
        lqw.eq(Order::getAcquirerId, acquirerId);
        List<Order> orderList = list(lqw);
        if (!orderList.isEmpty())
            return orderList.get(0);
        return null;
    }

    @Override
    public Order getOrderBySsoOrderId(String ssoOrderId) {
        LambdaQueryWrapper<Order> lqw = Wrappers.lambdaQuery();
        lqw.eq(Order::getSsoOrderId, ssoOrderId);
        List<Order> orderList = list(lqw);
        if (!orderList.isEmpty())
            return orderList.get(0);
        return null;
    }

    @Override
    public Order getOrderByRelatedTransactionId(String relatedTransactionId) {
        LambdaQueryWrapper<Order> lqw = Wrappers.lambdaQuery();
        lqw.eq(Order::getRelatedTransactionId, relatedTransactionId);
        List<Order> orderList = list(lqw);
        if (!orderList.isEmpty())
            return orderList.get(0);
        return null;
    }

    @Override
    public Order getOrderByMerchantOrderId(String ssoOrderId, String merchantId) {
        LambdaQueryWrapper<Order> lqw = Wrappers.lambdaQuery();
        lqw.eq(Order::getSsoOrderId, ssoOrderId);
        lqw.eq(Order::getMerchantId, merchantId);
        List<Order> orderList = list(lqw);
        if (!orderList.isEmpty())
            return orderList.get(0);
        return null;
    }

    @Override
    public boolean updateOrderState(Long orderNo, String superOrderId, OrderStatus state) {
        LambdaUpdateWrapper<Order> lqw = buildBaseUpdateWrapper(orderNo, superOrderId, state);
        return executeUpdate(orderNo, state, lqw);
    }

    @Override
    public boolean updateOrderStateAndTransactionID(Long orderNo, String superOrderId, OrderStatus state,
            String transactionId) {
        LambdaUpdateWrapper<Order> lqw = buildBaseUpdateWrapper(orderNo, superOrderId, state);
        lqw.set(Order::getTransactionId, transactionId);
        return executeUpdate(orderNo, state, lqw);
    }

    private LambdaUpdateWrapper<Order> buildBaseUpdateWrapper(Long orderNo, String superOrderId, OrderStatus state) {
        LambdaUpdateWrapper<Order> lqw = Wrappers.lambdaUpdate();
        lqw.eq(Order::getId, orderNo);
        if (StrUtil.isNotBlank(superOrderId)) {
            lqw.set(Order::getOrderUuid, superOrderId);
        }
        lqw.set(Order::getUpdateAt, new Date());
        if (OrderStatus.APPROVED == state) {
            lqw.in(Order::getStatus, OrderStatus.WAITING_FOR_DEPOSIT.getCode(), OrderStatus.DEPOSIT_DECLINED.getCode());
            lqw.set(Order::getPayTime, new Date());
        }
        lqw.set(Order::getStatus, state.getCode());
        return lqw;
    }

    private boolean executeUpdate(Long orderNo, OrderStatus state, LambdaUpdateWrapper<Order> lqw) {
        boolean isOk = this.update(lqw);
        log.info("更新订单状态:{}，新订单状态为:{}, 是否成功:{}", orderNo, state.name(), isOk);
        return isOk;
    }

    @Override
    public boolean updateOrderExcludeStatus(Order order, String excludeStatus) {
        // 第一步：更新除状态外的其他字段
        LambdaUpdateWrapper<Order> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(Order::getId, order.getId());

        // 设置要更新的字段（除了状态字段）
        if (order.getCustomerId() != null) {
            updateWrapper.set(Order::getCustomerId, order.getCustomerId());
        }
        if (order.getUpdateAt() != null) {
            updateWrapper.set(Order::getUpdateAt, order.getUpdateAt());
        }
        if (order.getFailReasons() != null) {
            updateWrapper.set(Order::getFailReasons, order.getFailReasons());
        }
        if (order.getPayInfo() != null) {
            updateWrapper.set(Order::getPayInfo, order.getPayInfo());
        }
        if (order.getPaymentIp() != null) {
            updateWrapper.set(Order::getPaymentIp, order.getPaymentIp());
        }
        if (order.getPayTime() != null) {
            updateWrapper.set(Order::getPayTime, order.getPayTime());
        }
        if (order.getPaymentType() != null) {
            updateWrapper.set(Order::getPaymentType, order.getPaymentType());
        }
        if (StrUtil.isNotBlank(order.getOrderUuid())) {
            updateWrapper.set(Order::getOrderUuid, order.getOrderUuid());
        }
        if (StrUtil.isNotBlank(order.getTransactionId())) {
            updateWrapper.set(Order::getTransactionId, order.getTransactionId());
        }
        if (StrUtil.isNotBlank(order.getRelatedTransactionId())) {
            updateWrapper.set(Order::getRelatedTransactionId, order.getRelatedTransactionId());
        }
        if (StrUtil.isNotBlank(order.getUserPaymentOptionId())) {
            updateWrapper.set(Order::getUserPaymentOptionId, order.getUserPaymentOptionId());
        }
        if (StrUtil.isNotBlank(order.getAcquirerId())) {
            updateWrapper.set(Order::getAcquirerId, order.getAcquirerId());
        }

        boolean update = this.update(updateWrapper);

        // 第二步：条件性地更新状态字段（只有当前状态不是excludeStatus时才更新）
        if (order.getStatus() != null) {
            LambdaUpdateWrapper<Order> statusUpdateWrapper = Wrappers.lambdaUpdate();
            statusUpdateWrapper.eq(Order::getId, order.getId());
            statusUpdateWrapper.ne(Order::getStatus, excludeStatus); // 当前状态不等于excludeStatus时才更新状态
            statusUpdateWrapper.set(Order::getStatus, order.getStatus());

            boolean statusUpdate = this.update(statusUpdateWrapper);
            update = update || statusUpdate; // 只要有一个更新成功就算成功
            log.info("更新订单:{}, 更新后状态:{}, 是否成功:{}", order.getId(), order.getStatus(), update);
        }
        return update;
    }

    /**
     * 查询订单列表
     *
     * @param order 订单
     * @return 订单
     */
    @Override
    public List<Order> selectAllList(Order order) {
        return getBaseMapper().selectAllList(order);
    }

    @Override
    public List<Order> exportQueryAllOrder(Order order) {
        return getBaseMapper().exportQueryAllOrder(order);
    }

    /**
     * 导出订单数据到Excel
     */
    @Override
    public AjaxResult exportOrderToExcel(Order order, String lang, String exportFields) {
        ExcelUtil<Order> util = new ExcelUtil<>(Order.class);
        final ExportProgressTracker progressTracker = ExportProgressTracker.create(20_000);

        // 处理导出字段参数
        if (exportFields != null && !exportFields.trim().isEmpty()) {
            Set<String> fieldSet = Arrays.stream(exportFields.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            util.setExportFields(fieldSet);
            log.info("导出订单数据，指定字段: {}", fieldSet);
        } else {
            log.info("导出订单数据，导出所有字段");
        }

        long exportStartTime = System.currentTimeMillis();
        log.info("开始导出订单数据，批次大小: 2000，线程数: 4");

        try {
            // 使用多线程导出，批次大小2000，4个线程
            AjaxResult result = util.exportLargeData("order", lang, 2000, 4, (pageNum, pageSize) -> {
                try {
                    PageHelper.startPage(pageNum, pageSize, false);
                    List<Order> orders = exportQueryAllOrder(order);

                    // 批量填充支付信息（卡号等）
                    batchPopulatePayInfo(orders, true);
                    // 批量填充导出所需的额外字段（钱包地址、支付机构、退款金额、邮箱等）
                    batchPopulateExportFieldsInternal(orders, progressTracker);

                    return orders;
                } finally {
                    PageHelper.clearPage();
                }
            });

            long exportTotalTime = System.currentTimeMillis() - exportStartTime;
            log.info("订单数据导出完成，总耗时：{} ms ({} 秒)", exportTotalTime, exportTotalTime / 1000.0);

            return result;
        } catch (Exception e) {
            long exportTotalTime = System.currentTimeMillis() - exportStartTime;
            log.error("导出订单数据异常，已耗时：{} ms", exportTotalTime, e);
            throw new CustomException("导出订单数据失败");
        } finally {
            long totalElapsed = System.currentTimeMillis() - exportStartTime;
            progressTracker.finish(totalElapsed);
        }
    }

    @Override
    public List<Order> queryList(Order order) {
        LambdaQueryWrapper<Order> lqw = Wrappers.lambdaQuery();
        if (order.getMerchantId() != null) {
            lqw.eq(Order::getMerchantId, order.getMerchantId());
        }
        if (order.getCustomerId() != null) {
            lqw.eq(Order::getCustomerId, order.getCustomerId());
        }
        if (order.getCustomerWalletId() != null) {
            lqw.eq(Order::getCustomerWalletId, order.getCustomerWalletId());
        }
        if (order.getType() != null) {
            lqw.eq(Order::getType, order.getType());
        }
        if (order.getCurrencyId() != null) {
            lqw.eq(Order::getCurrencyId, order.getCurrencyId());
        }
        if (order.getFiatAmount() != null) {
            lqw.eq(Order::getFiatAmount, order.getFiatAmount());
        }
        if (order.getCryptoId() != null) {
            lqw.eq(Order::getCryptoId, order.getCryptoId());
        }
        if (order.getCryptoAmount() != null) {
            lqw.eq(Order::getCryptoAmount, order.getCryptoAmount());
        }
        if (order.getProcessingFee() != null) {
            lqw.eq(Order::getProcessingFee, order.getProcessingFee());
        }
        if (order.getLiquidityQuote() != null) {
            lqw.eq(Order::getLiquidityQuote, order.getLiquidityQuote());
        }
        if (StringUtils.isNotBlank(order.getLiquidityProvider())) {
            lqw.eq(Order::getLiquidityProvider, order.getLiquidityProvider());
        }
        if (StringUtils.isNotBlank(order.getOrderUuid())) {
            lqw.eq(Order::getOrderUuid, order.getOrderUuid());
        }
        if (StringUtils.isNotBlank(order.getReference())) {
            lqw.eq(Order::getReference, order.getReference());
        }
        if (order.getTravelRuleStatus() != null) {
            lqw.eq(Order::getTravelRuleStatus, order.getTravelRuleStatus());
        }
        if (order.getStatus() != null) {
            lqw.eq(Order::getStatus, order.getStatus());
        }
        if (StringUtils.isNotBlank(order.getAmlId())) {
            lqw.eq(Order::getAmlId, order.getAmlId());
        }
        if (StringUtils.isNotBlank(order.getAmlPayload())) {
            lqw.eq(Order::getAmlPayload, order.getAmlPayload());
        }
        // if (StringUtils.isNotBlank(order.getAmlScore())){
        // lqw.eq(Order::getAmlScore ,order.getAmlScore());
        // }
        if (order.getAmlStatus() != null) {
            lqw.eq(Order::getAmlStatus, order.getAmlStatus());
        }
        if (order.getCreatedAt() != null) {
            lqw.eq(Order::getCreatedAt, order.getCreatedAt());
        }
        if (order.getCreatedBy() != null) {
            lqw.eq(Order::getCreatedBy, order.getCreatedBy());
        }
        if (order.getDeletedAt() != null) {
            lqw.eq(Order::getDeletedAt, order.getDeletedAt());
        }
        if (order.getDeletedBy() != null) {
            lqw.eq(Order::getDeletedBy, order.getDeletedBy());
        }
        if (order.getDeleted() != null) {
            lqw.eq(Order::getDeleted, order.getDeleted());
        }
        if (StringUtils.isNotBlank(order.getTravelRuleId())) {
            lqw.eq(Order::getTravelRuleId, order.getTravelRuleId());
        }
        if (StringUtils.isNotBlank(order.getPaymentIp())) {
            lqw.eq(Order::getPaymentIp, order.getPaymentIp());
        }
        if (StringUtils.isNotBlank(order.getMid())) {
            lqw.eq(Order::getMid, order.getMid());
        }
        if (order.getIsOtc() != null) {
            lqw.eq(Order::getIsOtc, order.getIsOtc());
        }
        if (StringUtils.isNotBlank(order.getSsoOrderId())) {
            lqw.eq(Order::getSsoOrderId, order.getSsoOrderId());
        }
        if (order.getIncludeFee() != null) {
            lqw.eq(Order::getIncludeFee, order.getIncludeFee());
        }
        if (order.getSum90Checked() != null) {
            lqw.eq(Order::getSum90Checked, order.getSum90Checked());
        }
        if (StringUtils.isNotBlank(order.getFraudScore())) {
            lqw.eq(Order::getFraudScore, order.getFraudScore());
        }
        if (order.getGatewayFee() != null) {
            lqw.eq(Order::getGatewayFee, order.getGatewayFee());
        }
        if (StringUtils.isNotBlank(order.getReferralCode())) {
            lqw.eq(Order::getReferralCode, order.getReferralCode());
        }
        if (order.getBankAccountId() != null) {
            lqw.eq(Order::getBankAccountId, order.getBankAccountId());
        }
        if (StringUtils.isNotBlank(order.getAniCode())) {
            lqw.eq(Order::getAniCode, order.getAniCode());
        }
        if (order.getIsRollback() != null) {
            lqw.eq(Order::getIsRollback, order.getIsRollback());
        }
        if (order.getPaymentType() != null) {
            lqw.eq(Order::getPaymentType, order.getPaymentType());
        }
        if (order.getPayParams() != null) {
            lqw.eq(Order::getPayParams, order.getPayParams());
        }
        return this.list(lqw);
    }

    @Override
    public List<Order> getNeedCallBackOrders(List<Long> shopIds, int maxCallbackTimes) {
        LambdaQueryWrapper<Order> lqw = Wrappers.lambdaQuery();
        lqw.in(Order::getMerchantId, shopIds);
        lqw.in(Order::getCallbackStatus, TradeCallBackStatus.NEW.name(), TradeCallBackStatus.FAIL.name());
        lqw.lt(Order::getCallbackNum, maxCallbackTimes);
        lqw.isNotNull(Order::getPayTime);
        lqw.orderByDesc(Order::getUpdateAt);
        lqw.orderByAsc(Order::getCallbackNum);
        PageHelper.startPage(1, 2);
        List<Order> tradeList = list(lqw);
        return tradeList;
    }

    @Override
    public OrderWithFeeModelDto getOrderWithFeeModel(Long orderId) {
        return orderMapper.getOrderWithFeeModel(orderId);
    }

    @Override
    public void settlementOrder(long orderId, BigDecimal orderAmount, int ordinal) {
        orderMapper.settlementOrder(orderId, orderAmount, ordinal);
    }

    @Override
    public IndexOrderStatisticsVo orderStatistics(Long shopId) {
        return orderStatistics(shopId, null);
    }

    @Override
    public IndexOrderStatisticsVo orderStatistics(Long shopId, String timezone) {
        // 获取今日日期（使用指定时区或默认北京时间）
        String startDatetime = DateUtils.getDate() + " 00:00:00";
        String endDatetime = DateUtils.getDate() + " 23:59:59";
        log.info("今日统计查询: startDatetime={}, endDatetime={}, shopId={}, timezone={}", startDatetime, endDatetime, shopId,
                timezone);

        // 成功数量
        int orderNum = orderMapper.getOrderNumber(startDatetime, endDatetime, shopId, OrderStatus.getSuccessStatus());
        // 成交总额
        BigDecimal transferAmount = orderMapper.getOrderAmount(startDatetime, endDatetime, shopId);

        // 昨日
        String yesterdayStart = DateUtils.dateTime(DateUtils.getYesterdayDate(true)) + " 00:00:00";
        String yesterdayEnd = DateUtils.dateTime(DateUtils.getYesterdayDate(true)) + " 23:59:59";
        BigDecimal yesterdayAmount = orderMapper.getOrderAmount(yesterdayStart, yesterdayEnd, shopId);

        // 最近7天
        Date last7Days = DateUtils.getDateAfter(new Date(), -6, Calendar.DAY_OF_MONTH);
        String last7Start = DateUtils.dateTime(last7Days) + " 00:00:00";
        BigDecimal last7DaysAmount = orderMapper.getOrderAmount(last7Start, endDatetime, shopId);

        IndexOrderStatisticsVo vo = new IndexOrderStatisticsVo();
        vo.setTodayOrderCount(orderNum);
        vo.setTodayTotalAmount(transferAmount);
        vo.setYesterdayTotalAmount(yesterdayAmount);
        vo.setLast7DaysTotalAmount(last7DaysAmount);

        log.info("今日统计结果: orderNum={}, transferAmount={}", orderNum, transferAmount);
        return vo;
    }

    @Override
    public IndexShopRankStatisticsVo shopRankStatistics(IndexShopRankStatisticsRequest request) {
        List<ShopOrderNumVo> shopOrderNum = orderMapper.getShopOrderNum(request);

        List<ShopOrderAmountVo> shopAmount = orderMapper.getShopOrderAmount(request);
        IndexShopRankStatisticsVo vo = new IndexShopRankStatisticsVo();
        vo.setTransactionCountRanking(shopOrderNum);
        vo.setTransactionAmountRanking(shopAmount);
        return vo;
    }

    @Override
    public IndexOrderNumCurveVo orderNumCurve(String startDate, String endDate, Long shopId) {
        return orderNumCurve(startDate, endDate, shopId, null);
    }

    @Override
    public IndexOrderNumCurveVo orderNumCurve(String startDate, String endDate, Long shopId, String timezone) {
        // 只统计成功的

        // 1.本月和上月订单数量
        String startOfMonth = DateUtils.dateTime(DateUtils.getStartOfMonth()) + " 00:00:00";
        String endOfMonth = DateUtils.dateTime(DateUtils.getEndOfMonth()) + " 23:59:59";
        String startOfLastMonth = DateUtils.dateTime(DateUtils.getStartOfLastMonth()) + " 00:00:00";
        String endOfLastMonth = DateUtils.dateTime(DateUtils.getEndOfLastMonth()) + " 23:59:59";

        // 批量查询本月和上月订单数量
        List<Map<String, Object>> monthRanges = new ArrayList<>();
        Map<String, Object> thisMonthRange = new HashMap<>();
        thisMonthRange.put("startDatetime", startOfMonth);
        thisMonthRange.put("endDatetime", endOfMonth);
        monthRanges.add(thisMonthRange);

        Map<String, Object> lastMonthRange = new HashMap<>();
        lastMonthRange.put("startDatetime", startOfLastMonth);
        lastMonthRange.put("endDatetime", endOfLastMonth);
        monthRanges.add(lastMonthRange);

        List<Integer> monthOrderNumbers = orderMapper.batchGetOrderNumber(monthRanges, shopId,
                OrderStatus.getSuccessStatus());
        int thisMonthOrderNumber = monthOrderNumbers.get(0);
        int lastMonthOrderNumber = monthOrderNumbers.get(1);

        // 2.本周和上周订单数量
        String startOfWeek = DateUtils.dateTime(DateUtils.getStartOfWeek()) + " 00:00:00";
        String endOfWeek = DateUtils.dateTime(DateUtils.getEndOfWeek()) + " 23:59:59";
        String startOfLastWeek = DateUtils.dateTime(DateUtils.getStartOfLastWeek()) + " 00:00:00";
        String endOfLastWeek = DateUtils.dateTime(DateUtils.getEndOfLastWeek()) + " 23:59:59";

        // 批量查询本周和上周订单数量
        List<Map<String, Object>> weekRanges = new ArrayList<>();
        Map<String, Object> thisWeekRange = new HashMap<>();
        thisWeekRange.put("startDatetime", startOfWeek);
        thisWeekRange.put("endDatetime", endOfWeek);
        weekRanges.add(thisWeekRange);

        Map<String, Object> lastWeekRange = new HashMap<>();
        lastWeekRange.put("startDatetime", startOfLastWeek);
        lastWeekRange.put("endDatetime", endOfLastWeek);
        weekRanges.add(lastWeekRange);

        List<Integer> weekOrderNumbers = orderMapper.batchGetOrderNumber(weekRanges, shopId,
                OrderStatus.getSuccessStatus());
        int thisWeekOrderNumber = weekOrderNumbers.get(0);
        int lastWeekOrderNumber = weekOrderNumbers.get(1);

        // 计算月环比
        BigDecimal monthlyTrend = BigDecimal.ZERO;
        if (lastMonthOrderNumber != 0) {
            monthlyTrend = new BigDecimal(thisMonthOrderNumber - lastMonthOrderNumber)
                    .multiply(new BigDecimal(100))
                    .divide(new BigDecimal(lastMonthOrderNumber), 2, RoundingMode.HALF_UP);
        }

        // 计算周环比
        BigDecimal weeklyTrend = BigDecimal.ZERO;
        if (lastWeekOrderNumber != 0) {
            weeklyTrend = new BigDecimal(thisWeekOrderNumber - lastWeekOrderNumber)
                    .multiply(new BigDecimal(100))
                    .divide(new BigDecimal(lastWeekOrderNumber), 2, RoundingMode.HALF_UP);
        }

        IndexOrderNumTrendVo monthly = new IndexOrderNumTrendVo();
        monthly.setOrderCount(thisMonthOrderNumber);
        monthly.setTrend(monthlyTrend);

        IndexOrderNumTrendVo weekly = new IndexOrderNumTrendVo();
        weekly.setOrderCount(thisWeekOrderNumber);
        weekly.setTrend(weeklyTrend);

        IndexOrderNumCurveVo result = new IndexOrderNumCurveVo();
        result.setMonthly(monthly);
        result.setWeekly(weekly);

        // 查询状态趋势数据
        List<OrderStatusStatistics> statusStats = orderMapper.getOrderStatusStatistics(startDate, endDate, shopId);

        // 构造状态趋势数据
        StatusTrend statusTrend = buildStatusTrend(statusStats, startDate, endDate, shopId);
        result.setStatusTrend(statusTrend);
        return result;
    }

    /**
     * 构造状态趋势数据
     *
     * @param statusStats 状态统计数据
     * @param startDate   开始日期
     * @param endDate     结束日期
     * @return StatusTrend对象
     */
    private StatusTrend buildStatusTrend(List<OrderStatusStatistics> statusStats, String startDate, String endDate,
            Long shopId) {
        StatusTrend statusTrend = new StatusTrend();

        // 生成日期列表
        List<String> dbDates = generateDateList(startDate, endDate);
        statusTrend.setDates(dbDates.stream().map(date -> {
            return date.substring(5); // 转换为 MM-dd 格式用于前端显示
        }).collect(Collectors.toList()));

        // 初始化各状态的列表
        List<Integer> completed = new ArrayList<>(Collections.nCopies(dbDates.size(), 0));
        List<Integer> cancelled = new ArrayList<>(Collections.nCopies(dbDates.size(), 0));
        List<Integer> processing = new ArrayList<>(Collections.nCopies(dbDates.size(), 0));
        List<Integer> failed = new ArrayList<>(Collections.nCopies(dbDates.size(), 0));
        List<Integer> refunded = new ArrayList<>(Collections.nCopies(dbDates.size(), 0));
        List<Integer> chargeback = new ArrayList<>(Collections.nCopies(dbDates.size(), 0));
        List<Integer> chargebackCancelled = new ArrayList<>(Collections.nCopies(dbDates.size(), 0));
        List<Integer> dispute = new ArrayList<>(Collections.nCopies(dbDates.size(), 0));

        // 填充统计数据
        Map<String, Integer> dateIndexMap = new HashMap<>();
        for (int i = 0; i < dbDates.size(); i++) {
            dateIndexMap.put(dbDates.get(i), i);
        }

        for (OrderStatusStatistics stat : statusStats) {
            String date = stat.getDate();
            String status = stat.getStatus();
            Integer count = stat.getCount();

            Integer index = dateIndexMap.get(date);
            if (index != null) {
                // 根据状态码填充对应的列表
                // 这里需要根据实际的状态码定义来调整
                if (Objects.equals(status, OrderStatus.COMPLETED.getCode())) {
                    completed.set(index, count);
                } else if (Objects.equals(status, OrderStatus.CANCELED.getCode())) {
                    cancelled.set(index, count);
                } else if (Objects.equals(status, OrderStatus.WAITING_FOR_DEPOSIT.getCode()) ||
                        Objects.equals(status, OrderStatus.APPROVED.getCode()) ||
                        Objects.equals(status, OrderStatus.TDS_PENDING.getCode())) {
                    processing.set(index, count);
                } else if (Objects.equals(status, OrderStatus.DEPOSIT_DECLINED.getCode())) {
                    failed.set(index, count);
                }
            }
        }

        // 退款数量改为从 refund_order 表统计
        Map<String, Integer> refundMap = new HashMap<>(
                refundOrderService.countRefundOrdersByDate(shopId, startDate, endDate));
        for (Map.Entry<String, Integer> entry : refundMap.entrySet()) {
            Integer index = dateIndexMap.get(entry.getKey());
            if (index != null) {
                refunded.set(index, entry.getValue());
            }
        }

        // 一次性查询 movement_data 的其它类型（拒付、拒付取消、争议）数量
        List<String> movementTypes = Arrays.asList(
                NuvieOrderStatus.CB.getCode(),
                NuvieOrderStatus.CB_CANCELLED.getCode(),
                NuvieOrderStatus.RDR.getCode());
        List<OrderStatusStatistics> allMovementStats = movementDataMapper.countByDateAndType(
                startDate + " 00:00:00", endDate + " 23:59:59", movementTypes, shopId);

        for (OrderStatusStatistics stat : allMovementStats) {
            String date = stat.getDate();
            String type = stat.getType();
            Integer count = stat.getCount();

            Integer index = dateIndexMap.get(date);
            if (index != null) {
                if (Objects.equals(type, NuvieOrderStatus.CB.getCode())) {
                    chargeback.set(index, count);
                } else if (Objects.equals(type, NuvieOrderStatus.CB_CANCELLED.getCode())) {
                    chargebackCancelled.set(index, count);
                } else if (Objects.equals(type, NuvieOrderStatus.RDR.getCode())) {
                    dispute.set(index, count);
                }
            }
        }

        statusTrend.setCompleted(completed);
        statusTrend.setCancelled(cancelled);
        statusTrend.setProcessing(processing);
        statusTrend.setFailed(failed);
        statusTrend.setRefunded(refunded);
        statusTrend.setChargeback(chargeback);
        statusTrend.setChargebackCancelled(chargebackCancelled);
        statusTrend.setDispute(dispute);

        return statusTrend;
    }

    /**
     * 生成日期列表
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 日期列表
     */
    private List<String> generateDateList(String startDate, String endDate) {
        List<String> dates = new ArrayList<>();
        LocalDate start = LocalDate.parse(startDate.substring(0, 10));
        LocalDate end = LocalDate.parse(endDate.substring(0, 10));

        LocalDate current = start;
        while (!current.isAfter(end)) {
            dates.add(current.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            current = current.plusDays(1);
        }

        return dates;
    }

    /**
     * 填充状态数据到对应的列表中
     *
     * @param stats        统计数据
     * @param targetList   目标列表
     * @param dateIndexMap 日期索引映射
     */
    private void fillStatusData(List<OrderStatusStatistics> stats, List<Integer> targetList,
            Map<String, Integer> dateIndexMap) {
        for (OrderStatusStatistics stat : stats) {
            String date = stat.getDate();
            Integer count = stat.getCount();
            Integer index = dateIndexMap.get(date);
            if (index != null) {
                targetList.set(index, count);
            }
        }
    }

    @Override
    public IndexOrderAmountCurveVo orderAmountCurve(String startDate, String endDate, Long shopId) {
        // 1.本月订单金额和上月订单金额
        String startOfMonth = DateUtils.dateTime(DateUtils.getStartOfMonth()) + " 00:00:00";
        String endOfMonth = DateUtils.dateTime(DateUtils.getEndOfMonth()) + " 23:59:59";
        String startOfLastMonth = DateUtils.dateTime(DateUtils.getStartOfLastMonth()) + " 00:00:00";
        String endOfLastMonth = DateUtils.dateTime(DateUtils.getEndOfLastMonth()) + " 23:59:59";

        // 批量查询本月和上月订单金额
        List<Map<String, Object>> monthRanges = new ArrayList<>();
        Map<String, Object> thisMonthRange = new HashMap<>();
        thisMonthRange.put("startDatetime", startOfMonth);
        thisMonthRange.put("endDatetime", endOfMonth);
        monthRanges.add(thisMonthRange);

        Map<String, Object> lastMonthRange = new HashMap<>();
        lastMonthRange.put("startDatetime", startOfLastMonth);
        lastMonthRange.put("endDatetime", endOfLastMonth);
        monthRanges.add(lastMonthRange);

        List<BigDecimal> monthAmounts = orderMapper.batchGetOrderAmount(monthRanges, shopId);
        BigDecimal thisMonthOrderAmount = monthAmounts.get(0);
        BigDecimal lastMonthOrderAmount = monthAmounts.get(1);

        // 2.本周订单金额和上周订单金额
        String startOfWeek = DateUtils.dateTime(DateUtils.getStartOfWeek()) + " 00:00:00";
        String endOfWeek = DateUtils.dateTime(DateUtils.getEndOfWeek()) + " 23:59:59";
        String startOfLastWeek = DateUtils.dateTime(DateUtils.getStartOfLastWeek()) + " 00:00:00";
        String endOfLastWeek = DateUtils.dateTime(DateUtils.getEndOfLastWeek()) + " 23:59:59";

        // 批量查询本周和上周订单金额
        List<Map<String, Object>> weekRanges = new ArrayList<>();
        Map<String, Object> thisWeekRange = new HashMap<>();
        thisWeekRange.put("startDatetime", startOfWeek);
        thisWeekRange.put("endDatetime", endOfWeek);
        weekRanges.add(thisWeekRange);

        Map<String, Object> lastWeekRange = new HashMap<>();
        lastWeekRange.put("startDatetime", startOfLastWeek);
        lastWeekRange.put("endDatetime", endOfLastWeek);
        weekRanges.add(lastWeekRange);

        List<BigDecimal> weekAmounts = orderMapper.batchGetOrderAmount(weekRanges, shopId);
        BigDecimal thisWeekOrderAmount = weekAmounts.get(0);
        BigDecimal lastWeekOrderAmount = weekAmounts.get(1);

        // 计算金额月环比
        BigDecimal monthlyAmountTrend = BigDecimal.ZERO;
        if (lastMonthOrderAmount != null && lastMonthOrderAmount.compareTo(BigDecimal.ZERO) != 0) {
            monthlyAmountTrend = thisMonthOrderAmount.subtract(lastMonthOrderAmount)
                    .multiply(new BigDecimal(100))
                    .divide(lastMonthOrderAmount, 2, RoundingMode.HALF_UP);
        }

        // 计算金额周环比
        BigDecimal weeklyAmountTrend = BigDecimal.ZERO;
        if (lastWeekOrderAmount != null && lastWeekOrderAmount.compareTo(BigDecimal.ZERO) != 0) {
            weeklyAmountTrend = thisWeekOrderAmount.subtract(lastWeekOrderAmount)
                    .multiply(new BigDecimal(100))
                    .divide(lastWeekOrderAmount, 2, RoundingMode.HALF_UP);
        }

        IndexOrderAmountTrendVo monthlyAmount = new IndexOrderAmountTrendVo();
        monthlyAmount.setOrderAmount(thisMonthOrderAmount);
        monthlyAmount.setTrend(monthlyAmountTrend);

        IndexOrderAmountTrendVo weeklyAmount = new IndexOrderAmountTrendVo();
        weeklyAmount.setOrderAmount(thisWeekOrderAmount);
        weeklyAmount.setTrend(weeklyAmountTrend);

        IndexOrderAmountCurveVo result = new IndexOrderAmountCurveVo();
        result.setMonthly(monthlyAmount);
        result.setWeekly(weeklyAmount);

        // 查询金额趋势数据
        List<OrderAmountStatistics> amountStats = orderMapper.getOrderAmountStatistics(startDate, endDate, shopId);

        // 构造金额趋势数据
        AmountTrend amountTrend = buildAmountTrend(amountStats, startDate, endDate, shopId);
        result.setAmountTrend(amountTrend);

        return result;
    }

    /**
     * 构造金额趋势数据
     */
    private AmountTrend buildAmountTrend(List<OrderAmountStatistics> amountStats, String startDate, String endDate,
            Long shopId) {
        AmountTrend amountTrend = new AmountTrend();

        // 生成日期列表
        List<String> dbDates = generateDateList(startDate, endDate);
        amountTrend.setDates(dbDates.stream().map(date -> {
            return date.substring(5); // 转换为 MM-dd 格式用于前端显示
        }).collect(Collectors.toList()));

        // 初始化各状态的列表
        List<BigDecimal> completed = new ArrayList<>(Collections.nCopies(dbDates.size(), BigDecimal.ZERO));
        List<BigDecimal> cancelled = new ArrayList<>(Collections.nCopies(dbDates.size(), BigDecimal.ZERO));
        List<BigDecimal> processing = new ArrayList<>(Collections.nCopies(dbDates.size(), BigDecimal.ZERO));
        List<BigDecimal> failed = new ArrayList<>(Collections.nCopies(dbDates.size(), BigDecimal.ZERO));
        List<BigDecimal> refunded = new ArrayList<>(Collections.nCopies(dbDates.size(), BigDecimal.ZERO));
        List<BigDecimal> chargeback = new ArrayList<>(Collections.nCopies(dbDates.size(), BigDecimal.ZERO));
        List<BigDecimal> chargebackCancelled = new ArrayList<>(Collections.nCopies(dbDates.size(), BigDecimal.ZERO));
        List<BigDecimal> dispute = new ArrayList<>(Collections.nCopies(dbDates.size(), BigDecimal.ZERO));
        // 填充订单金额统计数据（已完成和已取消）
        Map<String, Integer> dateIndexMap = new HashMap<>();
        for (int i = 0; i < dbDates.size(); i++) {
            dateIndexMap.put(dbDates.get(i), i);
        }

        for (OrderAmountStatistics stat : amountStats) {
            if (stat == null) {
                continue;
            }
            String date = stat.getDate();
            String status = stat.getStatus();
            BigDecimal amount = stat.getAmount() != null ? stat.getAmount() : BigDecimal.ZERO;

            Integer index = dateIndexMap.get(date);
            if (index != null) {
                // 根据状态码填充对应的列表
                if (Objects.equals(status, OrderStatus.COMPLETED.getCode())) {
                    completed.set(index, amount);
                } else if (Objects.equals(status, OrderStatus.CANCELED.getCode())) {
                    cancelled.set(index, amount);
                } else if (Objects.equals(status, OrderStatus.WAITING_FOR_DEPOSIT.getCode()) ||
                        Objects.equals(status, OrderStatus.APPROVED.getCode()) ||
                        Objects.equals(status, OrderStatus.TDS_PENDING.getCode())) {
                    processing.set(index, amount);
                } else if (Objects.equals(status, OrderStatus.DEPOSIT_DECLINED.getCode())) {
                    failed.set(index, amount);
                }
            }
        }

        // 退款金额改为从 refund_order 表统计（每日 sum(refund_amount)）
        Map<String, BigDecimal> refundAmountMap = refundOrderService.sumRefundAmountByDate(shopId, startDate, endDate);
        for (Map.Entry<String, BigDecimal> entry : refundAmountMap.entrySet()) {
            Integer index = dateIndexMap.get(entry.getKey());
            if (index != null) {
                refunded.set(index, entry.getValue() == null ? BigDecimal.ZERO : entry.getValue());
            }
        }

        // movement_data 仅统计拒付、拒付取消、争议金额
        List<String> movementTypes = Arrays.asList(
                NuvieOrderStatus.CB.getCode(),
                NuvieOrderStatus.CB_CANCELLED.getCode(),
                NuvieOrderStatus.RDR.getCode());
        List<OrderAmountStatistics> allMovementAmountStats = movementDataMapper.getAmountByDateAndType(
                startDate, endDate, movementTypes, shopId);
        fillAmountData(allMovementAmountStats, refunded, chargeback, chargebackCancelled, dispute, dateIndexMap);

        amountTrend.setCompleted(completed);
        amountTrend.setCancelled(cancelled);
        amountTrend.setProcessing(processing);
        amountTrend.setFailed(failed);
        amountTrend.setRefunded(refunded);
        amountTrend.setChargeback(chargeback);
        amountTrend.setChargebackCancelled(chargebackCancelled);
        amountTrend.setDispute(dispute);

        return amountTrend;
    }

    /**
     * 填充金额数据到对应的列表中
     */
    private void fillAmountData(List<OrderAmountStatistics> stats, List<BigDecimal> refunded,
            List<BigDecimal> chargeback,
            List<BigDecimal> chargebackCancelled, List<BigDecimal> dispute, Map<String, Integer> dateIndexMap) {
        for (OrderAmountStatistics stat : stats) {
            if (stat == null) {
                continue;
            }
            String date = stat.getDate();
            String type = stat.getType(); // 注意这里使用type而不是status
            BigDecimal amount = stat.getAmount() != null ? stat.getAmount() : BigDecimal.ZERO;
            Integer index = dateIndexMap.get(date);
            if (index != null) {
                if (Objects.equals(type, NuvieOrderStatus.Refund.getCode())) {
                    refunded.set(index, amount);
                } else if (Objects.equals(type, NuvieOrderStatus.CB.getCode())) {
                    chargeback.set(index, amount);
                } else if (Objects.equals(type, NuvieOrderStatus.CB_CANCELLED.getCode())) {
                    chargebackCancelled.set(index, amount);
                } else if (Objects.equals(type, NuvieOrderStatus.RDR.getCode())) {
                    dispute.set(index, amount);
                }
            }
        }
    }

    @Override
    public IndexOrderAvgAmountCurveVo orderAvgAmountCurve(String startDate, String endDate, Long shopId) {
        IndexOrderAvgAmountCurveVo result = new IndexOrderAvgAmountCurveVo();

        // 1.本月和上月平均价格
        String startOfMonth = DateUtils.dateTime(DateUtils.getStartOfMonth()) + " 00:00:00";
        String endOfMonth = DateUtils.dateTime(DateUtils.getEndOfMonth()) + " 23:59:59";
        String startOfLastMonth = DateUtils.dateTime(DateUtils.getStartOfLastMonth()) + " 00:00:00";
        String endOfLastMonth = DateUtils.dateTime(DateUtils.getEndOfLastMonth()) + " 23:59:59";

        // 批量查询本月和上月平均价格
        List<Map<String, Object>> monthRanges = new ArrayList<>();
        Map<String, Object> thisMonthRange = new HashMap<>();
        thisMonthRange.put("startDatetime", startOfMonth);
        thisMonthRange.put("endDatetime", endOfMonth);
        monthRanges.add(thisMonthRange);

        Map<String, Object> lastMonthRange = new HashMap<>();
        lastMonthRange.put("startDatetime", startOfLastMonth);
        lastMonthRange.put("endDatetime", endOfLastMonth);
        monthRanges.add(lastMonthRange);

        List<BigDecimal> monthAvgPrices = orderMapper.batchGetOrderAvgAmount(monthRanges, shopId);
        BigDecimal thisMonthAvgPrice = monthAvgPrices.get(0);
        BigDecimal lastMonthAvgPrice = monthAvgPrices.get(1);

        // 2.本周和上周平均价格
        String startOfWeek = DateUtils.dateTime(DateUtils.getStartOfWeek()) + " 00:00:00";
        String endOfWeek = DateUtils.dateTime(DateUtils.getEndOfWeek()) + " 23:59:59";
        String startOfLastWeek = DateUtils.dateTime(DateUtils.getStartOfLastWeek()) + " 00:00:00";
        String endOfLastWeek = DateUtils.dateTime(DateUtils.getEndOfLastWeek()) + " 23:59:59";

        // 批量查询本周和上周平均价格
        List<Map<String, Object>> weekRanges = new ArrayList<>();
        Map<String, Object> thisWeekRange = new HashMap<>();
        thisWeekRange.put("startDatetime", startOfWeek);
        thisWeekRange.put("endDatetime", endOfWeek);
        weekRanges.add(thisWeekRange);

        Map<String, Object> lastWeekRange = new HashMap<>();
        lastWeekRange.put("startDatetime", startOfLastWeek);
        lastWeekRange.put("endDatetime", endOfLastWeek);
        weekRanges.add(lastWeekRange);

        List<BigDecimal> weekAvgPrices = orderMapper.batchGetOrderAvgAmount(weekRanges, shopId);
        BigDecimal thisWeekAvgPrice = weekAvgPrices.get(0);
        BigDecimal lastWeekAvgPrice = weekAvgPrices.get(1);

        // 计算月趋势
        BigDecimal monthlyTrend = BigDecimal.ZERO;
        if (lastMonthAvgPrice != null && lastMonthAvgPrice.compareTo(BigDecimal.ZERO) != 0) {
            monthlyTrend = thisMonthAvgPrice.subtract(lastMonthAvgPrice)
                    .multiply(new BigDecimal(100))
                    .divide(lastMonthAvgPrice, 2, RoundingMode.HALF_UP);
        }

        // 计算周趋势
        BigDecimal weeklyTrend = BigDecimal.ZERO;
        if (lastWeekAvgPrice != null && lastWeekAvgPrice.compareTo(BigDecimal.ZERO) != 0) {
            weeklyTrend = thisWeekAvgPrice.subtract(lastWeekAvgPrice)
                    .multiply(new BigDecimal(100))
                    .divide(lastWeekAvgPrice, 2, RoundingMode.HALF_UP);
        }

        IndexOrderAvgAmountTrendVo monthly = new IndexOrderAvgAmountTrendVo();
        monthly.setAvgPrice(thisMonthAvgPrice);
        monthly.setTrend(monthlyTrend);

        IndexOrderAvgAmountTrendVo weekly = new IndexOrderAvgAmountTrendVo();
        weekly.setAvgPrice(thisWeekAvgPrice);
        weekly.setTrend(weeklyTrend);

        result.setMonthly(monthly);
        result.setWeekly(weekly);

        // 查询价格趋势数据
        List<OrderAmountStatistics> avgAmountStats = orderMapper.getOrderAvgAmountStatistics(startDate, endDate,
                shopId);
        PriceTrend priceTrend = buildPriceTrend(avgAmountStats, startDate, endDate);
        result.setPriceTrend(priceTrend);

        return result;
    }

    /**
     * 构造价格趋势数据
     */
    private PriceTrend buildPriceTrend(List<OrderAmountStatistics> avgAmountStats, String startDate, String endDate) {
        PriceTrend priceTrend = new PriceTrend();

        // 生成日期列表
        List<String> dbDates = generateDateList(startDate, endDate);
        priceTrend.setDates(dbDates.stream().map(date -> {
            return date.substring(5); // 转换为 MM-dd 格式用于前端显示
        }).collect(Collectors.toList()));

        // 初始化价格列表
        List<BigDecimal> values = new ArrayList<>(Collections.nCopies(dbDates.size(), BigDecimal.ZERO));

        // 填充数据
        Map<String, Integer> dateIndexMap = new HashMap<>();
        for (int i = 0; i < dbDates.size(); i++) {
            dateIndexMap.put(dbDates.get(i), i);
        }

        for (OrderAmountStatistics stat : avgAmountStats) {
            String date = stat.getDate();
            BigDecimal avgAmount = stat.getAmount() != null ? stat.getAmount() : BigDecimal.ZERO;

            Integer index = dateIndexMap.get(date);
            if (index != null) {
                values.set(index, avgAmount);
            }
        }

        priceTrend.setValues(values);
        return priceTrend;
    }

    @Override
    public RatesTrend orderRateCurve(String startDate, String endDate, Long shopId) {
        // 生成日期列表
        List<String> dbDates = generateDateList(startDate, endDate);
        List<String> displayDates = dbDates.stream().map(date -> {
            return date.substring(5); // 转换为 MM-dd 格式用于前端显示
        }).collect(Collectors.toList());

        // 初始化比率列表
        List<BigDecimal> refundRate = new ArrayList<>(Collections.nCopies(dbDates.size(), BigDecimal.ZERO));
        List<BigDecimal> chargebackRate = new ArrayList<>(Collections.nCopies(dbDates.size(), BigDecimal.ZERO));
        List<BigDecimal> cancelRate = new ArrayList<>(Collections.nCopies(dbDates.size(), BigDecimal.ZERO));
        List<BigDecimal> successRate = new ArrayList<>(Collections.nCopies(dbDates.size(), BigDecimal.ZERO));

        // 创建日期索引映射
        Map<String, Integer> dateIndexMap = new HashMap<>();
        for (int i = 0; i < dbDates.size(); i++) {
            dateIndexMap.put(dbDates.get(i), i);
        }

        // 批量获取退款、拒付、取消和成功订单的统计数据
        Map<String, Integer> chargebackMap = new HashMap<>();
        Map<String, Integer> cancelMap = new HashMap<>();
        Map<String, Integer> successMap = new HashMap<>();
        Map<String, Integer> countMap = new HashMap<>();

        // 获取退款订单数量（从refundOrder表查询）
        Map<String, Integer> refundMap = new HashMap<>(
                refundOrderService.countRefundOrdersByDate(shopId, startDate, endDate));

        // 获取拒付订单数量（保持原有逻辑）
        List<String> chargebackTypes = Collections.singletonList(NuvieOrderStatus.CB.getCode());
        List<OrderStatusStatistics> chargebackStats = movementDataMapper.countByDateAndType(
                startDate + " 00:00:00", endDate + " 23:59:59", chargebackTypes, shopId);
        for (OrderStatusStatistics stat : chargebackStats) {
            chargebackMap.put(stat.getDate(), stat.getCount());
        }

        // 获取订单状态统计数据（包括取消和成功订单）
        List<OrderStatusStatistics> orderStatusStats = orderMapper.getOrderStatusStatistics(
                startDate, endDate, shopId);
        // 分别筛选取消订单和成功订单数据
        for (OrderStatusStatistics stat : orderStatusStats) {
            String status = stat.getStatus();
            String date = stat.getDate();
            Integer count = stat.getCount();

            // 取消订单
            if (Objects.equals(status, OrderStatus.CANCELED.getCode())) {
                cancelMap.put(date, count);
            }
            // 成功订单
            else if (Objects.equals(status, OrderStatus.COMPLETED.getCode()) ||
                    Objects.equals(status, OrderStatus.CLOSE_REFUND.getCode()) ||
                    Objects.equals(status, OrderStatus.CLOSE_DECLINE.getCode())) {
                successMap.put(date, count);
            }
            countMap.put(date, countMap.getOrDefault(date, 0) + count);

        }

        // 计算每天的比率
        for (String date : dbDates) {
            Integer index = dateIndexMap.get(date);
            if (index != null) {
                int refundCount = refundMap.getOrDefault(date, 0);
                int chargebackCount = chargebackMap.getOrDefault(date, 0);
                int cancelCount = cancelMap.getOrDefault(date, 0);
                int successCount = successMap.getOrDefault(date, 0);
                int count = countMap.getOrDefault(date, 0);

                // 打印日志：成功订单数、退款订单数、总订单数和对应日期
                log.info("订单统计 - 日期: {}, 成功订单数: {}, 退款订单数: {}, 总订单数: {}",
                        date, successCount, refundCount, count);

                // 计算退款率 = 退款订单数量/成功订单数量*100%，结果不要小数
                if (successCount > 0) {
                    refundRate.set(index, new BigDecimal(refundCount).multiply(new BigDecimal(100))
                            .divide(new BigDecimal(successCount), 2, RoundingMode.DOWN));
                }

                // 计算拒付率 = 拒付订单数量/成功订单数量*100%，保留两位小数
                if (successCount > 0) {
                    chargebackRate.set(index, new BigDecimal(chargebackCount).multiply(new BigDecimal(100))
                            .divide(new BigDecimal(successCount), 2, RoundingMode.DOWN));
                }

                // 计算取消率 = 取消订单数量/(成功订单数量 + 取消订单数量)*100%，保留两位小数
                if ((successCount + cancelCount) > 0) {
                    cancelRate.set(index, new BigDecimal(cancelCount).multiply(new BigDecimal(100))
                            .divide(new BigDecimal(successCount + cancelCount), 2, RoundingMode.DOWN));
                }
                if (count > 0) {
                    successRate.set(index, new BigDecimal(successCount).multiply(new BigDecimal(100))
                            .divide(new BigDecimal(count), 2, RoundingMode.DOWN));
                }
            }
        }

        // 构造返回结果
        RatesTrend ratesTrend = new RatesTrend();
        ratesTrend.setDates(displayDates);
        ratesTrend.setRefundRate(refundRate);
        ratesTrend.setChargebackRate(chargebackRate);
        ratesTrend.setCancelRate(cancelRate);
        ratesTrend.setSuccessRate(successRate);
        return ratesTrend;
    }

    @Override
    public PaymentMethodStatsVo paymentMethodStats(String startDate, String endDate, Long shopId) {
        PaymentMethodStatsVo result = new PaymentMethodStatsVo();

        // 获取各种支付方式的统计
        List<PaymentMethodStatistics> paymentStats = orderMapper.getPaymentMethodStatistics(startDate, endDate, shopId);

        // 获取卡支付的各种卡类型的统计
        List<PaymentMethodStatistics> cardTypeStats = orderMapper.getCardTypeStatistics(startDate, endDate, shopId);

        // 计算总订单数
        int totalOrders = paymentStats.stream().mapToInt(PaymentMethodStatistics::getCount).sum();

        // 构造返回结果
        List<PaymentMethodVo> methods = new ArrayList<>();

        // 处理各种支付方式
        for (PaymentMethodStatistics stat : paymentStats) {
            String type = stat.getType();
            Integer count = stat.getCount();
            double percentage = totalOrders > 0 ? (count * 100.0 / totalOrders) : 0;

            PaymentMethodVo methodVo = new PaymentMethodVo();
            methodVo.setName(getPaymentTypeName(type));
            methodVo.setValue(Double.valueOf(String.format("%.1f", percentage)));

            // 如果是卡支付，添加子项
            if ("card".equals(type)) {
                List<PaymentMethodVo> children = new ArrayList<>();
                int cardTotal = cardTypeStats.stream().mapToInt(PaymentMethodStatistics::getCount).sum();

                for (PaymentMethodStatistics cardStat : cardTypeStats) {
                    String cardType = cardStat.getType();
                    Integer cardCount = cardStat.getCount();
                    double cardPercentage = cardTotal > 0 ? (cardCount * 100.0 / cardTotal) : 0;

                    PaymentMethodVo childVo = new PaymentMethodVo();
                    childVo.setName(getCardTypeName(cardType));
                    childVo.setValue(Double.valueOf(String.format("%.1f", cardPercentage)));
                    children.add(childVo);
                }

                methodVo.setChildren(children);
            }

            methods.add(methodVo);
        }

        result.setMethods(methods);
        return result;
    }

    /**
     * 获取支付方式显示名称
     */
    private String getPaymentTypeName(String type) {
        if (type == null) {
            return "UNKNOWN";
        }
        switch (type) {
            case "card":
                return "cardPay";
            case "google_pay":
                return "GooglePay";
            case "apple_pay":
                return "applePay";
            default:
                return "other";
        }
    }

    /**
     * 获取卡类型显示名称
     */
    private String getCardTypeName(String cardType) {
        if (cardType == null) {
            return "UNKNOWN";
        }
        switch (cardType.toLowerCase()) {
            case "visa":
                return "Visa";
            case "master":
            case "mastercard":
                return "Master";
            case "amex":
            case "american_express":
                return "American Express";
            case "jcb":
                return "JCB";
            case "unionpay":
                return MessageUtils.message("payment.channel.unionpay");
            default:
                return cardType;
        }
    }

    @Override
    public TotalFiatAmountVo getFiatAmountTotalByMerchant(Long merchantId) {
        return orderMapper.getFiatAmountTotalByMerchant(merchantId).get(0);
    }

    public BigDecimal getRollingFeeTotalByMerchant(Long merchantId) {
        return orderMapper.getRollingFeeTotalByMerchant(merchantId);
    }

    @Override
    public TotalFiatAmountVo getTransferAmountTotalByMerchant(Long merchantId) {
        return orderMapper.getTransferAmountTotalByMerchant(merchantId).get(0);
    }

    @Override
    public Map<Long, BigDecimal> getOrderTotalAmount(Long shopId, String start, String end) {
        QueryWrapper<Order> qw = new QueryWrapper<>();
        qw.select("currency_id, IFNULL(SUM(fiat_amount), 0) as totalAmount")
                .eq("merchant_id", shopId)
                .eq("status", OrderStatus.COMPLETED.getCode())
                .ge(start != null, "created_at", start) // >= 开始时间
                .le(end != null, "created_at", end) // <= 结束时间
                .groupBy("currency_id");

        List<Map<String, Object>> list = listMaps(qw);

        // 转换成 Map<Long, BigDecimal>
        return list.stream().collect(Collectors.toMap(
                m -> ((Number) m.get("currency_id")).longValue(),
                m -> (BigDecimal) m.get("totalAmount")));
    }

    /**
     * 获取订单总数
     * 
     * @param shopId
     * @param start
     * @param end
     * @param state
     * @return
     */
    @Override
    public Integer getOrderTotalCount(Long shopId, Date start, Date end, int state) {
        QueryWrapper<Order> qw = new QueryWrapper<>();
        qw.eq("merchant_id", shopId)
                .between(start != null && end != null, "created_at", start, end);
        if (state == 1) {
            qw.eq("status", OrderStatus.COMPLETED.getCode());
        } else {
            qw.ne("status", OrderStatus.COMPLETED.getCode());
        }
        return Math.toIntExact(count(qw));
    }

    public Date getLatestOrder(Long shopId) {
        QueryWrapper<Order> qw = new QueryWrapper<>();
        qw.select("MAX(created_at) as maxCreatedAt");
        if (shopId != null) {
            qw.eq("merchant_id", shopId);
        }
        Map<String, Object> result = getMap(qw);
        return result != null ? (Date) result.get("maxCreatedAt") : null;
    }

    @Override
    public BigDecimal getCompletedAmountBetween(String startDatetime, String endDatetime, Long shopId) {
        BigDecimal amount = orderMapper.getOrderAmount(startDatetime, endDatetime, shopId);
        return amount == null ? BigDecimal.ZERO : amount;
    }

    @Override
    public BigDecimal getCompletedFiatAmountBetween(String startDatetime, String endDatetime, Long shopId) {
        BigDecimal amount = orderMapper.getOrderFiatAmount(startDatetime, endDatetime, shopId);
        return amount == null ? BigDecimal.ZERO : amount;
    }

    @Override
    public BigDecimal getCompletedFiatAmountBetween(String startDatetime, String endDatetime, Long shopId,
            String timezone) {
        // 目前时区参数暂未在SQL层面使用，保持原有逻辑
        // 后续可以根据需要在SQL中添加时区转换逻辑
        log.info("查询已完成订单法币金额: startDatetime={}, endDatetime={}, shopId={}, timezone={}",
                startDatetime, endDatetime, shopId, timezone);
        BigDecimal amount = orderMapper.getOrderFiatAmount(startDatetime, endDatetime, shopId);
        return amount == null ? BigDecimal.ZERO : amount;
    }

    @Override
    public HourlySuccessRateVo getHourlySuccessRate(String startDatetime, String endDatetime, Long shopId,
            String timezone) {
        log.info("OrderService获取每小时成功率: startDatetime={}, endDatetime={}, shopId={}", startDatetime, endDatetime,
                shopId);

        // 获取每小时订单统计数据
        List<OrderStatusStatistics> hourlyStats = orderMapper.getHourlyOrderStatistics(startDatetime, endDatetime,
                shopId);
        log.info("查询到的统计数据条数: {}", hourlyStats.size());

        // 创建时间序列，用于确保每个小时都有数据点
        Map<String, Map<String, Integer>> hourlyDataMap = new LinkedHashMap<>();

        // 生成完整的小时时间序列
        List<String> hourLabels = generateHourlyLabels(startDatetime, endDatetime, timezone);
        log.info("生成的时间标签: {}", hourLabels);

        // 初始化每个小时的数据结构
        for (String hour : hourLabels) {
            hourlyDataMap.put(hour, new HashMap<>());
        }

        // 填充实际数据
        for (OrderStatusStatistics stat : hourlyStats) {
            String hour = stat.getDate(); // 格式: MM-dd HH:00
            String status = stat.getStatus();
            Integer count = stat.getCount();

            Map<String, Integer> hourData = hourlyDataMap.get(hour);
            if (hourData != null) {
                hourData.put(status, count);
            }
        }

        // 构建返回结果
        List<BigDecimal> successRates = new ArrayList<>();
        List<Integer> totalOrders = new ArrayList<>();
        List<Integer> successOrders = new ArrayList<>();
        List<Integer> failedOrders = new ArrayList<>();

        for (String hour : hourLabels) {
            Map<String, Integer> hourData = hourlyDataMap.get(hour);

            // 计算成功订单数（状态为9的订单）
            int successCount = hourData.getOrDefault(OrderStatus.COMPLETED.getCode(), 0);

            // 计算失败订单数（状态为4,5,6,7,17,20,30,40的订单）
            int failedCount = hourData.getOrDefault(OrderStatus.WAITING_FOR_DEPOSIT.getCode(), 0) +
                    hourData.getOrDefault(OrderStatus.APPROVED.getCode(), 0) +
                    hourData.getOrDefault(OrderStatus.DEPOSIT_DECLINED.getCode(), 0) +
                    hourData.getOrDefault(OrderStatus.CANCELED.getCode(), 0) +
                    hourData.getOrDefault(OrderStatus.TDS_PENDING.getCode(), 0) +
                    hourData.getOrDefault(OrderStatus.CREDITS.getCode(), 0) +
                    hourData.getOrDefault(OrderStatus.CB_CANCELLED.getCode(), 0) +
                    hourData.getOrDefault(OrderStatus.RDR.getCode(), 0);

            // 计算总订单数
            int totalCount = hourData.values().stream().mapToInt(Integer::intValue).sum();

            // 计算成功率
            BigDecimal successRate = BigDecimal.ZERO;
            if (totalCount > 0) {
                successRate = new BigDecimal(successCount)
                        .multiply(new BigDecimal(100))
                        .divide(new BigDecimal(totalCount), 2, RoundingMode.HALF_UP);
            }

            successRates.add(successRate);
            totalOrders.add(totalCount);
            successOrders.add(successCount);
            failedOrders.add(failedCount);
        }

        HourlySuccessRateVo result = new HourlySuccessRateVo();
        result.setHourLabels(hourLabels);
        result.setSuccessRates(successRates);
        result.setTotalOrders(totalOrders);
        result.setSuccessOrders(successOrders);
        result.setFailedOrders(failedOrders);

        return result;
    }

    /**
     * 生成小时时间标签
     */
    private List<String> generateHourlyLabels(String startDatetime, String endDatetime, String timezone) {
        List<String> labels = new ArrayList<>();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat labelFormat = new SimpleDateFormat("MM-dd HH");

            // 设置时区
            TimeZone targetTimeZone;
            if (timezone != null && !timezone.trim().isEmpty()) {
                targetTimeZone = TimeZone.getTimeZone(timezone);
            } else {
                // 默认使用北京时间
                targetTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
            }

            sdf.setTimeZone(targetTimeZone);
            labelFormat.setTimeZone(targetTimeZone);

            Date start = sdf.parse(startDatetime);
            Date end = sdf.parse(endDatetime);

            Calendar calendar = Calendar.getInstance(targetTimeZone);
            calendar.setTime(start);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            while (calendar.getTime().before(end) || calendar.getTime().equals(end)) {
                labels.add(labelFormat.format(calendar.getTime()));
                calendar.add(Calendar.HOUR_OF_DAY, 1);
            }

            log.info("时间标签生成: start={}, end={}, timezone={}, labels={}", startDatetime, endDatetime, timezone, labels);
        } catch (Exception e) {
            log.error("生成小时标签失败", e);
        }

        return labels;
    }

    @Override
    public IndexOrderAmountCurveVo orderAmountCurve(String startDate, String endDate, Long shopId, String timezone) {
        // 目前时区参数暂未在业务逻辑中使用，保持原有逻辑
        log.info("订单金额曲线统计: startDate={}, endDate={}, shopId={}, timezone={}", startDate, endDate, shopId, timezone);
        return orderAmountCurve(startDate, endDate, shopId);
    }

    @Override
    public IndexOrderAvgAmountCurveVo orderAvgAmountCurve(String startDate, String endDate, Long shopId,
            String timezone) {
        // 目前时区参数暂未在业务逻辑中使用，保持原有逻辑
        log.info("订单平均金额曲线统计: startDate={}, endDate={}, shopId={}, timezone={}", startDate, endDate, shopId, timezone);
        return orderAvgAmountCurve(startDate, endDate, shopId);
    }

    @Override
    public RatesTrend orderRateCurve(String startDate, String endDate, Long shopId, String timezone) {
        // 目前时区参数暂未在业务逻辑中使用，保持原有逻辑
        log.info("订单费率曲线统计: startDate={}, endDate={}, shopId={}, timezone={}", startDate, endDate, shopId, timezone);
        return orderRateCurve(startDate, endDate, shopId);
    }

    @Override
    public PaymentMethodStatsVo paymentMethodStats(String startDate, String endDate, Long shopId, String timezone) {
        // 目前时区参数暂未在业务逻辑中使用，保持原有逻辑
        log.info("支付方式统计: startDate={}, endDate={}, shopId={}, timezone={}", startDate, endDate, shopId, timezone);
        return paymentMethodStats(startDate, endDate, shopId);
    }

    @Override
    public FailReasonStatsVo getFailReasonStats(String startDate, String endDate, Long shopId) {
        log.info("获取失败原因统计: startDate={}, endDate={}, shopId={}", startDate, endDate, shopId);

        // 构建时间范围字符串
        String startDatetime = startDate + " 00:00:00";
        String endDatetime = endDate + " 23:59:59";

        // 获取失败原因统计数据
        List<FailReasonStatistics> failReasonStats = orderMapper.getFailReasonStatistics(startDatetime, endDatetime,
                shopId);
        log.info("查询到的失败原因统计条数: {}", failReasonStats.size());

        // 统计总失败订单数
        int totalFailedOrders = failReasonStats.stream().mapToInt(FailReasonStatistics::getCount).sum();

        // 按失败原因类型分组统计
        Map<FailReasonType, Integer> typeCountMap = new HashMap<>();
        Map<String, Integer> otherReasonMap = new HashMap<>(); // 存储其他原因的具体信息

        for (FailReasonStatistics stat : failReasonStats) {
            String failReason = stat.getFailReason();
            Integer count = stat.getCount();

            // 匹配失败原因类型
            FailReasonType type = FailReasonType.matchFailReason(failReason);

            if (type == FailReasonType.OTHER) {
                // 其他原因展开显示具体错误信息
                otherReasonMap.put(failReason, count);
            } else {
                // 已知类型进行合并
                typeCountMap.merge(type, count, Integer::sum);
            }
        }

        // 构建返回结果
        List<FailReasonStatsVo.FailReasonVo> failReasonVos = new ArrayList<>();

        // 添加已知类型的失败原因
        for (Map.Entry<FailReasonType, Integer> entry : typeCountMap.entrySet()) {
            FailReasonType type = entry.getKey();
            Integer count = entry.getValue();

            // 计算占比
            double percentage = totalFailedOrders > 0 ? (count * 100.0 / totalFailedOrders) : 0;

            FailReasonStatsVo.FailReasonVo vo = new FailReasonStatsVo.FailReasonVo(
                    type.getDisplayName(),
                    Double.valueOf(String.format("%.1f", percentage)),
                    count,
                    type.name());

            failReasonVos.add(vo);
        }

        // 添加其他原因的具体错误信息（展开显示）
        for (Map.Entry<String, Integer> entry : otherReasonMap.entrySet()) {
            String failReason = entry.getKey();
            Integer count = entry.getValue();

            // 计算占比
            double percentage = totalFailedOrders > 0 ? (count * 100.0 / totalFailedOrders) : 0;

            // 截断过长的错误信息
            String displayName = failReason.length() > 50 ? failReason.substring(0, 47) + "..." : failReason;

            FailReasonStatsVo.FailReasonVo vo = new FailReasonStatsVo.FailReasonVo(
                    displayName,
                    Double.valueOf(String.format("%.1f", percentage)),
                    count,
                    "OTHER_" + failReason.hashCode(), // 使用hashCode作为唯一标识
                    failReason // 完整的错误信息
            );

            failReasonVos.add(vo);
        }

        // 按订单数量降序排序
        failReasonVos.sort((a, b) -> b.getCount().compareTo(a.getCount()));

        // 构建返回结果
        FailReasonStatsVo result = new FailReasonStatsVo();
        result.setFailReasons(failReasonVos);
        result.setTotalFailedOrders(totalFailedOrders);
        result.setTimeRange(startDate + " 至 " + endDate);

        log.info("失败原因统计结果: 总失败订单数={}, 分类数={}", totalFailedOrders, failReasonVos.size());

        return result;
    }

    @Override
    public void batchPopulateExportFields(List<Order> orders) {
        batchPopulateExportFieldsInternal(orders, null);
    }

    /**
     * 批量填充导出所需的额外字段（多线程安全）
     */
    private void batchPopulateExportFieldsInternal(List<Order> orders, ExportProgressTracker tracker) {
        if (orders == null || orders.isEmpty()) {
            return;
        }

        if (tracker != null) {
            tracker.onBatchStart(orders.size());
        }

        long batchStartTime = System.currentTimeMillis();

        try {
            // 收集所有需要的ID
            Set<Long> merchantIds = new HashSet<>();
            Set<Long> customerIds = new HashSet<>();
            Set<Long> orderIds = new HashSet<>();

            for (Order order : orders) {
                if (order.getMerchantId() != null) {
                    merchantIds.add(order.getMerchantId());
                }
                if (order.getCustomerId() != null) {
                    customerIds.add(order.getCustomerId());
                }
                if (order.getId() != null) {
                    orderIds.add(order.getId());
                }
            }

            // 1. 批量查询商户信息（支付机构）
            Map<Long, String> merchantSourceMap = new HashMap<>();
            if (!merchantIds.isEmpty()) {
                try {
                    List<Shop> shops = shopService.listByIds(merchantIds);
                    for (Shop shop : shops) {
                        if (shop.getFromSource() != null) {
                            merchantSourceMap.put(shop.getId(), shop.getFromSource());
                        }
                    }
                } catch (Exception e) {
                    log.warn("批量查询商户信息失败", e);
                }
            }

            // 2. 批量查询客户信息（邮箱）
            Map<Long, String> customerEmailMap = new HashMap<>();
            if (!customerIds.isEmpty()) {
                try {
                    List<Customer> customers = customerService.listByIds(customerIds);
                    Set<Long> userIds = customers.stream()
                            .map(Customer::getUserId)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toSet());

                    if (!userIds.isEmpty()) {
                        List<User> users = userService.listByIds(userIds);
                        Map<Long, String> userEmailMap = users.stream()
                                .filter(u -> u.getEmail() != null)
                                .collect(Collectors.toMap(User::getId, User::getEmail, (v1, v2) -> v1));

                        for (Customer customer : customers) {
                            if (customer.getUserId() != null && userEmailMap.containsKey(customer.getUserId())) {
                                customerEmailMap.put(customer.getId(), userEmailMap.get(customer.getUserId()));
                            }
                        }
                    }
                } catch (Exception e) {
                    log.warn("批量查询客户邮箱失败", e);
                }
            }

            // 3. 批量查询钱包地址
            Map<String, String> walletAddressMap = new HashMap<>();
            if (!customerIds.isEmpty()) {
                try {
                    // 批量查询所有客户的钱包
                    List<CustomerWallet> wallets = customerWalletService.lambdaQuery()
                            .in(CustomerWallet::getCustomerId, customerIds)
                            .list();

                    // 构建 customerId_cryptoId -> walletAddress 映射
                    for (CustomerWallet wallet : wallets) {
                        if (wallet.getCustomerId() != null && wallet.getCryptoId() != null
                                && wallet.getWalletAddress() != null) {
                            String key = wallet.getCustomerId() + "_" + wallet.getCryptoId();
                            walletAddressMap.put(key, wallet.getWalletAddress());
                        }
                    }
                } catch (Exception e) {
                    log.warn("批量查询钱包地址失败", e);
                }
            }

            // 4. 批量查询退款金额
            Map<Long, BigDecimal> refundAmountMap = new HashMap<>();
            if (!orderIds.isEmpty()) {
                try {
                    List<RefundOrder> refundOrders = refundOrderService.lambdaQuery()
                            .in(RefundOrder::getOrderId, orderIds)
                            .list();

                    Map<Long, List<RefundOrder>> refundGroupMap = refundOrders.stream()
                            .collect(Collectors.groupingBy(RefundOrder::getOrderId));

                    for (Map.Entry<Long, List<RefundOrder>> entry : refundGroupMap.entrySet()) {
                        BigDecimal totalRefund = entry.getValue().stream()
                                .map(RefundOrder::getRefundAmount)
                                .filter(Objects::nonNull)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                        refundAmountMap.put(entry.getKey(), totalRefund);
                    }
                } catch (Exception e) {
                    log.warn("批量查询退款金额失败", e);
                }
            }

            // 5. 填充数据到订单对象
            for (Order order : orders) {
                try {
                    // 填充支付机构
                    if (order.getMerchantId() != null && merchantSourceMap.containsKey(order.getMerchantId())) {
                        order.setFromSource(merchantSourceMap.get(order.getMerchantId()));
                    }

                    // 填充邮箱
                    if (order.getCustomerId() != null && customerEmailMap.containsKey(order.getCustomerId())) {
                        order.setEmail(customerEmailMap.get(order.getCustomerId()));
                    }

                    // 填充钱包地址
                    if (order.getCustomerId() != null && order.getCryptoId() != null) {
                        String key = order.getCustomerId() + "_" + order.getCryptoId();
                        if (walletAddressMap.containsKey(key)) {
                            order.setWalletAddress(walletAddressMap.get(key));
                        }
                    }

                    // 填充退款金额
                    if (order.getId() != null) {
                        order.setRefundAmount(refundAmountMap.getOrDefault(order.getId(), BigDecimal.ZERO));
                    }
                } catch (Exception e) {
                    log.warn("填充订单数据失败, orderId={}", order.getId(), e);
                }
            }

            if (tracker != null) {
                tracker.recordBatch(orders.size());
            }

            long batchTime = System.currentTimeMillis() - batchStartTime;
            // 记录详细的批次统计（DEBUG级别，生产环境可关闭）
            log.debug("批次处理完成，数量：{}，商户数：{}，客户数：{}，耗时：{} ms",
                    orders.size(), merchantIds.size(), customerIds.size(), batchTime);
        } catch (Exception e) {
            log.error("批量填充导出字段异常", e);
        }
    }

    private static final class ExportProgressTracker {
        private static final long DEFAULT_LOG_STEP = 20_000L;
        private final LongAdder processedCount = new LongAdder();
        private final AtomicLong nextLogThreshold;
        private final long logStep;
        private final long startTimeMillis = System.currentTimeMillis();
        private final AtomicBoolean started = new AtomicBoolean(false);

        private ExportProgressTracker(long logStep) {
            this.logStep = Math.max(logStep, 1L);
            this.nextLogThreshold = new AtomicLong(this.logStep);
        }

        static ExportProgressTracker create(long logStep) {
            return new ExportProgressTracker(logStep > 0 ? logStep : DEFAULT_LOG_STEP);
        }

        void onBatchStart(int batchSize) {
            if (started.compareAndSet(false, true)) {
                log.info("开始批量填充导出字段，每批 {} 条", batchSize);
            }
        }

        void recordBatch(int batchSize) {
            processedCount.add(batchSize);
            maybeLogProgress();
        }

        void finish(long totalElapsedMillis) {
            long total = processedCount.sum();
            double seconds = totalElapsedMillis / 1000.0;
            log.info("批量填充导出字段完成，总计 {} 条，耗时：{} ms ({} 秒)", total, totalElapsedMillis,
                    String.format("%.2f", seconds));
        }

        private void maybeLogProgress() {
            long total = processedCount.sum();
            long threshold;
            while ((threshold = nextLogThreshold.get()) <= total) {
                if (nextLogThreshold.compareAndSet(threshold, threshold + logStep)) {
                    long elapsed = System.currentTimeMillis() - startTimeMillis;
                    double seconds = elapsed / 1000.0;
                    log.info("导出进度：已处理 {} 条，已耗时：{} ms ({} 秒)", total, elapsed,
                            String.format("%.2f", seconds));
                } else {
                    // 竞争情况下重读最新阈值
                    continue;
                }
            }
        }
    }

    /**
     * 填充订单支付信息（解析payInfo JSON，提取卡号等）
     */
    @Override
    public void populatePayInfo(Order order, boolean clearRawData) {
        if (order == null) {
            return;
        }
        String rawPayInfo = order.getPayInfo();
        if (rawPayInfo == null || rawPayInfo.isEmpty()) {
            if (clearRawData) {
                order.setPayInfo(null);
            }
            return;
        }
        try {
            PayOrderVo payInfoVo = JSONUtil.toBean(rawPayInfo, PayOrderVo.class);
            if (payInfoVo != null && payInfoVo.getCardInfo() != null) {
                // 清除敏感的 CVV 信息
                payInfoVo.getCardInfo().setCvv(null);
                // 提取卡号
                order.setCardNumber(payInfoVo.getCardInfo().getCardNumber());
            }
            order.setPayInfoVo(payInfoVo);
        } catch (Exception ex) {
            log.warn("解析订单支付信息失败, orderId={}", order.getId(), ex);
        } finally {
            if (clearRawData) {
                order.setPayInfo(null);
            }
        }
    }

    /**
     * 批量填充订单支付信息
     */
    @Override
    public void batchPopulatePayInfo(List<Order> orders, boolean clearRawData) {
        if (orders == null || orders.isEmpty()) {
            return;
        }
        for (Order order : orders) {
            populatePayInfo(order, clearRawData);
        }
    }

    /**
     * 获取订单详细信息并填充关联数据
     */
    @Override
    public Order getOrderDetailWithRelatedData(Long id, Long merchantId) {
        LambdaQueryWrapper<Order> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Order::getId, id);
        if (merchantId != null) {
            queryWrapper.eq(Order::getMerchantId, merchantId);
        }
        Order order = this.getOne(queryWrapper);

        if (order == null) {
            return null;
        }

        // 填充支付信息（卡号等）
        populatePayInfo(order, true);

        try {
            // 填充钱包地址
            if (order.getCustomerId() != null && order.getCryptoId() != null) {
                try {
                    CustomerWallet wallet = customerWalletService.getCustomerWalletByCrypto(order.getCustomerId(),
                            order.getCryptoId());
                    if (wallet != null) {
                        order.setWalletAddress(wallet.getWalletAddress());
                    }
                } catch (Exception e) {
                    log.warn("填充钱包地址失败, orderId={}", id, e);
                }
            }

            // 填充支付机构
            if (order.getMerchantId() != null) {
                try {
                    Shop shop = shopService.getById(order.getMerchantId());
                    if (shop != null) {
                        order.setFromSource(shop.getFromSource());
                    }
                } catch (Exception e) {
                    log.warn("填充支付机构失败, orderId={}", id, e);
                }
            }

            // 填充退款金额
            try {
                List<RefundOrder> refundOrders = refundOrderService.lambdaQuery()
                        .eq(RefundOrder::getOrderId, id)
                        .list();
                BigDecimal refundAmount = refundOrders.stream()
                        .map(RefundOrder::getRefundAmount)
                        .filter(Objects::nonNull)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                order.setRefundAmount(refundAmount);
            } catch (Exception e) {
                log.warn("填充退款金额失败, orderId={}", id, e);
            }

            // 填充邮箱
            if (order.getCustomerId() != null) {
                try {
                    Customer customer = customerService.getById(order.getCustomerId());
                    if (customer != null && customer.getUserId() != null) {
                        User user = userService.getById(customer.getUserId());
                        if (user != null) {
                            order.setEmail(user.getEmail());
                        }
                    }
                } catch (Exception e) {
                    log.warn("填充邮箱失败, orderId={}", id, e);
                }
            }
        } catch (Exception e) {
            log.error("填充订单详细信息失败, orderId={}", id, e);
        }

        return order;
    }

}
