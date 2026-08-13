package com.games.payment.mapper;

import com.games.payment.domain.SaasCorpOutCurrency;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * SaaS用户公司出金支持货币Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface SaasCorpOutCurrencyMapper extends BaseMapper<SaasCorpOutCurrency> {
    /**
     * 查询SaaS用户公司出金支持货币列表
     *
     * @param saasCorpOutCurrency SaaS用户公司出金支持货币
     * @return SaaS用户公司出金支持货币集合
     */
    List<SaasCorpOutCurrency> selectAllList(SaasCorpOutCurrency saasCorpOutCurrency);

}
