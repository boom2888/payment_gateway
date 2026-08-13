package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.SaasUserCorporationSupportedCountryMapper;
import com.games.payment.domain.SaasUserCorporationSupportedCountry;
import com.games.payment.service.ISaasUserCorporationSupportedCountryService;

import java.util.List;
import java.util.Map;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-06
 */
@Service
public class SaasUserCorporationSupportedCountryServiceImpl extends ServiceImpl<SaasUserCorporationSupportedCountryMapper, SaasUserCorporationSupportedCountry> implements ISaasUserCorporationSupportedCountryService {

    /**
     * 查询【请填写功能名称】列表
     *
     * @param saasUserCorporationSupportedCountry 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<SaasUserCorporationSupportedCountry> selectAllList(SaasUserCorporationSupportedCountry saasUserCorporationSupportedCountry)
    {
        return getBaseMapper().selectAllList(saasUserCorporationSupportedCountry);
    }


    @Override
    public List<SaasUserCorporationSupportedCountry> queryList(SaasUserCorporationSupportedCountry saasUserCorporationSupportedCountry) {
        LambdaQueryWrapper<SaasUserCorporationSupportedCountry> lqw = Wrappers.lambdaQuery();
        if (saasUserCorporationSupportedCountry.getSaasUserCorporationId() != null){
            lqw.eq(SaasUserCorporationSupportedCountry::getSaasUserCorporationId ,saasUserCorporationSupportedCountry.getSaasUserCorporationId());
        }
        if (StringUtils.isNotBlank(saasUserCorporationSupportedCountry.getCountryCode())){
            lqw.eq(SaasUserCorporationSupportedCountry::getCountryCode ,saasUserCorporationSupportedCountry.getCountryCode());
        }
        if (saasUserCorporationSupportedCountry.getCreatedAt() != null){
            lqw.eq(SaasUserCorporationSupportedCountry::getCreatedAt ,saasUserCorporationSupportedCountry.getCreatedAt());
        }
        if (saasUserCorporationSupportedCountry.getCreatedBy() != null){
            lqw.eq(SaasUserCorporationSupportedCountry::getCreatedBy ,saasUserCorporationSupportedCountry.getCreatedBy());
        }
        if (saasUserCorporationSupportedCountry.getDeletedAt() != null){
            lqw.eq(SaasUserCorporationSupportedCountry::getDeletedAt ,saasUserCorporationSupportedCountry.getDeletedAt());
        }
        if (saasUserCorporationSupportedCountry.getDeletedBy() != null){
            lqw.eq(SaasUserCorporationSupportedCountry::getDeletedBy ,saasUserCorporationSupportedCountry.getDeletedBy());
        }
        if (saasUserCorporationSupportedCountry.getDeleted() != null){
            lqw.eq(SaasUserCorporationSupportedCountry::getDeleted ,saasUserCorporationSupportedCountry.getDeleted());
        }
        return this.list(lqw);
    }




}
