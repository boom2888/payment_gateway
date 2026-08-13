package com.games.payment.mapper;

import com.games.payment.domain.ConfigOrderStatus;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 订单状态配置Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface ConfigOrderStatusMapper extends BaseMapper<ConfigOrderStatus> {
    /**
     * 查询订单状态配置列表
     *
     * @param configOrderStatus 订单状态配置
     * @return 订单状态配置集合
     */
    List<ConfigOrderStatus> selectAllList(ConfigOrderStatus configOrderStatus);

}
