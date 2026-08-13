package com.games.payment.mapper;

import com.games.payment.domain.ConfigContinent;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 大洲配置Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface ConfigContinentMapper extends BaseMapper<ConfigContinent> {
    /**
     * 查询大洲配置列表
     *
     * @param configContinent 大洲配置
     * @return 大洲配置集合
     */
    List<ConfigContinent> selectAllList(ConfigContinent configContinent);

}
