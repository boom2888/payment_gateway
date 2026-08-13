package com.games.payment.service;

import com.games.payment.domain.SaasCorpOutCurrency;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * SaaS用户公司出金支持货币Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface ISaasCorpOutCurrencyService extends IService<SaasCorpOutCurrency> {

    /**
     * 查询SaaS用户公司出金支持货币列表
     *
     * @param saasCorpOutCurrency SaaS用户公司出金支持货币
     * @return SaaS用户公司出金支持货币集合
     */
    List<SaasCorpOutCurrency> selectAllList(SaasCorpOutCurrency saasCorpOutCurrency);

    /**
     * 查询列表
     */
    List<SaasCorpOutCurrency> queryList(SaasCorpOutCurrency saasCorpOutCurrency);

}
