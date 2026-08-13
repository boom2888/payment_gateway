package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.ConfigCountryMapper;
import com.games.payment.domain.ConfigCountry;
import com.games.payment.service.IConfigCountryService;

import java.util.List;
import java.util.Map;

/**
 * 国家配置Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class ConfigCountryServiceImpl extends ServiceImpl<ConfigCountryMapper, ConfigCountry> implements IConfigCountryService {

    /**
     * 查询国家配置列表
     *
     * @param configCountry 国家配置
     * @return 国家配置
     */
    @Override
    public List<ConfigCountry> selectAllList(ConfigCountry configCountry)
    {
        return getBaseMapper().selectAllList(configCountry);
    }


    @Override
    public List<ConfigCountry> queryList(ConfigCountry configCountry) {
        LambdaQueryWrapper<ConfigCountry> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(configCountry.getName())){
            lqw.like(ConfigCountry::getName ,configCountry.getName());
        }
        if (StringUtils.isNotBlank(configCountry.getFullName())){
            lqw.like(ConfigCountry::getFullName ,configCountry.getFullName());
        }
        if (StringUtils.isNotBlank(configCountry.getIso3())){
            lqw.eq(ConfigCountry::getIso3 ,configCountry.getIso3());
        }
        if (StringUtils.isNotBlank(configCountry.getNumber())){
            lqw.eq(ConfigCountry::getNumber ,configCountry.getNumber());
        }
        if (StringUtils.isNotBlank(configCountry.getContinentCode())){
            lqw.eq(ConfigCountry::getContinentCode ,configCountry.getContinentCode());
        }
        return this.list(lqw);
    }




}
