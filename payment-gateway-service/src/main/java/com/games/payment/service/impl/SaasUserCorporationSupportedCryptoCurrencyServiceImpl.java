package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.SaasUserCorporationSupportedCryptoCurrencyMapper;
import com.games.payment.domain.SaasUserCorporationSupportedCryptoCurrency;
import com.games.payment.service.ISaasUserCorporationSupportedCryptoCurrencyService;

import java.util.List;
import java.util.Map;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-06
 */
@Service
public class SaasUserCorporationSupportedCryptoCurrencyServiceImpl extends ServiceImpl<SaasUserCorporationSupportedCryptoCurrencyMapper, SaasUserCorporationSupportedCryptoCurrency> implements ISaasUserCorporationSupportedCryptoCurrencyService {

    /**
     * 查询【请填写功能名称】列表
     *
     * @param saasUserCorporationSupportedCryptoCurrency 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<SaasUserCorporationSupportedCryptoCurrency> selectAllList(SaasUserCorporationSupportedCryptoCurrency saasUserCorporationSupportedCryptoCurrency)
    {
        return getBaseMapper().selectAllList(saasUserCorporationSupportedCryptoCurrency);
    }


    @Override
    public List<SaasUserCorporationSupportedCryptoCurrency> queryList(SaasUserCorporationSupportedCryptoCurrency saasUserCorporationSupportedCryptoCurrency) {
        LambdaQueryWrapper<SaasUserCorporationSupportedCryptoCurrency> lqw = Wrappers.lambdaQuery();
        if (saasUserCorporationSupportedCryptoCurrency.getSaasUserCorporationId() != null){
            lqw.eq(SaasUserCorporationSupportedCryptoCurrency::getSaasUserCorporationId ,saasUserCorporationSupportedCryptoCurrency.getSaasUserCorporationId());
        }
        if (saasUserCorporationSupportedCryptoCurrency.getCryptoCurrencyId() != null){
            lqw.eq(SaasUserCorporationSupportedCryptoCurrency::getCryptoCurrencyId ,saasUserCorporationSupportedCryptoCurrency.getCryptoCurrencyId());
        }
        if (saasUserCorporationSupportedCryptoCurrency.getCreatedAt() != null){
            lqw.eq(SaasUserCorporationSupportedCryptoCurrency::getCreatedAt ,saasUserCorporationSupportedCryptoCurrency.getCreatedAt());
        }
        if (saasUserCorporationSupportedCryptoCurrency.getCreatedBy() != null){
            lqw.eq(SaasUserCorporationSupportedCryptoCurrency::getCreatedBy ,saasUserCorporationSupportedCryptoCurrency.getCreatedBy());
        }
        if (saasUserCorporationSupportedCryptoCurrency.getDeletedAt() != null){
            lqw.eq(SaasUserCorporationSupportedCryptoCurrency::getDeletedAt ,saasUserCorporationSupportedCryptoCurrency.getDeletedAt());
        }
        if (saasUserCorporationSupportedCryptoCurrency.getDeletedBy() != null){
            lqw.eq(SaasUserCorporationSupportedCryptoCurrency::getDeletedBy ,saasUserCorporationSupportedCryptoCurrency.getDeletedBy());
        }
        if (saasUserCorporationSupportedCryptoCurrency.getDeleted() != null){
            lqw.eq(SaasUserCorporationSupportedCryptoCurrency::getDeleted ,saasUserCorporationSupportedCryptoCurrency.getDeleted());
        }
        return this.list(lqw);
    }




}
