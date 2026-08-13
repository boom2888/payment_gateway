package com.games.payment.mapper;

import com.games.payment.domain.ConfigRiskLevel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 风险等级配置Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface ConfigRiskLevelMapper extends BaseMapper<ConfigRiskLevel> {
    /**
     * 查询风险等级配置列表
     *
     * @param configRiskLevel 风险等级配置
     * @return 风险等级配置集合
     */
    List<ConfigRiskLevel> selectAllList(ConfigRiskLevel configRiskLevel);

}
