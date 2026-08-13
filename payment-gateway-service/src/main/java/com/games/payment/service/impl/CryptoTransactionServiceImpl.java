package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.CryptoTransactionMapper;
import com.games.payment.domain.CryptoTransaction;
import com.games.payment.service.ICryptoTransactionService;

import java.util.List;
import java.util.Map;

/**
 * 加密货币交易Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class CryptoTransactionServiceImpl extends ServiceImpl<CryptoTransactionMapper, CryptoTransaction> implements ICryptoTransactionService {

    /**
     * 查询加密货币交易列表
     *
     * @param cryptoTransaction 加密货币交易
     * @return 加密货币交易
     */
    @Override
    public List<CryptoTransaction> selectAllList(CryptoTransaction cryptoTransaction)
    {
        return getBaseMapper().selectAllList(cryptoTransaction);
    }


    @Override
    public List<CryptoTransaction> queryList(CryptoTransaction cryptoTransaction) {
        LambdaQueryWrapper<CryptoTransaction> lqw = Wrappers.lambdaQuery();
        if (cryptoTransaction.getOrderId() != null){
            lqw.eq(CryptoTransaction::getOrderId ,cryptoTransaction.getOrderId());
        }
        if (StringUtils.isNotBlank(cryptoTransaction.getTransactionId())){
            lqw.eq(CryptoTransaction::getTransactionId ,cryptoTransaction.getTransactionId());
        }
        if (StringUtils.isNotBlank(cryptoTransaction.getTransactionHash())){
            lqw.eq(CryptoTransaction::getTransactionHash ,cryptoTransaction.getTransactionHash());
        }
        if (cryptoTransaction.getCryptoAmount() != null){
            lqw.eq(CryptoTransaction::getCryptoAmount ,cryptoTransaction.getCryptoAmount());
        }
        if (cryptoTransaction.getNetworkFee() != null){
            lqw.eq(CryptoTransaction::getNetworkFee ,cryptoTransaction.getNetworkFee());
        }
        if (StringUtils.isNotBlank(cryptoTransaction.getFromWallet())){
            lqw.eq(CryptoTransaction::getFromWallet ,cryptoTransaction.getFromWallet());
        }
        if (StringUtils.isNotBlank(cryptoTransaction.getToWallet())){
            lqw.eq(CryptoTransaction::getToWallet ,cryptoTransaction.getToWallet());
        }
        if (cryptoTransaction.getCreatedAt() != null){
            lqw.eq(CryptoTransaction::getCreatedAt ,cryptoTransaction.getCreatedAt());
        }
        if (cryptoTransaction.getCreatedBy() != null){
            lqw.eq(CryptoTransaction::getCreatedBy ,cryptoTransaction.getCreatedBy());
        }
        if (cryptoTransaction.getDeletedAt() != null){
            lqw.eq(CryptoTransaction::getDeletedAt ,cryptoTransaction.getDeletedAt());
        }
        if (cryptoTransaction.getDeletedBy() != null){
            lqw.eq(CryptoTransaction::getDeletedBy ,cryptoTransaction.getDeletedBy());
        }
        if (cryptoTransaction.getDeleted() != null){
            lqw.eq(CryptoTransaction::getDeleted ,cryptoTransaction.getDeleted());
        }
        return this.list(lqw);
    }




}
