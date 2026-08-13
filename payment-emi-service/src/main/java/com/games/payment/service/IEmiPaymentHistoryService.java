package com.games.payment.service;

import com.games.payment.domain.EmiPaymentHistory;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 付款记录Service接口
 *
 * @author Ticker
 * @date 2025-10-16
 */
public interface IEmiPaymentHistoryService extends IService<EmiPaymentHistory> {

    /**
     * 查询付款记录列表
     *
     * @param emiPaymentHistory 付款记录
     * @return 付款记录集合
     */
    List<EmiPaymentHistory> selectAllList(EmiPaymentHistory emiPaymentHistory);

    /**
     * 查询列表
     */
    List<EmiPaymentHistory> queryList(EmiPaymentHistory emiPaymentHistory);

}
