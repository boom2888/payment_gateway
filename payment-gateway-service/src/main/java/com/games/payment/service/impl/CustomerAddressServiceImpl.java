package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.CustomerAddressMapper;
import com.games.payment.domain.CustomerAddress;
import com.games.payment.service.ICustomerAddressService;

import java.util.List;
import java.util.Map;

/**
 * 客户地址Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class CustomerAddressServiceImpl extends ServiceImpl<CustomerAddressMapper, CustomerAddress> implements ICustomerAddressService {

    /**
     * 查询客户地址列表
     *
     * @param customerAddress 客户地址
     * @return 客户地址
     */
    @Override
    public List<CustomerAddress> selectAllList(CustomerAddress customerAddress)
    {
        return getBaseMapper().selectAllList(customerAddress);
    }


    @Override
    public List<CustomerAddress> queryList(CustomerAddress customerAddress) {
        LambdaQueryWrapper<CustomerAddress> lqw = Wrappers.lambdaQuery();
        if (customerAddress.getCustomerId() != null){
            lqw.eq(CustomerAddress::getCustomerId ,customerAddress.getCustomerId());
        }
        if (customerAddress.getAddressId() != null){
            lqw.eq(CustomerAddress::getAddressId ,customerAddress.getAddressId());
        }
        if (customerAddress.getActivated() != null){
            lqw.eq(CustomerAddress::getActivated ,customerAddress.getActivated());
        }
        if (customerAddress.getCreatedAt() != null){
            lqw.eq(CustomerAddress::getCreatedAt ,customerAddress.getCreatedAt());
        }
        if (customerAddress.getCreatedBy() != null){
            lqw.eq(CustomerAddress::getCreatedBy ,customerAddress.getCreatedBy());
        }
        if (customerAddress.getDeletedAt() != null){
            lqw.eq(CustomerAddress::getDeletedAt ,customerAddress.getDeletedAt());
        }
        if (customerAddress.getDeletedBy() != null){
            lqw.eq(CustomerAddress::getDeletedBy ,customerAddress.getDeletedBy());
        }
        if (customerAddress.getDeleted() != null){
            lqw.eq(CustomerAddress::getDeleted ,customerAddress.getDeleted());
        }
        return this.list(lqw);
    }




}
