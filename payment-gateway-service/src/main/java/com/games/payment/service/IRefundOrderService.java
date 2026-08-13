package com.games.payment.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.games.payment.domain.RefundOrder;

import java.util.List;
import java.util.Map;

public interface IRefundOrderService extends IService<RefundOrder> {

    RefundOrder selectByOrderId(Long orderId);

    RefundOrder selectSuccessOrderId(Long orderId);


    RefundOrder selectByRefundSsoId(String refundSsoId);


    RefundOrder selectByRefundSsoIdAndOrderId(String refundSsoId, Long orderId);

    List<RefundOrder> selectAllList(RefundOrder refundOrder);

    /**
     * 查询列表
     */
    List<RefundOrder> queryList(RefundOrder refundOrder);

    /**
     * 按日期统计指定商户的退款订单数量
     * @param merchantId 商户ID
     * @param startDate 开始日期 (格式: yyyy-MM-dd)
     * @param endDate 结束日期 (格式: yyyy-MM-dd)
     * @return 按日期分组的退款订单数量统计 Map<日期, 数量>
     */
    Map<String, Integer> countRefundOrdersByDate(Long merchantId, String startDate, String endDate);

    /**
     * 按日期统计退款金额（sum(refund_amount)）
     * @param merchantId 商户ID
     * @param startDate 开始日期 (yyyy-MM-dd)
     * @param endDate 结束日期 (yyyy-MM-dd)
     * @return Map<日期(yyyy-MM-dd), 金额>
     */
    java.util.Map<String, java.math.BigDecimal> sumRefundAmountByDate(Long merchantId, String startDate, String endDate);

}
