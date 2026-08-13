package com.games.payment.service.impl;

import com.games.payment.domain.Crypto;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.CustomerMapper;
import com.games.payment.domain.Customer;
import com.games.payment.service.ICustomerService;

import java.util.List;
import java.util.Map;

/**
 * 客户Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService {

    @Override
    public Customer getBySsoCustomerId(Long shopId, String ssoCustomerId) {
        LambdaQueryWrapper<Customer> lqw = Wrappers.lambdaQuery();
        lqw.like(Customer::getMerchantId, shopId);
        lqw.like(Customer::getSsoCustomerId, ssoCustomerId);
        List<Customer> cryptoList = this.list(lqw);
        if (!cryptoList.isEmpty()) {
            return cryptoList.get(0);
        }
        return null;
    }

    /**
     * 查询客户列表
     *
     * @param customer 客户
     * @return 客户
     */
    @Override
    public List<Customer> selectAllList(Customer customer)
    {
        return getBaseMapper().selectAllList(customer);
    }


    @Override
    public List<Customer> queryList(Customer customer) {
        LambdaQueryWrapper<Customer> lqw = Wrappers.lambdaQuery();
        if (customer.getUserId() != null){
            lqw.eq(Customer::getUserId ,customer.getUserId());
        }
        if (customer.getSaasUserCorporationId() != null){
            lqw.eq(Customer::getSaasUserCorporationId ,customer.getSaasUserCorporationId());
        }
        if (customer.getType() != null){
            lqw.eq(Customer::getType ,customer.getType());
        }
        if (customer.getManager() != null){
            lqw.eq(Customer::getManager ,customer.getManager());
        }
        if (customer.getSubscriptionModelId() != null){
            lqw.eq(Customer::getSubscriptionModelId ,customer.getSubscriptionModelId());
        }
        if (customer.getRiskLevel() != null){
            lqw.eq(Customer::getRiskLevel ,customer.getRiskLevel());
        }
        if (StringUtils.isNotBlank(customer.getIdRef())){
            lqw.eq(Customer::getIdRef ,customer.getIdRef());
        }
        if (customer.getVipId() != null){
            lqw.eq(Customer::getVipId ,customer.getVipId());
        }
        if (customer.getMerchantId() != null){
            lqw.eq(Customer::getMerchantId ,customer.getMerchantId());
        }
        if (StringUtils.isNotBlank(customer.getSsoCustomerId())){
            lqw.eq(Customer::getSsoCustomerId ,customer.getSsoCustomerId());
        }
        if (customer.getCreatedAt() != null){
            lqw.eq(Customer::getCreatedAt ,customer.getCreatedAt());
        }
        if (customer.getCreatedBy() != null){
            lqw.eq(Customer::getCreatedBy ,customer.getCreatedBy());
        }
        if (customer.getDeletedAt() != null){
            lqw.eq(Customer::getDeletedAt ,customer.getDeletedAt());
        }
        if (customer.getDeletedBy() != null){
            lqw.eq(Customer::getDeletedBy ,customer.getDeletedBy());
        }
        if (customer.getDeleted() != null){
            lqw.eq(Customer::getDeleted ,customer.getDeleted());
        }
        if (StringUtils.isNotBlank(customer.getFireblocksVaultAccountId())){
            lqw.eq(Customer::getFireblocksVaultAccountId ,customer.getFireblocksVaultAccountId());
        }
        if (customer.getSum90Day() != null){
            lqw.eq(Customer::getSum90Day ,customer.getSum90Day());
        }
        if (StringUtils.isNotBlank(customer.getReferralCode())){
            lqw.eq(Customer::getReferralCode ,customer.getReferralCode());
        }
        return this.list(lqw);
    }




}
