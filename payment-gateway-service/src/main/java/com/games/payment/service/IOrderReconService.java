package com.games.payment.service;

import com.games.payment.domain.OrderRecon;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 订单对账Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface IOrderReconService extends IService<OrderRecon> {

    /**
     * 查询订单对账列表
     *
     * @param orderRecon 订单对账
     * @return 订单对账集合
     */
    List<OrderRecon> selectAllList(OrderRecon orderRecon);

    /**
     * 查询列表
     */
    List<OrderRecon> queryList(OrderRecon orderRecon);

}
