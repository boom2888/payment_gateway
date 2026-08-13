package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.ConfigContinentMapper;
import com.games.payment.domain.ConfigContinent;
import com.games.payment.service.IConfigContinentService;

import java.util.List;
import java.util.Map;

/**
 * 大洲配置Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class ConfigContinentServiceImpl extends ServiceImpl<ConfigContinentMapper, ConfigContinent> implements IConfigContinentService {

    /**
     * 查询大洲配置列表
     *
     * @param configContinent 大洲配置
     * @return 大洲配置
     */
    @Override
    public List<ConfigContinent> selectAllList(ConfigContinent configContinent)
    {
        return getBaseMapper().selectAllList(configContinent);
    }


    @Override
    public List<ConfigContinent> queryList(ConfigContinent configContinent) {
        LambdaQueryWrapper<ConfigContinent> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(configContinent.getName())){
            lqw.like(ConfigContinent::getName ,configContinent.getName());
        }
        return this.list(lqw);
    }




}
