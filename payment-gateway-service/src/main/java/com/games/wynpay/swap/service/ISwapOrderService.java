package com.games.wynpay.swap.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.games.wynpay.swap.domain.SwapOrder;

import java.util.List;

/**
 * Swap订单Service接口
 *
 * @author Minter
 * @date 2025-02-17
 */
public interface ISwapOrderService extends IService<SwapOrder> {

    /**
     * 查询Swap订单列表
     *
     * @param swapOrder Swap订单
     * @return Swap订单集合
     */
    List<SwapOrder> selectAllList(SwapOrder swapOrder);

    /**
     * 查询列表
     */
    List<SwapOrder> queryList(SwapOrder swapOrder);

}
