package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.OrderUserInfoMapper;
import com.games.payment.domain.OrderUserInfo;
import com.games.payment.service.IOrderUserInfoService;

import java.util.List;
import java.util.Map;

/**
 * 订单用户信息Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class OrderUserInfoServiceImpl extends ServiceImpl<OrderUserInfoMapper, OrderUserInfo> implements IOrderUserInfoService {

    /**
     * 查询订单用户信息列表
     *
     * @param orderUserInfo 订单用户信息
     * @return 订单用户信息
     */
    @Override
    public List<OrderUserInfo> selectAllList(OrderUserInfo orderUserInfo)
    {
        return getBaseMapper().selectAllList(orderUserInfo);
    }


    @Override
    public List<OrderUserInfo> queryList(OrderUserInfo orderUserInfo) {
        LambdaQueryWrapper<OrderUserInfo> lqw = Wrappers.lambdaQuery();
        if (orderUserInfo.getOrderId() != null){
            lqw.eq(OrderUserInfo::getOrderId ,orderUserInfo.getOrderId());
        }
        if (StringUtils.isNotBlank(orderUserInfo.getEmail())){
            lqw.eq(OrderUserInfo::getEmail ,orderUserInfo.getEmail());
        }
        if (StringUtils.isNotBlank(orderUserInfo.getCountry())){
            lqw.eq(OrderUserInfo::getCountry ,orderUserInfo.getCountry());
        }
        if (StringUtils.isNotBlank(orderUserInfo.getCity())){
            lqw.eq(OrderUserInfo::getCity ,orderUserInfo.getCity());
        }
        if (StringUtils.isNotBlank(orderUserInfo.getAddressLine1())){
            lqw.eq(OrderUserInfo::getAddressLine1 ,orderUserInfo.getAddressLine1());
        }
        if (StringUtils.isNotBlank(orderUserInfo.getZip())){
            lqw.eq(OrderUserInfo::getZip ,orderUserInfo.getZip());
        }
        return this.list(lqw);
    }




}
