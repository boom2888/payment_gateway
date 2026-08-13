package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.SaasUserCorporationSupportedCurrencyMapper;
import com.games.payment.domain.SaasUserCorporationSupportedCurrency;
import com.games.payment.service.ISaasUserCorporationSupportedCurrencyService;

import java.util.List;
import java.util.Map;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-06
 */
@Service
public class SaasUserCorporationSupportedCurrencyServiceImpl extends ServiceImpl<SaasUserCorporationSupportedCurrencyMapper, SaasUserCorporationSupportedCurrency> implements ISaasUserCorporationSupportedCurrencyService {

    /**
     * 查询【请填写功能名称】列表
     *
     * @param saasUserCorporationSupportedCurrency 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<SaasUserCorporationSupportedCurrency> selectAllList(SaasUserCorporationSupportedCurrency saasUserCorporationSupportedCurrency)
    {
        return getBaseMapper().selectAllList(saasUserCorporationSupportedCurrency);
    }


    @Override
    public List<SaasUserCorporationSupportedCurrency> queryList(SaasUserCorporationSupportedCurrency saasUserCorporationSupportedCurrency) {
        LambdaQueryWrapper<SaasUserCorporationSupportedCurrency> lqw = Wrappers.lambdaQuery();
        if (saasUserCorporationSupportedCurrency.getSaasUserCorporationId() != null){
            lqw.eq(SaasUserCorporationSupportedCurrency::getSaasUserCorporationId ,saasUserCorporationSupportedCurrency.getSaasUserCorporationId());
        }
        if (saasUserCorporationSupportedCurrency.getCurrencyId() != null){
            lqw.eq(SaasUserCorporationSupportedCurrency::getCurrencyId ,saasUserCorporationSupportedCurrency.getCurrencyId());
        }
        if (saasUserCorporationSupportedCurrency.getIsBasicCurrency() != null){
            lqw.eq(SaasUserCorporationSupportedCurrency::getIsBasicCurrency ,saasUserCorporationSupportedCurrency.getIsBasicCurrency());
        }
        if (saasUserCorporationSupportedCurrency.getCreatedAt() != null){
            lqw.eq(SaasUserCorporationSupportedCurrency::getCreatedAt ,saasUserCorporationSupportedCurrency.getCreatedAt());
        }
        if (saasUserCorporationSupportedCurrency.getCreatedBy() != null){
            lqw.eq(SaasUserCorporationSupportedCurrency::getCreatedBy ,saasUserCorporationSupportedCurrency.getCreatedBy());
        }
        if (saasUserCorporationSupportedCurrency.getDeletedAt() != null){
            lqw.eq(SaasUserCorporationSupportedCurrency::getDeletedAt ,saasUserCorporationSupportedCurrency.getDeletedAt());
        }
        if (saasUserCorporationSupportedCurrency.getDeletedBy() != null){
            lqw.eq(SaasUserCorporationSupportedCurrency::getDeletedBy ,saasUserCorporationSupportedCurrency.getDeletedBy());
        }
        if (saasUserCorporationSupportedCurrency.getDeleted() != null){
            lqw.eq(SaasUserCorporationSupportedCurrency::getDeleted ,saasUserCorporationSupportedCurrency.getDeleted());
        }
        if (StringUtils.isNotBlank(saasUserCorporationSupportedCurrency.getCurrencyAccountCheckout())){
            lqw.eq(SaasUserCorporationSupportedCurrency::getCurrencyAccountCheckout ,saasUserCorporationSupportedCurrency.getCurrencyAccountCheckout());
        }
        return this.list(lqw);
    }




}
