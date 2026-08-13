package com.games.payment.service;

import com.games.payment.domain.SaasCorpCountry;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * SaaS用户公司支持国家Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface ISaasCorpCountryService extends IService<SaasCorpCountry> {

    /**
     * 查询SaaS用户公司支持国家列表
     *
     * @param saasCorpCountry SaaS用户公司支持国家
     * @return SaaS用户公司支持国家集合
     */
    List<SaasCorpCountry> selectAllList(SaasCorpCountry saasCorpCountry);

    /**
     * 查询列表
     */
    List<SaasCorpCountry> queryList(SaasCorpCountry saasCorpCountry);

}
