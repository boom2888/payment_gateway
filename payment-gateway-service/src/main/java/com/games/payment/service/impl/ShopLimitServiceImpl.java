package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.ShopLimitMapper;
import com.games.payment.domain.ShopLimit;
import com.games.payment.service.IShopLimitService;

import java.util.List;
import java.util.Map;

/**
 * 商户限额Service业务层处理
 *
 * @author Ticker
 * @date 2025-09-24
 */
@Service
public class ShopLimitServiceImpl extends ServiceImpl<ShopLimitMapper, ShopLimit> implements IShopLimitService {

    @Override
    public ShopLimit getByShopId(Long shopId) {
        LambdaQueryWrapper<ShopLimit> lqw = Wrappers.lambdaQuery();
        lqw.eq(ShopLimit::getShopId, shopId);
        List<ShopLimit> shopLimits = this.list(lqw);
        if (shopLimits != null && !shopLimits.isEmpty()) {
            return shopLimits.get(0);
        }
        return null;
    }

    /**
     * 查询商户限额列表
     *
     * @param shopLimit 商户限额
     * @return 商户限额
     */
    @Override
    public List<ShopLimit> selectAllList(ShopLimit shopLimit)
    {
        return getBaseMapper().selectAllList(shopLimit);
    }


    @Override
    public List<ShopLimit> queryList(ShopLimit shopLimit) {
        LambdaQueryWrapper<ShopLimit> lqw = Wrappers.lambdaQuery();
        if (shopLimit.getShopId() != null){
            lqw.eq(ShopLimit::getShopId ,shopLimit.getShopId());
        }
        if (StringUtils.isNotBlank(shopLimit.getStatus())){
            lqw.eq(ShopLimit::getStatus ,shopLimit.getStatus());
        }
        return this.list(lqw);
    }

    @Override
    public boolean existsByShopId(Long shopId) {
        if (shopId == null) return false;
        LambdaQueryWrapper<ShopLimit> lqw = Wrappers.lambdaQuery();
        lqw.eq(ShopLimit::getShopId, shopId).last("limit 1");
        return this.count(lqw) > 0;
    }

    @Override
    public boolean existsByShopIdExceptId(Long shopId, Long excludeId) {
        if (shopId == null) return false;
        LambdaQueryWrapper<ShopLimit> lqw = Wrappers.lambdaQuery();
        lqw.eq(ShopLimit::getShopId, shopId);
        if (excludeId != null) {
            lqw.ne(ShopLimit::getId, excludeId);
        }
        lqw.last("limit 1");
        return this.count(lqw) > 0;
    }




}
