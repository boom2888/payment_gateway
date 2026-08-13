package com.games.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.games.pay.dto.OrderStatusStatistics;
import com.games.pay.dto.OrderWithFeeModelDto;
import com.games.pay.vo.*;
import com.games.payment.domain.Order;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 订单Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface OrderMapper extends BaseMapper<Order> {
    /**
     * 查询订单列表
     *
     * @param order 订单
     * @return 订单集合
     */
    List<Order> selectAllList(Order order);

    List<Order> exportQueryAllOrder(Order order);

    OrderWithFeeModelDto getOrderWithFeeModel(@Param("id") Long orderId);

    List<OrderWithFeeModelDto> getOrdersWithFeeModelByIds(@Param("orderIds") Set<String> orderIds);

    void settlementOrder(@Param("orderId") long orderId,
                         @Param("orderAmount") BigDecimal orderAmount,
                         @Param("ordinal") int ordinal);

    int getOrderNumber(@Param("startDatetime") String startDatetime,
                       @Param("endDatetime") String endDatetime,
                       @Param("shopId") Long shopId,
                       @Param("status") List<String> status);


    /**
     * 批量查询指定时间范围内的订单数量
     *
     * @param timeRanges 时间范围列表
     * @param shopId     商户ID
     * @param status     订单状态列表
     * @return 订单数量列表
     */
    List<Integer> batchGetOrderNumber(@Param("timeRanges") List<Map<String, Object>> timeRanges,
                                      @Param("shopId") Long shopId,
                                      @Param("status") List<String> status);

    /**
     * 成交总额
     * @param startDatetime
     * @param endDatetime
     * @param shopId
     * @return
     */
    BigDecimal getOrderAmount(@Param("startDatetime") String startDatetime,
                              @Param("endDatetime") String endDatetime,
                              @Param("shopId") Long shopId);

    List<ShopOrderNumVo> getShopOrderNum(IndexShopRankStatisticsRequest request);

    List<ShopOrderAmountVo> getShopOrderAmount(IndexShopRankStatisticsRequest request);

    /**
     * 查询指定日期范围内已完成/已取消/处理中/已失败订单数量统计
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param shopId        商户ID
     * @return 按日期和状态分组的订单数量统计
     */
    List<OrderStatusStatistics> getOrderStatusStatistics(@Param("startDate") String startDate,
                                                         @Param("endDate") String endDate,
                                                         @Param("shopId") Long shopId);

    List<OrderAmountStatistics> getOrderAmountStatistics(@Param("startDate") String startDate,
                                                         @Param("endDate") String endDate,
                                                         @Param("shopId") Long shopId);


    /**
     * 查询指定日期范围内成功订单的平均金额统计
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param shopId        商户ID
     * @return 按日期分组的成功订单平均金额统计
     */
    List<OrderAmountStatistics> getOrderAvgAmountStatistics(@Param("startDate") String startDate,
                                                            @Param("endDate") String endDate,
                                                            @Param("shopId") Long shopId);

    /**
     * 查询指定日期范围内订单的平均金额
     *
     * @param startDatetime 开始时间
     * @param endDatetime   结束时间
     * @param shopId        商户ID
     * @return 订单平均金额
     */
    BigDecimal getOrderAvgAmount(@Param("startDatetime") String startDatetime,
                                 @Param("endDatetime") String endDatetime,
                                 @Param("shopId") Long shopId);

    /**
     * 指定日期范围内（完成订单）法币金额总和
     */
    BigDecimal getOrderFiatAmount(@Param("startDatetime") String startDatetime,
                                  @Param("endDatetime") String endDatetime,
                                  @Param("shopId") Long shopId);

    /**
     * 批量查询指定时间范围内的订单平均金额
     *
     * @param timeRanges 时间范围列表
     * @param shopId     商户ID
     * @return 订单平均金额列表
     */
    List<BigDecimal> batchGetOrderAvgAmount(@Param("timeRanges") List<Map<String, Object>> timeRanges,
                                            @Param("shopId") Long shopId);

    /**
     * 查询指定日期范围内各种支付方式的统计
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @param shopId 商户ID
     * @return 支付方式统计
     */
    List<PaymentMethodStatistics> getPaymentMethodStatistics(@Param("startDate") String startDate,
                                                             @Param("endDate") String endDate,
                                                             @Param("shopId") Long shopId);

    /**
     * 查询指定日期范围内卡支付的各种卡类型的统计
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @param shopId 商户ID
     * @return 卡类型统计
     */
    List<PaymentMethodStatistics> getCardTypeStatistics(@Param("startDate") String startDate,
                                                        @Param("endDate") String endDate,
                                                        @Param("shopId") Long shopId);


    /**
     * 批量查询指定时间范围内的订单金额
     *
     * @param timeRanges 时间范围列表
     * @param shopId     商户ID
     * @return 订单金额列表
     */
    List<BigDecimal> batchGetOrderAmount(@Param("timeRanges") List<Map<String, Object>> timeRanges,
                                         @Param("shopId") Long shopId);
    /**
     * 查询指定商户的已完成订单金额总和
     */
    List<TotalFiatAmountVo> getFiatAmountTotalByMerchant(@Param("merchantId") Long merchantId);

    /**
     * 查询指定商户的已完成订单 transfer_amount 总和
     */
    List<TotalFiatAmountVo> getTransferAmountTotalByMerchant(@Param("merchantId") Long merchantId);


    /**
     * 统计商户的循环保证金
     * @param merchantId 商户 id
     * @return 循环保证金总额
     */
    BigDecimal getRollingFeeTotalByMerchant(@Param("merchantId") Long merchantId);

    /**
     * 查询指定时间范围内按小时分组的订单统计
     * 
     * @param startDatetime 开始时间（yyyy-MM-dd HH:mm:ss）
     * @param endDatetime 结束时间（yyyy-MM-dd HH:mm:ss）
     * @param shopId 商户ID
     * @return 每小时订单统计列表
     */
    List<OrderStatusStatistics> getHourlyOrderStatistics(@Param("startDatetime") String startDatetime,
                                                         @Param("endDatetime") String endDatetime,
                                                         @Param("shopId") Long shopId);

    /**
     * 获取失败原因统计
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param shopId 商户ID
     * @return 失败原因统计列表
     */
    List<FailReasonStatistics> getFailReasonStatistics(@Param("startDate") String startDate,
                                                       @Param("endDate") String endDate,
                                                       @Param("shopId") Long shopId);

    /**
     * 查询用户近7天的累计支付金额
     *
     * @param customerId 用户ID
     * @return 累计金额
     */
    BigDecimal sumAmountByCustomerLast7Days(@Param("customerId") String customerId);

}
