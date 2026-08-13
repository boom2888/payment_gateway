package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.AccountMapper;
import com.games.payment.domain.Account;
import com.games.payment.service.IAccountService;

import java.util.List;
import java.util.Map;

/**
 * 账户Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-07
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements IAccountService {

    /**
     * 查询账户列表
     *
     * @param account 账户
     * @return 账户
     */
    @Override
    public List<Account> selectAllList(Account account)
    {
        return getBaseMapper().selectAllList(account);
    }


    @Override
    public List<Account> queryList(Account account) {
        LambdaQueryWrapper<Account> lqw = Wrappers.lambdaQuery();
        if (account.getUserId() != null){
            lqw.eq(Account::getUserId ,account.getUserId());
        }
        if (StringUtils.isNotBlank(account.getName())){
            lqw.like(Account::getName ,account.getName());
        }
        if (account.getBankAccountId() != null){
            lqw.eq(Account::getBankAccountId ,account.getBankAccountId());
        }
        if (account.getTotalBalance() != null){
            lqw.eq(Account::getTotalBalance ,account.getTotalBalance());
        }
        if (account.getAvailableBalance() != null){
            lqw.eq(Account::getAvailableBalance ,account.getAvailableBalance());
        }
        if (account.getFreezedBalance() != null){
            lqw.eq(Account::getFreezedBalance ,account.getFreezedBalance());
        }
        if (StringUtils.isNotBlank(account.getColor())){
            lqw.eq(Account::getColor ,account.getColor());
        }
        if (account.getStatus() != null){
            lqw.eq(Account::getStatus ,account.getStatus());
        }
        if (account.getCreatedAt() != null){
            lqw.eq(Account::getCreatedAt ,account.getCreatedAt());
        }
        if (account.getCreatedBy() != null){
            lqw.eq(Account::getCreatedBy ,account.getCreatedBy());
        }
        if (account.getDeletedAt() != null){
            lqw.eq(Account::getDeletedAt ,account.getDeletedAt());
        }
        if (account.getDeletedBy() != null){
            lqw.eq(Account::getDeletedBy ,account.getDeletedBy());
        }
        if (account.getDeleted() != null){
            lqw.eq(Account::getDeleted ,account.getDeleted());
        }
        if (StringUtils.isNotBlank(account.getCounterpartyId())){
            lqw.eq(Account::getCounterpartyId ,account.getCounterpartyId());
        }
        if (StringUtils.isNotBlank(account.getCounterpartyState())){
            lqw.eq(Account::getCounterpartyState ,account.getCounterpartyState());
        }
        if (account.getCounterpartyType() != null){
            lqw.eq(Account::getCounterpartyType ,account.getCounterpartyType());
        }
        if (StringUtils.isNotBlank(account.getInternalBankAccountId())){
            lqw.eq(Account::getInternalBankAccountId ,account.getInternalBankAccountId());
        }
        return this.list(lqw);
    }




}
