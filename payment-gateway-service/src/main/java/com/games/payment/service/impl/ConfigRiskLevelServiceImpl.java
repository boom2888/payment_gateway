package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.ConfigRiskLevelMapper;
import com.games.payment.domain.ConfigRiskLevel;
import com.games.payment.service.IConfigRiskLevelService;

import java.util.List;
import java.util.Map;

/**
 * 风险等级配置Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class ConfigRiskLevelServiceImpl extends ServiceImpl<ConfigRiskLevelMapper, ConfigRiskLevel> implements IConfigRiskLevelService {

    /**
     * 查询风险等级配置列表
     *
     * @param configRiskLevel 风险等级配置
     * @return 风险等级配置
     */
    @Override
    public List<ConfigRiskLevel> selectAllList(ConfigRiskLevel configRiskLevel)
    {
        return getBaseMapper().selectAllList(configRiskLevel);
    }


    @Override
    public List<ConfigRiskLevel> queryList(ConfigRiskLevel configRiskLevel) {
        LambdaQueryWrapper<ConfigRiskLevel> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(configRiskLevel.getName())){
            lqw.like(ConfigRiskLevel::getName ,configRiskLevel.getName());
        }
        return this.list(lqw);
    }




}
