package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.SaasCorpOutCurrencyMapper;
import com.games.payment.domain.SaasCorpOutCurrency;
import com.games.payment.service.ISaasCorpOutCurrencyService;

import java.util.List;
import java.util.Map;

/**
 * SaaS用户公司出金支持货币Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class SaasCorpOutCurrencyServiceImpl extends ServiceImpl<SaasCorpOutCurrencyMapper, SaasCorpOutCurrency> implements ISaasCorpOutCurrencyService {

    /**
     * 查询SaaS用户公司出金支持货币列表
     *
     * @param saasCorpOutCurrency SaaS用户公司出金支持货币
     * @return SaaS用户公司出金支持货币
     */
    @Override
    public List<SaasCorpOutCurrency> selectAllList(SaasCorpOutCurrency saasCorpOutCurrency)
    {
        return getBaseMapper().selectAllList(saasCorpOutCurrency);
    }


    @Override
    public List<SaasCorpOutCurrency> queryList(SaasCorpOutCurrency saasCorpOutCurrency) {
        LambdaQueryWrapper<SaasCorpOutCurrency> lqw = Wrappers.lambdaQuery();
        if (saasCorpOutCurrency.getSaasUserCorporationId() != null){
            lqw.eq(SaasCorpOutCurrency::getSaasUserCorporationId ,saasCorpOutCurrency.getSaasUserCorporationId());
        }
        if (saasCorpOutCurrency.getCurrencyId() != null){
            lqw.eq(SaasCorpOutCurrency::getCurrencyId ,saasCorpOutCurrency.getCurrencyId());
        }
        if (saasCorpOutCurrency.getIsBasicCurrency() != null){
            lqw.eq(SaasCorpOutCurrency::getIsBasicCurrency ,saasCorpOutCurrency.getIsBasicCurrency());
        }
        if (StringUtils.isNotBlank(saasCorpOutCurrency.getCurrencyAccountCheckout())){
            lqw.eq(SaasCorpOutCurrency::getCurrencyAccountCheckout ,saasCorpOutCurrency.getCurrencyAccountCheckout());
        }
        if (saasCorpOutCurrency.getCreatedAt() != null){
            lqw.eq(SaasCorpOutCurrency::getCreatedAt ,saasCorpOutCurrency.getCreatedAt());
        }
        if (saasCorpOutCurrency.getCreatedBy() != null){
            lqw.eq(SaasCorpOutCurrency::getCreatedBy ,saasCorpOutCurrency.getCreatedBy());
        }
        if (saasCorpOutCurrency.getDeletedAt() != null){
            lqw.eq(SaasCorpOutCurrency::getDeletedAt ,saasCorpOutCurrency.getDeletedAt());
        }
        if (saasCorpOutCurrency.getDeletedBy() != null){
            lqw.eq(SaasCorpOutCurrency::getDeletedBy ,saasCorpOutCurrency.getDeletedBy());
        }
        if (saasCorpOutCurrency.getDeleted() != null){
            lqw.eq(SaasCorpOutCurrency::getDeleted ,saasCorpOutCurrency.getDeleted());
        }
        return this.list(lqw);
    }




}
