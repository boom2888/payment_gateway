package com.games.payment.mapper;

import com.games.payment.domain.SaasCorpCurrency;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * SaaS用户公司支持货币Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface SaasCorpCurrencyMapper extends BaseMapper<SaasCorpCurrency> {
    /**
     * 查询SaaS用户公司支持货币列表
     *
     * @param saasCorpCurrency SaaS用户公司支持货币
     * @return SaaS用户公司支持货币集合
     */
    List<SaasCorpCurrency> selectAllList(SaasCorpCurrency saasCorpCurrency);

}
