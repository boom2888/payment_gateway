package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.EmiOrderManagementMapper;
import com.games.payment.domain.EmiOrderManagement;
import com.games.payment.service.IEmiOrderManagementService;

import java.util.List;

/**
 * 订单管理Service业务层处理
 *
 * @author Ticker
 * @date 2025-10-16
 */
@Service
public class EmiOrderManagementServiceImpl extends ServiceImpl<EmiOrderManagementMapper, EmiOrderManagement> implements IEmiOrderManagementService {

    /**
     * 查询订单管理列表
     *
     * @param emiOrderManagement 订单管理
     * @return 订单管理
     */
    @Override
    public List<EmiOrderManagement> selectAllList(EmiOrderManagement emiOrderManagement)
    {
        return getBaseMapper().selectAllList(emiOrderManagement);
    }


    @Override
    public List<EmiOrderManagement> queryList(EmiOrderManagement emiOrderManagement) {
        LambdaQueryWrapper<EmiOrderManagement> lqw = Wrappers.lambdaQuery();
        // 时间范围过滤（优先 between；仅有一端则 ge / le）
        if (emiOrderManagement.getBeginTime() != null && emiOrderManagement.getEndTime() != null) {
            lqw.between(EmiOrderManagement::getOrderTime, emiOrderManagement.getBeginTime(), emiOrderManagement.getEndTime());
        } else if (emiOrderManagement.getBeginTime() != null) {
            lqw.ge(EmiOrderManagement::getOrderTime, emiOrderManagement.getBeginTime());
        } else if (emiOrderManagement.getEndTime() != null) {
            lqw.le(EmiOrderManagement::getOrderTime, emiOrderManagement.getEndTime());
        } else if (emiOrderManagement.getOrderTime() != null) {
            lqw.eq(EmiOrderManagement::getOrderTime, emiOrderManagement.getOrderTime());
        }
        if (StringUtils.isNotBlank(emiOrderManagement.getOrderNumber())){
            lqw.eq(EmiOrderManagement::getOrderNumber ,emiOrderManagement.getOrderNumber());
        }
        if (StringUtils.isNotBlank(emiOrderManagement.getOrderAmount())){
            lqw.eq(EmiOrderManagement::getOrderAmount ,emiOrderManagement.getOrderAmount());
        }
        if (StringUtils.isNotBlank(emiOrderManagement.getRemainingBalance())){
            lqw.eq(EmiOrderManagement::getRemainingBalance ,emiOrderManagement.getRemainingBalance());
        }
        if (StringUtils.isNotBlank(emiOrderManagement.getNation())){
            lqw.eq(EmiOrderManagement::getNation ,emiOrderManagement.getNation());
        }
        if (StringUtils.isNotBlank(emiOrderManagement.getTradeMethod())){
            lqw.eq(EmiOrderManagement::getTradeMethod ,emiOrderManagement.getTradeMethod());
        }
        if (StringUtils.isNotBlank(emiOrderManagement.getBuyerName())){
            lqw.like(EmiOrderManagement::getBuyerName ,emiOrderManagement.getBuyerName());
        }
        if (StringUtils.isNotBlank(emiOrderManagement.getStatus())){
            lqw.eq(EmiOrderManagement::getStatus ,emiOrderManagement.getStatus());
        }
        return this.list(lqw);
    }




}
