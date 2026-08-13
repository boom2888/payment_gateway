package com.games.payment.service;

import com.games.payment.domain.ConfigRiskLevel;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 风险等级配置Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface IConfigRiskLevelService extends IService<ConfigRiskLevel> {

    /**
     * 查询风险等级配置列表
     *
     * @param configRiskLevel 风险等级配置
     * @return 风险等级配置集合
     */
    List<ConfigRiskLevel> selectAllList(ConfigRiskLevel configRiskLevel);

    /**
     * 查询列表
     */
    List<ConfigRiskLevel> queryList(ConfigRiskLevel configRiskLevel);

}
