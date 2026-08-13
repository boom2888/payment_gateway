package com.games.payment.service.impl;

import com.games.payment.domain.Currency;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.AcquirerOrderMapper;
import com.games.payment.domain.AcquirerOrder;
import com.games.payment.service.IAcquirerOrderService;

import java.util.List;
import java.util.Map;

/**
 * 收单机构订单Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-08
 */
@Service
public class AcquirerOrderServiceImpl extends ServiceImpl<AcquirerOrderMapper, AcquirerOrder> implements IAcquirerOrderService {

    @Override
    public AcquirerOrder getByAcsTransactionId(String acsTransactionId) {
        LambdaQueryWrapper<AcquirerOrder> lqw = Wrappers.lambdaQuery();
        lqw.like(AcquirerOrder::getAcsTransactionId, acsTransactionId);
        List<AcquirerOrder> cryptoList = this.list(lqw);
        if (!cryptoList.isEmpty()) {
            return cryptoList.get(0);
        }
        return null;
    }

    /**
     * 查询收单机构订单列表
     *
     * @param acquirerOrder 收单机构订单
     * @return 收单机构订单
     */
    @Override
    public List<AcquirerOrder> selectAllList(AcquirerOrder acquirerOrder)
    {
        return getBaseMapper().selectAllList(acquirerOrder);
    }


    @Override
    public List<AcquirerOrder> queryList(AcquirerOrder acquirerOrder) {
        LambdaQueryWrapper<AcquirerOrder> lqw = Wrappers.lambdaQuery();
        if (acquirerOrder.getAcquirerId() != null){
            lqw.eq(AcquirerOrder::getAcquirerId ,acquirerOrder.getAcquirerId());
        }
        if (StringUtils.isNotBlank(acquirerOrder.getReconRequestId())){
            lqw.eq(AcquirerOrder::getReconRequestId ,acquirerOrder.getReconRequestId());
        }
        if (acquirerOrder.getOrderId() != null){
            lqw.eq(AcquirerOrder::getOrderId ,acquirerOrder.getOrderId());
        }
        if (acquirerOrder.getType() != null){
            lqw.eq(AcquirerOrder::getType ,acquirerOrder.getType());
        }
        if (acquirerOrder.getCurrencyId() != null){
            lqw.eq(AcquirerOrder::getCurrencyId ,acquirerOrder.getCurrencyId());
        }
        if (acquirerOrder.getAmount() != null){
            lqw.eq(AcquirerOrder::getAmount ,acquirerOrder.getAmount());
        }
        if (StringUtils.isNotBlank(acquirerOrder.getPaymentDeclinedPayload())){
            lqw.eq(AcquirerOrder::getPaymentDeclinedPayload ,acquirerOrder.getPaymentDeclinedPayload());
        }
        if (acquirerOrder.getStatus() != null){
            lqw.eq(AcquirerOrder::getStatus ,acquirerOrder.getStatus());
        }
        if (acquirerOrder.getCreatedAt() != null){
            lqw.eq(AcquirerOrder::getCreatedAt ,acquirerOrder.getCreatedAt());
        }
        if (acquirerOrder.getCreatedBy() != null){
            lqw.eq(AcquirerOrder::getCreatedBy ,acquirerOrder.getCreatedBy());
        }
        if (acquirerOrder.getDeletedAt() != null){
            lqw.eq(AcquirerOrder::getDeletedAt ,acquirerOrder.getDeletedAt());
        }
        if (acquirerOrder.getDeletedBy() != null){
            lqw.eq(AcquirerOrder::getDeletedBy ,acquirerOrder.getDeletedBy());
        }
        if (acquirerOrder.getDeleted() != null){
            lqw.eq(AcquirerOrder::getDeleted ,acquirerOrder.getDeleted());
        }
        if (acquirerOrder.getTokenId() != null){
            lqw.eq(AcquirerOrder::getTokenId ,acquirerOrder.getTokenId());
        }
        if (StringUtils.isNotBlank(acquirerOrder.getResponseCode())){
            lqw.eq(AcquirerOrder::getResponseCode ,acquirerOrder.getResponseCode());
        }
        if (StringUtils.isNotBlank(acquirerOrder.getAcsTransactionId())){
            lqw.eq(AcquirerOrder::getAcsTransactionId ,acquirerOrder.getAcsTransactionId());
        }
        return this.list(lqw);
    }




}
