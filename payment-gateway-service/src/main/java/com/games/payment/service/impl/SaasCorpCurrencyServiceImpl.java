package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.SaasCorpCurrencyMapper;
import com.games.payment.domain.SaasCorpCurrency;
import com.games.payment.service.ISaasCorpCurrencyService;

import java.util.List;
import java.util.Map;

/**
 * SaaS用户公司支持货币Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class SaasCorpCurrencyServiceImpl extends ServiceImpl<SaasCorpCurrencyMapper, SaasCorpCurrency> implements ISaasCorpCurrencyService {

    /**
     * 查询SaaS用户公司支持货币列表
     *
     * @param saasCorpCurrency SaaS用户公司支持货币
     * @return SaaS用户公司支持货币
     */
    @Override
    public List<SaasCorpCurrency> selectAllList(SaasCorpCurrency saasCorpCurrency)
    {
        return getBaseMapper().selectAllList(saasCorpCurrency);
    }


    @Override
    public List<SaasCorpCurrency> queryList(SaasCorpCurrency saasCorpCurrency) {
        LambdaQueryWrapper<SaasCorpCurrency> lqw = Wrappers.lambdaQuery();
        if (saasCorpCurrency.getSaasUserCorporationId() != null){
            lqw.eq(SaasCorpCurrency::getSaasUserCorporationId ,saasCorpCurrency.getSaasUserCorporationId());
        }
        if (saasCorpCurrency.getCurrencyId() != null){
            lqw.eq(SaasCorpCurrency::getCurrencyId ,saasCorpCurrency.getCurrencyId());
        }
        if (saasCorpCurrency.getIsBasicCurrency() != null){
            lqw.eq(SaasCorpCurrency::getIsBasicCurrency ,saasCorpCurrency.getIsBasicCurrency());
        }
        if (saasCorpCurrency.getCreatedAt() != null){
            lqw.eq(SaasCorpCurrency::getCreatedAt ,saasCorpCurrency.getCreatedAt());
        }
        if (saasCorpCurrency.getCreatedBy() != null){
            lqw.eq(SaasCorpCurrency::getCreatedBy ,saasCorpCurrency.getCreatedBy());
        }
        if (saasCorpCurrency.getDeletedAt() != null){
            lqw.eq(SaasCorpCurrency::getDeletedAt ,saasCorpCurrency.getDeletedAt());
        }
        if (saasCorpCurrency.getDeletedBy() != null){
            lqw.eq(SaasCorpCurrency::getDeletedBy ,saasCorpCurrency.getDeletedBy());
        }
        if (saasCorpCurrency.getDeleted() != null){
            lqw.eq(SaasCorpCurrency::getDeleted ,saasCorpCurrency.getDeleted());
        }
        if (StringUtils.isNotBlank(saasCorpCurrency.getCurrencyAccountCheckout())){
            lqw.eq(SaasCorpCurrency::getCurrencyAccountCheckout ,saasCorpCurrency.getCurrencyAccountCheckout());
        }
        return this.list(lqw);
    }




}
