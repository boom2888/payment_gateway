package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.PaymentsDataMapper;
import com.games.payment.domain.PaymentsData;
import com.games.payment.service.IPaymentsDataService;

import java.util.List;
import java.util.Map;

/**
 * 支付数据Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class PaymentsDataServiceImpl extends ServiceImpl<PaymentsDataMapper, PaymentsData> implements IPaymentsDataService {

    /**
     * 查询支付数据列表
     *
     * @param paymentsData 支付数据
     * @return 支付数据
     */
    @Override
    public List<PaymentsData> selectAllList(PaymentsData paymentsData)
    {
        return getBaseMapper().selectAllList(paymentsData);
    }


    @Override
    public List<PaymentsData> queryList(PaymentsData paymentsData) {
        LambdaQueryWrapper<PaymentsData> lqw = Wrappers.lambdaQuery();
        if (paymentsData.getMerchantId() != null){
            lqw.eq(PaymentsData::getMerchantId ,paymentsData.getMerchantId());
        }
        if (paymentsData.getMethodName() != null){
            lqw.like(PaymentsData::getMethodName ,paymentsData.getMethodName());
        }
        if (paymentsData.getOrderId() != null){
            lqw.eq(PaymentsData::getOrderId ,paymentsData.getOrderId());
        }
        if (paymentsData.getDate() != null){
            lqw.eq(PaymentsData::getDate ,paymentsData.getDate());
        }
        if (paymentsData.getUploadId() != null){
            lqw.eq(PaymentsData::getUploadId ,paymentsData.getUploadId());
        }
        if (paymentsData.getAmount() != null){
            lqw.eq(PaymentsData::getAmount ,paymentsData.getAmount());
        }
        if (StringUtils.isNotBlank(paymentsData.getCurrency())){
            lqw.eq(PaymentsData::getCurrency ,paymentsData.getCurrency());
        }
        if (StringUtils.isNotBlank(paymentsData.getTransactionResult())){
            lqw.eq(PaymentsData::getTransactionResult ,paymentsData.getTransactionResult());
        }
        if (paymentsData.getBin() != null){
            lqw.eq(PaymentsData::getBin ,paymentsData.getBin());
        }
        if (StringUtils.isNotBlank(paymentsData.getReasonCode())){
            lqw.eq(PaymentsData::getReasonCode ,paymentsData.getReasonCode());
        }
        if (paymentsData.getQuote() != null){
            lqw.eq(PaymentsData::getQuote ,paymentsData.getQuote());
        }
        if (StringUtils.isNotBlank(paymentsData.getSsoOrderId())){
            lqw.eq(PaymentsData::getSsoOrderId ,paymentsData.getSsoOrderId());
        }
        if (StringUtils.isNotBlank(paymentsData.getOrderUuid())){
            lqw.eq(PaymentsData::getOrderUuid ,paymentsData.getOrderUuid());
        }
        if (paymentsData.getCurrencyId() != null){
            lqw.eq(PaymentsData::getCurrencyId ,paymentsData.getCurrencyId());
        }
        if (paymentsData.getFinalAmount() != null){
            lqw.eq(PaymentsData::getFinalAmount ,paymentsData.getFinalAmount());
        }
        if (paymentsData.getOrderCreated() != null){
            lqw.eq(PaymentsData::getOrderCreated ,paymentsData.getOrderCreated());
        }
        if (paymentsData.getCryptoAmount() != null){
            lqw.eq(PaymentsData::getCryptoAmount ,paymentsData.getCryptoAmount());
        }
        if (paymentsData.getOrderStatus() != null){
            lqw.eq(PaymentsData::getOrderStatus ,paymentsData.getOrderStatus());
        }
        if (paymentsData.getCreatedAt() != null){
            lqw.eq(PaymentsData::getCreatedAt ,paymentsData.getCreatedAt());
        }
        if (paymentsData.getUpdatedAt() != null){
            lqw.eq(PaymentsData::getUpdatedAt ,paymentsData.getUpdatedAt());
        }
        if (StringUtils.isNotBlank(paymentsData.getTransactionId())){
            lqw.eq(PaymentsData::getTransactionId ,paymentsData.getTransactionId());
        }
        if (StringUtils.isNotBlank(paymentsData.getIssuerCountry())){
            lqw.eq(PaymentsData::getIssuerCountry ,paymentsData.getIssuerCountry());
        }
        return this.list(lqw);
    }




}
