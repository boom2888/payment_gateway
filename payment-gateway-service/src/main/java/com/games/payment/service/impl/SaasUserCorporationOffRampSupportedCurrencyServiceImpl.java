package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.SaasUserCorporationOffRampSupportedCurrencyMapper;
import com.games.payment.domain.SaasUserCorporationOffRampSupportedCurrency;
import com.games.payment.service.ISaasUserCorporationOffRampSupportedCurrencyService;

import java.util.List;
import java.util.Map;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-06
 */
@Service
public class SaasUserCorporationOffRampSupportedCurrencyServiceImpl extends ServiceImpl<SaasUserCorporationOffRampSupportedCurrencyMapper, SaasUserCorporationOffRampSupportedCurrency> implements ISaasUserCorporationOffRampSupportedCurrencyService {

    /**
     * 查询【请填写功能名称】列表
     *
     * @param saasUserCorporationOffRampSupportedCurrency 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<SaasUserCorporationOffRampSupportedCurrency> selectAllList(SaasUserCorporationOffRampSupportedCurrency saasUserCorporationOffRampSupportedCurrency)
    {
        return getBaseMapper().selectAllList(saasUserCorporationOffRampSupportedCurrency);
    }


    @Override
    public List<SaasUserCorporationOffRampSupportedCurrency> queryList(SaasUserCorporationOffRampSupportedCurrency saasUserCorporationOffRampSupportedCurrency) {
        LambdaQueryWrapper<SaasUserCorporationOffRampSupportedCurrency> lqw = Wrappers.lambdaQuery();
        if (saasUserCorporationOffRampSupportedCurrency.getSaasUserCorporationId() != null){
            lqw.eq(SaasUserCorporationOffRampSupportedCurrency::getSaasUserCorporationId ,saasUserCorporationOffRampSupportedCurrency.getSaasUserCorporationId());
        }
        if (saasUserCorporationOffRampSupportedCurrency.getCurrencyId() != null){
            lqw.eq(SaasUserCorporationOffRampSupportedCurrency::getCurrencyId ,saasUserCorporationOffRampSupportedCurrency.getCurrencyId());
        }
        if (saasUserCorporationOffRampSupportedCurrency.getIsBasicCurrency() != null){
            lqw.eq(SaasUserCorporationOffRampSupportedCurrency::getIsBasicCurrency ,saasUserCorporationOffRampSupportedCurrency.getIsBasicCurrency());
        }
        if (StringUtils.isNotBlank(saasUserCorporationOffRampSupportedCurrency.getCurrencyAccountCheckout())){
            lqw.eq(SaasUserCorporationOffRampSupportedCurrency::getCurrencyAccountCheckout ,saasUserCorporationOffRampSupportedCurrency.getCurrencyAccountCheckout());
        }
        if (saasUserCorporationOffRampSupportedCurrency.getCreatedAt() != null){
            lqw.eq(SaasUserCorporationOffRampSupportedCurrency::getCreatedAt ,saasUserCorporationOffRampSupportedCurrency.getCreatedAt());
        }
        if (saasUserCorporationOffRampSupportedCurrency.getCreatedBy() != null){
            lqw.eq(SaasUserCorporationOffRampSupportedCurrency::getCreatedBy ,saasUserCorporationOffRampSupportedCurrency.getCreatedBy());
        }
        if (saasUserCorporationOffRampSupportedCurrency.getDeletedAt() != null){
            lqw.eq(SaasUserCorporationOffRampSupportedCurrency::getDeletedAt ,saasUserCorporationOffRampSupportedCurrency.getDeletedAt());
        }
        if (saasUserCorporationOffRampSupportedCurrency.getDeletedBy() != null){
            lqw.eq(SaasUserCorporationOffRampSupportedCurrency::getDeletedBy ,saasUserCorporationOffRampSupportedCurrency.getDeletedBy());
        }
        if (saasUserCorporationOffRampSupportedCurrency.getDeleted() != null){
            lqw.eq(SaasUserCorporationOffRampSupportedCurrency::getDeleted ,saasUserCorporationOffRampSupportedCurrency.getDeleted());
        }
        return this.list(lqw);
    }




}
