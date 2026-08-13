package com.games.payment.service;

import com.games.payment.domain.ConfigOrderStatus;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 订单状态配置Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface IConfigOrderStatusService extends IService<ConfigOrderStatus> {

    /**
     * 查询订单状态配置列表
     *
     * @param configOrderStatus 订单状态配置
     * @return 订单状态配置集合
     */
    List<ConfigOrderStatus> selectAllList(ConfigOrderStatus configOrderStatus);

    /**
     * 查询列表
     */
    List<ConfigOrderStatus> queryList(ConfigOrderStatus configOrderStatus);

}
