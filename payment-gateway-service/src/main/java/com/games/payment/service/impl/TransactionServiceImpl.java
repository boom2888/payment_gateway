package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.TransactionMapper;
import com.games.payment.domain.Transaction;
import com.games.payment.service.ITransactionService;

import java.util.List;
import java.util.Map;

/**
 * 交易Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class TransactionServiceImpl extends ServiceImpl<TransactionMapper, Transaction> implements ITransactionService {

    /**
     * 查询交易列表
     *
     * @param transaction 交易
     * @return 交易
     */
    @Override
    public List<Transaction> selectAllList(Transaction transaction)
    {
        return getBaseMapper().selectAllList(transaction);
    }


    @Override
    public List<Transaction> queryList(Transaction transaction) {
        LambdaQueryWrapper<Transaction> lqw = Wrappers.lambdaQuery();
        if (transaction.getOrderId() != null){
            lqw.eq(Transaction::getOrderId ,transaction.getOrderId());
        }
        if (transaction.getAccountId() != null){
            lqw.eq(Transaction::getAccountId ,transaction.getAccountId());
        }
        if (StringUtils.isNotBlank(transaction.getCurrencyCode())){
            lqw.eq(Transaction::getCurrencyCode ,transaction.getCurrencyCode());
        }
        if (StringUtils.isNotBlank(transaction.getCpName())){
            lqw.like(Transaction::getCpName ,transaction.getCpName());
        }
        if (StringUtils.isNotBlank(transaction.getCpEmail())){
            lqw.eq(Transaction::getCpEmail ,transaction.getCpEmail());
        }
        if (StringUtils.isNotBlank(transaction.getCpBankName())){
            lqw.like(Transaction::getCpBankName ,transaction.getCpBankName());
        }
        if (StringUtils.isNotBlank(transaction.getCpSortCode())){
            lqw.eq(Transaction::getCpSortCode ,transaction.getCpSortCode());
        }
        if (StringUtils.isNotBlank(transaction.getCpAccountName())){
            lqw.like(Transaction::getCpAccountName ,transaction.getCpAccountName());
        }
        if (StringUtils.isNotBlank(transaction.getCpAccountNumber())){
            lqw.eq(Transaction::getCpAccountNumber ,transaction.getCpAccountNumber());
        }
        if (StringUtils.isNotBlank(transaction.getCpIban())){
            lqw.eq(Transaction::getCpIban ,transaction.getCpIban());
        }
        if (StringUtils.isNotBlank(transaction.getCpBicSwift())){
            lqw.eq(Transaction::getCpBicSwift ,transaction.getCpBicSwift());
        }
        if (transaction.getType() != null){
            lqw.eq(Transaction::getType ,transaction.getType());
        }
        if (transaction.getAmount() != null){
            lqw.eq(Transaction::getAmount ,transaction.getAmount());
        }
        if (StringUtils.isNotBlank(transaction.getFeeDetail())){
            lqw.eq(Transaction::getFeeDetail ,transaction.getFeeDetail());
        }
        if (transaction.getFeeAmount() != null){
            lqw.eq(Transaction::getFeeAmount ,transaction.getFeeAmount());
        }
        if (transaction.getInOut() != null){
            lqw.eq(Transaction::getInOut ,transaction.getInOut());
        }
        if (transaction.getStatus() != null){
            lqw.eq(Transaction::getStatus ,transaction.getStatus());
        }
        if (StringUtils.isNotBlank(transaction.getComment())){
            lqw.eq(Transaction::getComment ,transaction.getComment());
        }
        if (transaction.getInternal() != null){
            lqw.eq(Transaction::getInternal ,transaction.getInternal());
        }
        if (StringUtils.isNotBlank(transaction.getDescription())){
            lqw.eq(Transaction::getDescription ,transaction.getDescription());
        }
        if (transaction.getRiskLevel() != null){
            lqw.eq(Transaction::getRiskLevel ,transaction.getRiskLevel());
        }
        if (transaction.getCreatedAt() != null){
            lqw.eq(Transaction::getCreatedAt ,transaction.getCreatedAt());
        }
        if (transaction.getCreatedBy() != null){
            lqw.eq(Transaction::getCreatedBy ,transaction.getCreatedBy());
        }
        return this.list(lqw);
    }




}
