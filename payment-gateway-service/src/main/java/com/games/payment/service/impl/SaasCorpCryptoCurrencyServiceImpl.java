package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.SaasCorpCryptoCurrencyMapper;
import com.games.payment.domain.SaasCorpCryptoCurrency;
import com.games.payment.service.ISaasCorpCryptoCurrencyService;

import java.util.List;
import java.util.Map;

/**
 * SaaS用户公司支持加密货币Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class SaasCorpCryptoCurrencyServiceImpl extends ServiceImpl<SaasCorpCryptoCurrencyMapper, SaasCorpCryptoCurrency> implements ISaasCorpCryptoCurrencyService {

    /**
     * 查询SaaS用户公司支持加密货币列表
     *
     * @param saasCorpCryptoCurrency SaaS用户公司支持加密货币
     * @return SaaS用户公司支持加密货币
     */
    @Override
    public List<SaasCorpCryptoCurrency> selectAllList(SaasCorpCryptoCurrency saasCorpCryptoCurrency)
    {
        return getBaseMapper().selectAllList(saasCorpCryptoCurrency);
    }


    @Override
    public List<SaasCorpCryptoCurrency> queryList(SaasCorpCryptoCurrency saasCorpCryptoCurrency) {
        LambdaQueryWrapper<SaasCorpCryptoCurrency> lqw = Wrappers.lambdaQuery();
        if (saasCorpCryptoCurrency.getSaasUserCorporationId() != null){
            lqw.eq(SaasCorpCryptoCurrency::getSaasUserCorporationId ,saasCorpCryptoCurrency.getSaasUserCorporationId());
        }
        if (saasCorpCryptoCurrency.getCryptoCurrencyId() != null){
            lqw.eq(SaasCorpCryptoCurrency::getCryptoCurrencyId ,saasCorpCryptoCurrency.getCryptoCurrencyId());
        }
        if (saasCorpCryptoCurrency.getCreatedAt() != null){
            lqw.eq(SaasCorpCryptoCurrency::getCreatedAt ,saasCorpCryptoCurrency.getCreatedAt());
        }
        if (saasCorpCryptoCurrency.getCreatedBy() != null){
            lqw.eq(SaasCorpCryptoCurrency::getCreatedBy ,saasCorpCryptoCurrency.getCreatedBy());
        }
        if (saasCorpCryptoCurrency.getDeletedAt() != null){
            lqw.eq(SaasCorpCryptoCurrency::getDeletedAt ,saasCorpCryptoCurrency.getDeletedAt());
        }
        if (saasCorpCryptoCurrency.getDeletedBy() != null){
            lqw.eq(SaasCorpCryptoCurrency::getDeletedBy ,saasCorpCryptoCurrency.getDeletedBy());
        }
        if (saasCorpCryptoCurrency.getDeleted() != null){
            lqw.eq(SaasCorpCryptoCurrency::getDeleted ,saasCorpCryptoCurrency.getDeleted());
        }
        return this.list(lqw);
    }




}
