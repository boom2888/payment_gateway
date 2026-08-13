package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.AddressMapper;
import com.games.payment.domain.Address;
import com.games.payment.service.IAddressService;

import java.util.List;
import java.util.Map;

/**
 * 地址Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements IAddressService {

    /**
     * 查询地址列表
     *
     * @param address 地址
     * @return 地址
     */
    @Override
    public List<Address> selectAllList(Address address)
    {
        return getBaseMapper().selectAllList(address);
    }


    @Override
    public List<Address> queryList(Address address) {
        LambdaQueryWrapper<Address> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(address.getAddress1())){
            lqw.eq(Address::getAddress1 ,address.getAddress1());
        }
        if (StringUtils.isNotBlank(address.getAddress2())){
            lqw.eq(Address::getAddress2 ,address.getAddress2());
        }
        if (StringUtils.isNotBlank(address.getCity())){
            lqw.eq(Address::getCity ,address.getCity());
        }
        if (StringUtils.isNotBlank(address.getState())){
            lqw.eq(Address::getState ,address.getState());
        }
        if (StringUtils.isNotBlank(address.getCountryName())){
            lqw.like(Address::getCountryName ,address.getCountryName());
        }
        if (StringUtils.isNotBlank(address.getPostCode())){
            lqw.eq(Address::getPostCode ,address.getPostCode());
        }
        if (address.getCreatedAt() != null){
            lqw.eq(Address::getCreatedAt ,address.getCreatedAt());
        }
        if (address.getCreatedBy() != null){
            lqw.eq(Address::getCreatedBy ,address.getCreatedBy());
        }
        if (address.getDeletedAt() != null){
            lqw.eq(Address::getDeletedAt ,address.getDeletedAt());
        }
        if (address.getDeletedBy() != null){
            lqw.eq(Address::getDeletedBy ,address.getDeletedBy());
        }
        if (address.getDeleted() != null){
            lqw.eq(Address::getDeleted ,address.getDeleted());
        }
        return this.list(lqw);
    }




}
