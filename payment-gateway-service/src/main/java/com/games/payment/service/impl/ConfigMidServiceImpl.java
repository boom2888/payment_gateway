package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.ConfigMidMapper;
import com.games.payment.domain.ConfigMid;
import com.games.payment.service.IConfigMidService;

import java.util.List;
import java.util.Map;

/**
 * MID配置Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class ConfigMidServiceImpl extends ServiceImpl<ConfigMidMapper, ConfigMid> implements IConfigMidService {

    /**
     * 查询MID配置列表
     *
     * @param configMid MID配置
     * @return MID配置
     */
    @Override
    public List<ConfigMid> selectAllList(ConfigMid configMid)
    {
        return getBaseMapper().selectAllList(configMid);
    }


    @Override
    public List<ConfigMid> queryList(ConfigMid configMid) {
        LambdaQueryWrapper<ConfigMid> lqw = Wrappers.lambdaQuery();
        if (configMid.getAcquirerId() != null){
            lqw.eq(ConfigMid::getAcquirerId ,configMid.getAcquirerId());
        }
        if (StringUtils.isNotBlank(configMid.getMidCode())){
            lqw.eq(ConfigMid::getMidCode ,configMid.getMidCode());
        }
        if (StringUtils.isNotBlank(configMid.getDescription())){
            lqw.eq(ConfigMid::getDescription ,configMid.getDescription());
        }
        if (configMid.getSettlementCurrencyId() != null){
            lqw.eq(ConfigMid::getSettlementCurrencyId ,configMid.getSettlementCurrencyId());
        }
        if (configMid.getSettlementCurrencyType() != null){
            lqw.eq(ConfigMid::getSettlementCurrencyType ,configMid.getSettlementCurrencyType());
        }
        if (configMid.getStatus() != null){
            lqw.eq(ConfigMid::getStatus ,configMid.getStatus());
        }
        if (StringUtils.isNotBlank(configMid.getSiteId())){
            lqw.eq(ConfigMid::getSiteId ,configMid.getSiteId());
        }
        if (StringUtils.isNotBlank(configMid.getSiteName())){
            lqw.like(ConfigMid::getSiteName ,configMid.getSiteName());
        }
        if (StringUtils.isNotBlank(configMid.getClientId())){
            lqw.eq(ConfigMid::getClientId ,configMid.getClientId());
        }
        if (StringUtils.isNotBlank(configMid.getClientName())){
            lqw.like(ConfigMid::getClientName ,configMid.getClientName());
        }
        if (StringUtils.isNotBlank(configMid.getAcquirerMerchantId())){
            lqw.eq(ConfigMid::getAcquirerMerchantId ,configMid.getAcquirerMerchantId());
        }
        if (configMid.getType() != null){
            lqw.eq(ConfigMid::getType ,configMid.getType());
        }
        return this.list(lqw);
    }




}
