package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.NuvieTransactionDataMapper;
import com.games.payment.domain.NuvieTransactionData;
import com.games.payment.service.INuvieTransactionDataService;

import java.util.List;
import java.util.Map;

/**
 * Nuvei交易数据Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class NuvieTransactionDataServiceImpl extends ServiceImpl<NuvieTransactionDataMapper, NuvieTransactionData> implements INuvieTransactionDataService {

    /**
     * 查询Nuvei交易数据列表
     *
     * @param nuvieTransactionData Nuvei交易数据
     * @return Nuvei交易数据
     */
    @Override
    public List<NuvieTransactionData> selectAllList(NuvieTransactionData nuvieTransactionData)
    {
        return getBaseMapper().selectAllList(nuvieTransactionData);
    }


    @Override
    public List<NuvieTransactionData> queryList(NuvieTransactionData nuvieTransactionData) {
        LambdaQueryWrapper<NuvieTransactionData> lqw = Wrappers.lambdaQuery();
        if (nuvieTransactionData.getMerchantId() != null){
            lqw.eq(NuvieTransactionData::getMerchantId ,nuvieTransactionData.getMerchantId());
        }
        if (nuvieTransactionData.getMethodName() != null){
            lqw.like(NuvieTransactionData::getMethodName ,nuvieTransactionData.getMethodName());
        }
        if (nuvieTransactionData.getOrderId() != null){
            lqw.eq(NuvieTransactionData::getOrderId ,nuvieTransactionData.getOrderId());
        }
        if (nuvieTransactionData.getDate() != null){
            lqw.eq(NuvieTransactionData::getDate ,nuvieTransactionData.getDate());
        }
        if (nuvieTransactionData.getUploadId() != null){
            lqw.eq(NuvieTransactionData::getUploadId ,nuvieTransactionData.getUploadId());
        }
        if (nuvieTransactionData.getAmount() != null){
            lqw.eq(NuvieTransactionData::getAmount ,nuvieTransactionData.getAmount());
        }
        if (StringUtils.isNotBlank(nuvieTransactionData.getCurrency())){
            lqw.eq(NuvieTransactionData::getCurrency ,nuvieTransactionData.getCurrency());
        }
        if (StringUtils.isNotBlank(nuvieTransactionData.getTransactionResult())){
            lqw.eq(NuvieTransactionData::getTransactionResult ,nuvieTransactionData.getTransactionResult());
        }
        if (nuvieTransactionData.getBin() != null){
            lqw.eq(NuvieTransactionData::getBin ,nuvieTransactionData.getBin());
        }
        if (StringUtils.isNotBlank(nuvieTransactionData.getReasonCode())){
            lqw.eq(NuvieTransactionData::getReasonCode ,nuvieTransactionData.getReasonCode());
        }
        if (nuvieTransactionData.getQuote() != null){
            lqw.eq(NuvieTransactionData::getQuote ,nuvieTransactionData.getQuote());
        }
        if (StringUtils.isNotBlank(nuvieTransactionData.getSsoOrderId())){
            lqw.eq(NuvieTransactionData::getSsoOrderId ,nuvieTransactionData.getSsoOrderId());
        }
        if (StringUtils.isNotBlank(nuvieTransactionData.getOrderUuid())){
            lqw.eq(NuvieTransactionData::getOrderUuid ,nuvieTransactionData.getOrderUuid());
        }
        if (nuvieTransactionData.getCurrencyId() != null){
            lqw.eq(NuvieTransactionData::getCurrencyId ,nuvieTransactionData.getCurrencyId());
        }
        if (nuvieTransactionData.getFinalAmount() != null){
            lqw.eq(NuvieTransactionData::getFinalAmount ,nuvieTransactionData.getFinalAmount());
        }
        if (nuvieTransactionData.getOrderCreated() != null){
            lqw.eq(NuvieTransactionData::getOrderCreated ,nuvieTransactionData.getOrderCreated());
        }
        if (nuvieTransactionData.getCryptoAmount() != null){
            lqw.eq(NuvieTransactionData::getCryptoAmount ,nuvieTransactionData.getCryptoAmount());
        }
        if (nuvieTransactionData.getOrderStatus() != null){
            lqw.eq(NuvieTransactionData::getOrderStatus ,nuvieTransactionData.getOrderStatus());
        }
        if (nuvieTransactionData.getCreatedAt() != null){
            lqw.eq(NuvieTransactionData::getCreatedAt ,nuvieTransactionData.getCreatedAt());
        }
        if (nuvieTransactionData.getUpdatedAt() != null){
            lqw.eq(NuvieTransactionData::getUpdatedAt ,nuvieTransactionData.getUpdatedAt());
        }
        if (StringUtils.isNotBlank(nuvieTransactionData.getTransactionId())){
            lqw.eq(NuvieTransactionData::getTransactionId ,nuvieTransactionData.getTransactionId());
        }
        if (StringUtils.isNotBlank(nuvieTransactionData.getIssuerCountry())){
            lqw.eq(NuvieTransactionData::getIssuerCountry ,nuvieTransactionData.getIssuerCountry());
        }
        return this.list(lqw);
    }




}
