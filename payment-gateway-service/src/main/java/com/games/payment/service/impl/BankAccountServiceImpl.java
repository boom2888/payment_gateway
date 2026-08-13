package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.BankAccountMapper;
import com.games.payment.domain.BankAccount;
import com.games.payment.service.IBankAccountService;

import java.util.List;
import java.util.Map;

/**
 * 银行账户Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class BankAccountServiceImpl extends ServiceImpl<BankAccountMapper, BankAccount> implements IBankAccountService {

    /**
     * 查询银行账户列表
     *
     * @param bankAccount 银行账户
     * @return 银行账户
     */
    @Override
    public List<BankAccount> selectAllList(BankAccount bankAccount)
    {
        return getBaseMapper().selectAllList(bankAccount);
    }


    @Override
    public List<BankAccount> queryList(BankAccount bankAccount) {
        LambdaQueryWrapper<BankAccount> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(bankAccount.getBankName())){
            lqw.like(BankAccount::getBankName ,bankAccount.getBankName());
        }
        if (bankAccount.getBankAddressId() != null){
            lqw.eq(BankAccount::getBankAddressId ,bankAccount.getBankAddressId());
        }
        if (StringUtils.isNotBlank(bankAccount.getSortCode())){
            lqw.eq(BankAccount::getSortCode ,bankAccount.getSortCode());
        }
        if (StringUtils.isNotBlank(bankAccount.getAccountNumber())){
            lqw.eq(BankAccount::getAccountNumber ,bankAccount.getAccountNumber());
        }
        if (StringUtils.isNotBlank(bankAccount.getIban())){
            lqw.eq(BankAccount::getIban ,bankAccount.getIban());
        }
        if (StringUtils.isNotBlank(bankAccount.getBicSwift())){
            lqw.eq(BankAccount::getBicSwift ,bankAccount.getBicSwift());
        }
        if (bankAccount.getCurrencyId() != null){
            lqw.eq(BankAccount::getCurrencyId ,bankAccount.getCurrencyId());
        }
        if (bankAccount.getType() != null){
            lqw.eq(BankAccount::getType ,bankAccount.getType());
        }
        if (bankAccount.getCreatedAt() != null){
            lqw.eq(BankAccount::getCreatedAt ,bankAccount.getCreatedAt());
        }
        if (bankAccount.getCreatedBy() != null){
            lqw.eq(BankAccount::getCreatedBy ,bankAccount.getCreatedBy());
        }
        if (bankAccount.getDeletedAt() != null){
            lqw.eq(BankAccount::getDeletedAt ,bankAccount.getDeletedAt());
        }
        if (bankAccount.getDeletedBy() != null){
            lqw.eq(BankAccount::getDeletedBy ,bankAccount.getDeletedBy());
        }
        if (bankAccount.getDeleted() != null){
            lqw.eq(BankAccount::getDeleted ,bankAccount.getDeleted());
        }
        if (StringUtils.isNotBlank(bankAccount.getRoutingNumber())){
            lqw.eq(BankAccount::getRoutingNumber ,bankAccount.getRoutingNumber());
        }
        if (StringUtils.isNotBlank(bankAccount.getBankCounterpartyId())){
            lqw.eq(BankAccount::getBankCounterpartyId ,bankAccount.getBankCounterpartyId());
        }
        if (StringUtils.isNotBlank(bankAccount.getBankCountry())){
            lqw.eq(BankAccount::getBankCountry ,bankAccount.getBankCountry());
        }
        if (StringUtils.isNotBlank(bankAccount.getBsbCode())){
            lqw.eq(BankAccount::getBsbCode ,bankAccount.getBsbCode());
        }
        if (StringUtils.isNotBlank(bankAccount.getIfsc())){
            lqw.eq(BankAccount::getIfsc ,bankAccount.getIfsc());
        }
        if (StringUtils.isNotBlank(bankAccount.getClabe())){
            lqw.eq(BankAccount::getClabe ,bankAccount.getClabe());
        }
        if (StringUtils.isNotBlank(bankAccount.getCptyAddressPostcode())){
            lqw.eq(BankAccount::getCptyAddressPostcode ,bankAccount.getCptyAddressPostcode());
        }
        if (StringUtils.isNotBlank(bankAccount.getCptyAddress())){
            lqw.eq(BankAccount::getCptyAddress ,bankAccount.getCptyAddress());
        }
        if (StringUtils.isNotBlank(bankAccount.getCptyCountry())){
            lqw.eq(BankAccount::getCptyCountry ,bankAccount.getCptyCountry());
        }
        if (StringUtils.isNotBlank(bankAccount.getCptyCity())){
            lqw.eq(BankAccount::getCptyCity ,bankAccount.getCptyCity());
        }
        if (StringUtils.isNotBlank(bankAccount.getCptyStreetLine1())){
            lqw.eq(BankAccount::getCptyStreetLine1 ,bankAccount.getCptyStreetLine1());
        }
        if (StringUtils.isNotBlank(bankAccount.getFirstName())){
            lqw.like(BankAccount::getFirstName ,bankAccount.getFirstName());
        }
        if (StringUtils.isNotBlank(bankAccount.getLastName())){
            lqw.like(BankAccount::getLastName ,bankAccount.getLastName());
        }
        return this.list(lqw);
    }




}
