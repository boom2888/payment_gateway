package com.games.payment.service;

import com.games.payment.domain.OrderUserInfo;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 订单用户信息Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface IOrderUserInfoService extends IService<OrderUserInfo> {

    /**
     * 查询订单用户信息列表
     *
     * @param orderUserInfo 订单用户信息
     * @return 订单用户信息集合
     */
    List<OrderUserInfo> selectAllList(OrderUserInfo orderUserInfo);

    /**
     * 查询列表
     */
    List<OrderUserInfo> queryList(OrderUserInfo orderUserInfo);

}
