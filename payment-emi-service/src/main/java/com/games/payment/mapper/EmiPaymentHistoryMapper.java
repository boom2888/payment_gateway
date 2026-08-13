package com.games.payment.mapper;

import com.games.payment.domain.EmiPaymentHistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 付款记录Mapper接口
 *
 * @author Ticker
 * @date 2025-10-16
 */
public interface EmiPaymentHistoryMapper extends BaseMapper<EmiPaymentHistory> {
    /**
     * 查询付款记录列表
     *
     * @param emiPaymentHistory 付款记录
     * @return 付款记录集合
     */
    List<EmiPaymentHistory> selectAllList(EmiPaymentHistory emiPaymentHistory);

}
