package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.SaasCorpCountryMapper;
import com.games.payment.domain.SaasCorpCountry;
import com.games.payment.service.ISaasCorpCountryService;

import java.util.List;
import java.util.Map;

/**
 * SaaS用户公司支持国家Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class SaasCorpCountryServiceImpl extends ServiceImpl<SaasCorpCountryMapper, SaasCorpCountry> implements ISaasCorpCountryService {

    /**
     * 查询SaaS用户公司支持国家列表
     *
     * @param saasCorpCountry SaaS用户公司支持国家
     * @return SaaS用户公司支持国家
     */
    @Override
    public List<SaasCorpCountry> selectAllList(SaasCorpCountry saasCorpCountry)
    {
        return getBaseMapper().selectAllList(saasCorpCountry);
    }


    @Override
    public List<SaasCorpCountry> queryList(SaasCorpCountry saasCorpCountry) {
        LambdaQueryWrapper<SaasCorpCountry> lqw = Wrappers.lambdaQuery();
        if (saasCorpCountry.getSaasUserCorporationId() != null){
            lqw.eq(SaasCorpCountry::getSaasUserCorporationId ,saasCorpCountry.getSaasUserCorporationId());
        }
        if (StringUtils.isNotBlank(saasCorpCountry.getCountryCode())){
            lqw.eq(SaasCorpCountry::getCountryCode ,saasCorpCountry.getCountryCode());
        }
        if (saasCorpCountry.getCreatedAt() != null){
            lqw.eq(SaasCorpCountry::getCreatedAt ,saasCorpCountry.getCreatedAt());
        }
        if (saasCorpCountry.getCreatedBy() != null){
            lqw.eq(SaasCorpCountry::getCreatedBy ,saasCorpCountry.getCreatedBy());
        }
        if (saasCorpCountry.getDeletedAt() != null){
            lqw.eq(SaasCorpCountry::getDeletedAt ,saasCorpCountry.getDeletedAt());
        }
        if (saasCorpCountry.getDeletedBy() != null){
            lqw.eq(SaasCorpCountry::getDeletedBy ,saasCorpCountry.getDeletedBy());
        }
        if (saasCorpCountry.getDeleted() != null){
            lqw.eq(SaasCorpCountry::getDeleted ,saasCorpCountry.getDeleted());
        }
        return this.list(lqw);
    }




}
