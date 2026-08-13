package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.OrderReconMapper;
import com.games.payment.domain.OrderRecon;
import com.games.payment.service.IOrderReconService;

import java.util.List;
import java.util.Map;

/**
 * 订单对账Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class OrderReconServiceImpl extends ServiceImpl<OrderReconMapper, OrderRecon> implements IOrderReconService {

    /**
     * 查询订单对账列表
     *
     * @param orderRecon 订单对账
     * @return 订单对账
     */
    @Override
    public List<OrderRecon> selectAllList(OrderRecon orderRecon)
    {
        return getBaseMapper().selectAllList(orderRecon);
    }


    @Override
    public List<OrderRecon> queryList(OrderRecon orderRecon) {
        LambdaQueryWrapper<OrderRecon> lqw = Wrappers.lambdaQuery();
        if (orderRecon.getReconId() != null){
            lqw.eq(OrderRecon::getReconId ,orderRecon.getReconId());
        }
        if (orderRecon.getOrderId() != null){
            lqw.eq(OrderRecon::getOrderId ,orderRecon.getOrderId());
        }
        if (StringUtils.isNotBlank(orderRecon.getAcquirerBatchPaymentId())){
            lqw.eq(OrderRecon::getAcquirerBatchPaymentId ,orderRecon.getAcquirerBatchPaymentId());
        }
        if (StringUtils.isNotBlank(orderRecon.getAcquirerPaymentId())){
            lqw.eq(OrderRecon::getAcquirerPaymentId ,orderRecon.getAcquirerPaymentId());
        }
        if (StringUtils.isNotBlank(orderRecon.getComment())){
            lqw.eq(OrderRecon::getComment ,orderRecon.getComment());
        }
        if (orderRecon.getStatus() != null){
            lqw.eq(OrderRecon::getStatus ,orderRecon.getStatus());
        }
        if (orderRecon.getCreatedAt() != null){
            lqw.eq(OrderRecon::getCreatedAt ,orderRecon.getCreatedAt());
        }
        if (orderRecon.getCreatedBy() != null){
            lqw.eq(OrderRecon::getCreatedBy ,orderRecon.getCreatedBy());
        }
        if (orderRecon.getDeletedAt() != null){
            lqw.eq(OrderRecon::getDeletedAt ,orderRecon.getDeletedAt());
        }
        if (orderRecon.getDeletedBy() != null){
            lqw.eq(OrderRecon::getDeletedBy ,orderRecon.getDeletedBy());
        }
        if (orderRecon.getDeleted() != null){
            lqw.eq(OrderRecon::getDeleted ,orderRecon.getDeleted());
        }
        return this.list(lqw);
    }




}
