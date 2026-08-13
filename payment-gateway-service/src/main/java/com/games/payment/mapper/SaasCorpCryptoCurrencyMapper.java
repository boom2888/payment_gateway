package com.games.payment.mapper;

import com.games.payment.domain.SaasCorpCryptoCurrency;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * SaaS用户公司支持加密货币Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface SaasCorpCryptoCurrencyMapper extends BaseMapper<SaasCorpCryptoCurrency> {
    /**
     * 查询SaaS用户公司支持加密货币列表
     *
     * @param saasCorpCryptoCurrency SaaS用户公司支持加密货币
     * @return SaaS用户公司支持加密货币集合
     */
    List<SaasCorpCryptoCurrency> selectAllList(SaasCorpCryptoCurrency saasCorpCryptoCurrency);

}
