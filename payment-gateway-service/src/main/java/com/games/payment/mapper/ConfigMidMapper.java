package com.games.payment.mapper;

import com.games.payment.domain.ConfigMid;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * MID配置Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface ConfigMidMapper extends BaseMapper<ConfigMid> {
    /**
     * 查询MID配置列表
     *
     * @param configMid MID配置
     * @return MID配置集合
     */
    List<ConfigMid> selectAllList(ConfigMid configMid);

}
