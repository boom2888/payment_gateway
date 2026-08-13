package com.games.payment.service;

import com.games.payment.domain.Transaction;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 交易Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface ITransactionService extends IService<Transaction> {

    /**
     * 查询交易列表
     *
     * @param transaction 交易
     * @return 交易集合
     */
    List<Transaction> selectAllList(Transaction transaction);

    /**
     * 查询列表
     */
    List<Transaction> queryList(Transaction transaction);

}
