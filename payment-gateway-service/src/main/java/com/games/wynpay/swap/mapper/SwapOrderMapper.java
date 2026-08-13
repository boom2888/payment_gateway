package com.games.wynpay.swap.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.games.wynpay.swap.domain.SwapOrder;

import java.util.List;
/**
 * Swap订单Mapper接口
 *
 * @author Minter
 * @date 2025-02-17
 */
public interface SwapOrderMapper extends BaseMapper<SwapOrder> {
    /**
     * 查询Swap订单列表
     *
     * @param swapOrder Swap订单
     * @return Swap订单集合
     */
    List<SwapOrder> selectAllList(SwapOrder swapOrder);

}
