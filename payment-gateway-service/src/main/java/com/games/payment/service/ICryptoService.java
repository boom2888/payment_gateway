package com.games.payment.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.games.payment.domain.Crypto;

import java.util.List;

/**
 * 加密货币Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface ICryptoService extends IService<Crypto> {
    List<Crypto> getUseAbleList();
    /**
     * 查询加密货币列表
     *
     * @param cryptoCurrency 加密货币
     * @return 加密货币集合
     */
    List<Crypto> selectAllList(Crypto cryptoCurrency);

    /**
     * 查询列表
     */
    List<Crypto> queryList(Crypto cryptoCurrency);

    Crypto getBySymbol(String cryptoCurrency);
}
