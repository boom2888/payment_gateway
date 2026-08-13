package com.games.payment.service;

import com.games.payment.domain.CryptoTransaction;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 加密货币交易Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface ICryptoTransactionService extends IService<CryptoTransaction> {

    /**
     * 查询加密货币交易列表
     *
     * @param cryptoTransaction 加密货币交易
     * @return 加密货币交易集合
     */
    List<CryptoTransaction> selectAllList(CryptoTransaction cryptoTransaction);

    /**
     * 查询列表
     */
    List<CryptoTransaction> queryList(CryptoTransaction cryptoTransaction);

}
