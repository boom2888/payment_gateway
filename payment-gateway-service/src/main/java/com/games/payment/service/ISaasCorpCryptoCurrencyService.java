package com.games.payment.service;

import com.games.payment.domain.SaasCorpCryptoCurrency;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * SaaS用户公司支持加密货币Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface ISaasCorpCryptoCurrencyService extends IService<SaasCorpCryptoCurrency> {

    /**
     * 查询SaaS用户公司支持加密货币列表
     *
     * @param saasCorpCryptoCurrency SaaS用户公司支持加密货币
     * @return SaaS用户公司支持加密货币集合
     */
    List<SaasCorpCryptoCurrency> selectAllList(SaasCorpCryptoCurrency saasCorpCryptoCurrency);

    /**
     * 查询列表
     */
    List<SaasCorpCryptoCurrency> queryList(SaasCorpCryptoCurrency saasCorpCryptoCurrency);

}
