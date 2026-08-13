package com.games.wynpay.swap.service.impl;

import com.games.payment.domain.AccessToken;
import com.games.payment.mapper.AccessTokenMapper;
import com.games.payment.service.IAccessTokenService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;


import com.games.wynpay.swap.domain.SwapOrder;
import com.games.wynpay.swap.mapper.SwapOrderMapper;
import com.games.wynpay.swap.service.ISwapOrderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Swap订单Service业务层处理
 *
 * @author Minter
 * @date 2025-02-17
 */

@Service
public class SwapOrderServiceImpl extends ServiceImpl<SwapOrderMapper, SwapOrder> implements ISwapOrderService {

    /**
     * 查询Swap订单列表
     *
     * @param swapOrder Swap订单
     * @return Swap订单
     */
    @Override
    public List<SwapOrder> selectAllList(SwapOrder swapOrder)
    {
        return getBaseMapper().selectAllList(swapOrder);
    }


    @Override
    public List<SwapOrder> queryList(SwapOrder swapOrder) {
        LambdaQueryWrapper<SwapOrder> lqw = Wrappers.lambdaQuery();
        if (swapOrder.getShopId() != null){
            lqw.eq(SwapOrder::getShopId ,swapOrder.getShopId());
        }
        if (StringUtils.isNotBlank(swapOrder.getAddress())){
            lqw.eq(SwapOrder::getAddress ,swapOrder.getAddress());
        }
        if (StringUtils.isNotBlank(swapOrder.getBuySell())){
            lqw.eq(SwapOrder::getBuySell ,swapOrder.getBuySell());
        }
        if (StringUtils.isNotBlank(swapOrder.getContact())){
            lqw.eq(SwapOrder::getContact ,swapOrder.getContact());
        }
        if (StringUtils.isNotBlank(swapOrder.getState())){
            lqw.eq(SwapOrder::getState ,swapOrder.getState());
        }
        if (StringUtils.isNotBlank(swapOrder.getHash())){
            lqw.like(SwapOrder::getHash ,swapOrder.getHash());
        }
        return this.list(lqw);
    }




}
