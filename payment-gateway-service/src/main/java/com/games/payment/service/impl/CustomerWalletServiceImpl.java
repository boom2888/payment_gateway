package com.games.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.games.payment.domain.CustomerWallet;
import com.games.payment.mapper.CustomerWalletMapper;
import com.games.payment.service.ICustomerWalletService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 客户钱包Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class CustomerWalletServiceImpl extends ServiceImpl<CustomerWalletMapper, CustomerWallet> implements ICustomerWalletService {

    @Override
    public CustomerWallet getCustomerWalletByCrypto(Long customerId, Long cryptoId) {
        LambdaQueryWrapper<CustomerWallet> lqw = Wrappers.lambdaQuery();
        lqw.eq(CustomerWallet::getCustomerId, customerId);
        if (cryptoId != null) {
            lqw.eq(CustomerWallet::getCryptoId, cryptoId);
        }
        List<CustomerWallet> cryptoList = this.list(lqw);
        if (!cryptoList.isEmpty()) {
            return cryptoList.get(0);
        }
        return null;
    }

    /**
     * 查询客户钱包列表
     *
     * @param customerWallet 客户钱包
     * @return 客户钱包
     */
    @Override
    public List<CustomerWallet> selectAllList(CustomerWallet customerWallet)
    {
        return getBaseMapper().selectAllList(customerWallet);
    }


    @Override
    public List<CustomerWallet> queryList(CustomerWallet customerWallet) {
        LambdaQueryWrapper<CustomerWallet> lqw = Wrappers.lambdaQuery();
        if (customerWallet.getCustomerId() != null){
            lqw.eq(CustomerWallet::getCustomerId ,customerWallet.getCustomerId());
        }
        if (customerWallet.getCryptoId() != null){
            lqw.eq(CustomerWallet::getCryptoId ,customerWallet.getCryptoId());
        }
        if (StringUtils.isNotBlank(customerWallet.getWalletAddress())){
            lqw.eq(CustomerWallet::getWalletAddress ,customerWallet.getWalletAddress());
        }
        if (customerWallet.getActive() != null){
            lqw.eq(CustomerWallet::getActive ,customerWallet.getActive());
        }
        if (customerWallet.getCreatedAt() != null){
            lqw.eq(CustomerWallet::getCreatedAt ,customerWallet.getCreatedAt());
        }
        if (customerWallet.getCreatedBy() != null){
            lqw.eq(CustomerWallet::getCreatedBy ,customerWallet.getCreatedBy());
        }
        if (customerWallet.getDeletedAt() != null){
            lqw.eq(CustomerWallet::getDeletedAt ,customerWallet.getDeletedAt());
        }
        if (customerWallet.getDeletedBy() != null){
            lqw.eq(CustomerWallet::getDeletedBy ,customerWallet.getDeletedBy());
        }
        if (customerWallet.getDeleted() != null){
            lqw.eq(CustomerWallet::getDeleted ,customerWallet.getDeleted());
        }
        if (StringUtils.isNotBlank(customerWallet.getWalletName())){
            lqw.like(CustomerWallet::getWalletName ,customerWallet.getWalletName());
        }
        return this.list(lqw);
    }




}
