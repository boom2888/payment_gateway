package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.OffRampingCustomerWalletMapper;
import com.games.payment.domain.OffRampingCustomerWallet;
import com.games.payment.service.IOffRampingCustomerWalletService;

import java.util.List;
import java.util.Map;

/**
 * 出金客户钱包Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class OffRampingCustomerWalletServiceImpl extends ServiceImpl<OffRampingCustomerWalletMapper, OffRampingCustomerWallet> implements IOffRampingCustomerWalletService {

    /**
     * 查询出金客户钱包列表
     *
     * @param offRampingCustomerWallet 出金客户钱包
     * @return 出金客户钱包
     */
    @Override
    public List<OffRampingCustomerWallet> selectAllList(OffRampingCustomerWallet offRampingCustomerWallet)
    {
        return getBaseMapper().selectAllList(offRampingCustomerWallet);
    }


    @Override
    public List<OffRampingCustomerWallet> queryList(OffRampingCustomerWallet offRampingCustomerWallet) {
        LambdaQueryWrapper<OffRampingCustomerWallet> lqw = Wrappers.lambdaQuery();
        if (offRampingCustomerWallet.getCryptoId() != null){
            lqw.eq(OffRampingCustomerWallet::getCryptoId ,offRampingCustomerWallet.getCryptoId());
        }
        if (StringUtils.isNotBlank(offRampingCustomerWallet.getWalletAddress())){
            lqw.eq(OffRampingCustomerWallet::getWalletAddress ,offRampingCustomerWallet.getWalletAddress());
        }
        if (offRampingCustomerWallet.getCustomerId() != null){
            lqw.eq(OffRampingCustomerWallet::getCustomerId ,offRampingCustomerWallet.getCustomerId());
        }
        if (offRampingCustomerWallet.getCreatedAt() != null){
            lqw.eq(OffRampingCustomerWallet::getCreatedAt ,offRampingCustomerWallet.getCreatedAt());
        }
        if (offRampingCustomerWallet.getCreatedBy() != null){
            lqw.eq(OffRampingCustomerWallet::getCreatedBy ,offRampingCustomerWallet.getCreatedBy());
        }
        if (offRampingCustomerWallet.getDeletedAt() != null){
            lqw.eq(OffRampingCustomerWallet::getDeletedAt ,offRampingCustomerWallet.getDeletedAt());
        }
        if (offRampingCustomerWallet.getDeletedBy() != null){
            lqw.eq(OffRampingCustomerWallet::getDeletedBy ,offRampingCustomerWallet.getDeletedBy());
        }
        if (offRampingCustomerWallet.getDeleted() != null){
            lqw.eq(OffRampingCustomerWallet::getDeleted ,offRampingCustomerWallet.getDeleted());
        }
        if (StringUtils.isNotBlank(offRampingCustomerWallet.getWalletName())){
            lqw.like(OffRampingCustomerWallet::getWalletName ,offRampingCustomerWallet.getWalletName());
        }
        return this.list(lqw);
    }




}
