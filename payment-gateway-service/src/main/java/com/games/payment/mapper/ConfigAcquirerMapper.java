package com.games.payment.mapper;

import com.games.payment.domain.ConfigAcquirer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 收单机构配置Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface ConfigAcquirerMapper extends BaseMapper<ConfigAcquirer> {
    /**
     * 查询收单机构配置列表
     *
     * @param configAcquirer 收单机构配置
     * @return 收单机构配置集合
     */
    List<ConfigAcquirer> selectAllList(ConfigAcquirer configAcquirer);

}
