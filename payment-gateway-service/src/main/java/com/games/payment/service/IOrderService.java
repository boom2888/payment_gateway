package com.games.payment.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.games.api.enums.OrderStatus;
import com.games.common.core.domain.AjaxResult;
import com.games.pay.dto.OrderWithFeeModelDto;
import com.games.pay.vo.*;
import com.games.payment.domain.Order;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 订单Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface IOrderService extends IService<Order> {

    Order getByAcquirerId(String acquirerId);

    Order getOrderBySsoOrderId(String ssoOrderId);

    /**
     * 根据relatedTransactionId (3ds_trxid) 查询订单
     * 用于DFP回调时通过3ds_trxid找到对应订单
     */
    Order getOrderByRelatedTransactionId(String relatedTransactionId);

    Order getOrderByMerchantOrderId(String ssoOrderId, String merchantId);

    boolean updateOrderState(Long orderNo, String superOrderId, OrderStatus state);

    boolean updateOrderStateAndTransactionID(Long orderNo, String superOrderId, OrderStatus state,
            String transactionId);

    /**
     * 条件更新订单：如果当前状态是指定状态，则不更新状态字段，但更新其他字段
     * 
     * @param order         要更新的订单对象
     * @param excludeStatus 排除的状态值，如果当前订单是这个状态则不更新状态字段
     * @return 更新是否成功
     */
    boolean updateOrderExcludeStatus(Order order, String excludeStatus);

    /**
     * 查询订单列表
     *
     * @param order 订单
     * @return 订单集合
     */
    List<Order> selectAllList(Order order);

    List<Order> exportQueryAllOrder(Order order);

    /**
     * 导出订单数据到Excel
     * 
     * @param order        查询条件
     * @param lang         语言
     * @param exportFields 导出字段列表（逗号分隔，为空则导出所有字段）
     * @return 导出结果
     */
    AjaxResult exportOrderToExcel(Order order, String lang, String exportFields);

    /**
     * 批量填充导出字段（钱包地址、支付机构、退款金额、邮箱等）
     * 
     * @param orders 订单列表
     */
    void batchPopulateExportFields(List<Order> orders);

    /**
     * 填充订单支付信息（解析payInfo JSON，提取卡号等）
     * 
     * @param order        订单对象
     * @param clearRawData 是否清除原始JSON数据
     */
    void populatePayInfo(Order order, boolean clearRawData);

    /**
     * 批量填充订单支付信息
     * 
     * @param orders       订单列表
     * @param clearRawData 是否清除原始JSON数据
     */
    void batchPopulatePayInfo(List<Order> orders, boolean clearRawData);

    /**
     * 获取订单详细信息并填充关联数据
     * 
     * @param id         订单ID
     * @param merchantId 商户ID（可选，用于权限校验）
     * @return 订单详情
     */
    Order getOrderDetailWithRelatedData(Long id, Long merchantId);

    /**
     * 查询列表
     */
    List<Order> queryList(Order order);

    List<Order> getNeedCallBackOrders(List<Long> shopIds, int callBackTimes);

    OrderWithFeeModelDto getOrderWithFeeModel(Long orderId);

    void settlementOrder(long orderId, BigDecimal orderAmount, int ordinal);

    IndexOrderStatisticsVo orderStatistics(Long shopId);

    IndexOrderStatisticsVo orderStatistics(Long shopId, String timezone);

    IndexShopRankStatisticsVo shopRankStatistics(IndexShopRankStatisticsRequest request);

    IndexOrderNumCurveVo orderNumCurve(String startDate, String endDate, Long shopId);

    IndexOrderNumCurveVo orderNumCurve(String startDate, String endDate, Long shopId, String timezone);

    /**
     * 订单金额曲线统计
     */
    IndexOrderAmountCurveVo orderAmountCurve(String startDate, String endDate, Long shopId);

    IndexOrderAmountCurveVo orderAmountCurve(String startDate, String endDate, Long shopId, String timezone);

    /**
     * 订单平均金额曲线统计
     */
    IndexOrderAvgAmountCurveVo orderAvgAmountCurve(String startDate, String endDate, Long shopId);

    IndexOrderAvgAmountCurveVo orderAvgAmountCurve(String startDate, String endDate, Long shopId, String timezone);

    /**
     * 订单退款率、拒付率、取消率曲线统计
     */
    RatesTrend orderRateCurve(String startDate, String endDate, Long shopId);

    RatesTrend orderRateCurve(String startDate, String endDate, Long shopId, String timezone);

    /**
     * 支付方式统计
     */
    PaymentMethodStatsVo paymentMethodStats(String startDate, String endDate, Long shopId);

    PaymentMethodStatsVo paymentMethodStats(String startDate, String endDate, Long shopId, String timezone);

    TotalFiatAmountVo getFiatAmountTotalByMerchant(Long merchantId);

    /**
     * 使用 transfer_amount 统计指定商户的已完成订单金额总和
     */
    TotalFiatAmountVo getTransferAmountTotalByMerchant(Long merchantId);

    BigDecimal getRollingFeeTotalByMerchant(Long merchantId);

    /**
     * 统计商户在一段时间内的总额
     * 
     * @param shopId
     * @param start
     * @param end
     * @return
     */
    Map<Long, BigDecimal> getOrderTotalAmount(Long shopId, String start, String end);

    Integer getOrderTotalCount(Long shopId, Date start, Date end, int state);

    Date getLatestOrder(Long shopId);

    /**
     * 获取指定时间范围内（含当日）已完成订单的总金额（按 transfer_amount 累加）
     * 
     * @param startDatetime 开始时间，格式 yyyy-MM-dd HH:mm:ss
     * @param endDatetime   结束时间，格式 yyyy-MM-dd HH:mm:ss
     * @param shopId        商户ID
     * @return 金额合计，若无订单返回 0
     */
    BigDecimal getCompletedAmountBetween(String startDatetime, String endDatetime, Long shopId);

    /**
     * 获取指定时间范围内（含当日）已完成订单的法币金额总和（fiat_amount）
     */
    BigDecimal getCompletedFiatAmountBetween(String startDatetime, String endDatetime, Long shopId);

    /**
     * 获取指定时间范围内（含当日）已完成订单的法币金额总和（fiat_amount）
     * 支持时区参数
     */
    BigDecimal getCompletedFiatAmountBetween(String startDatetime, String endDatetime, Long shopId, String timezone);

    /**
     * 获取指定时间范围内按小时统计的订单成功率
     *
     * @param startDatetime 开始时间（yyyy-MM-dd HH:mm:ss）
     * @param endDatetime   结束时间（yyyy-MM-dd HH:mm:ss）
     * @param shopId        商户ID
     * @param timezone      客户端时区，如"Asia/Shanghai"，null则使用默认时区
     * @return 每小时成功率统计
     */
    HourlySuccessRateVo getHourlySuccessRate(String startDatetime, String endDatetime, Long shopId, String timezone);

    /**
     * 获取失败原因统计
     *
     * @param startDate 开始日期（yyyy-MM-dd）
     * @param endDate   结束日期（yyyy-MM-dd）
     * @param shopId    商户ID
     * @return 失败原因统计
     */
    FailReasonStatsVo getFailReasonStats(String startDate, String endDate, Long shopId);
}
