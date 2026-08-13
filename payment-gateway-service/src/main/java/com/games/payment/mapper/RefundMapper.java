package com.games.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.games.payment.domain.RefundOrder;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface RefundMapper extends BaseMapper<RefundOrder> {

    List<RefundOrder> selectAllList(RefundOrder refundOrder);

    /**
     * 按日期统计指定商户的退款订单数量
     * @param merchantId 商户ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 按日期分组的退款订单数量统计 List<Map<String, Object>>
     */
    @MapKey("date")
    List<Map<String, Object>> countRefundOrdersByDate(@Param("merchantId") Long merchantId, 
                                                     @Param("startTime") String startTime, 
                                                     @Param("endTime") String endTime);

    /**
     * 按日期统计退款金额（sum(refund_amount)）
     */
    @MapKey("date")
    List<Map<String, Object>> sumRefundAmountByDate(@Param("merchantId") Long merchantId,
                                                    @Param("startTime") String startTime,
                                                    @Param("endTime") String endTime);
}
