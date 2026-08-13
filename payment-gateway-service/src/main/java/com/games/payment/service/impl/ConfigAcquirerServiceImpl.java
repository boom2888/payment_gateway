package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.ConfigAcquirerMapper;
import com.games.payment.domain.ConfigAcquirer;
import com.games.payment.service.IConfigAcquirerService;

import java.util.List;
import java.util.Map;

/**
 * 收单机构配置Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class ConfigAcquirerServiceImpl extends ServiceImpl<ConfigAcquirerMapper, ConfigAcquirer> implements IConfigAcquirerService {

    /**
     * 查询收单机构配置列表
     *
     * @param configAcquirer 收单机构配置
     * @return 收单机构配置
     */
    @Override
    public List<ConfigAcquirer> selectAllList(ConfigAcquirer configAcquirer)
    {
        return getBaseMapper().selectAllList(configAcquirer);
    }


    @Override
    public List<ConfigAcquirer> queryList(ConfigAcquirer configAcquirer) {
        LambdaQueryWrapper<ConfigAcquirer> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(configAcquirer.getAcquirerName())){
            lqw.like(ConfigAcquirer::getAcquirerName ,configAcquirer.getAcquirerName());
        }
        if (configAcquirer.getStatus() != null){
            lqw.eq(ConfigAcquirer::getStatus ,configAcquirer.getStatus());
        }
        return this.list(lqw);
    }




}
