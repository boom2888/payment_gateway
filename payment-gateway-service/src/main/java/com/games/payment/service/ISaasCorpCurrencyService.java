package com.games.payment.service;

import com.games.payment.domain.SaasCorpCurrency;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * SaaS用户公司支持货币Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface ISaasCorpCurrencyService extends IService<SaasCorpCurrency> {

    /**
     * 查询SaaS用户公司支持货币列表
     *
     * @param saasCorpCurrency SaaS用户公司支持货币
     * @return SaaS用户公司支持货币集合
     */
    List<SaasCorpCurrency> selectAllList(SaasCorpCurrency saasCorpCurrency);

    /**
     * 查询列表
     */
    List<SaasCorpCurrency> queryList(SaasCorpCurrency saasCorpCurrency);

}
