package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.SubscriptionFeeMapper;
import com.games.payment.domain.SubscriptionFee;
import com.games.payment.service.ISubscriptionFeeService;

import java.util.List;
import java.util.Map;

/**
 * 订阅费用Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class SubscriptionFeeServiceImpl extends ServiceImpl<SubscriptionFeeMapper, SubscriptionFee> implements ISubscriptionFeeService {

    /**
     * 查询订阅费用列表
     *
     * @param subscriptionFee 订阅费用
     * @return 订阅费用
     */
    @Override
    public List<SubscriptionFee> selectAllList(SubscriptionFee subscriptionFee)
    {
        return getBaseMapper().selectAllList(subscriptionFee);
    }


    @Override
    public List<SubscriptionFee> queryList(SubscriptionFee subscriptionFee) {
        LambdaQueryWrapper<SubscriptionFee> lqw = Wrappers.lambdaQuery();
        if (subscriptionFee.getSubscriptionModelId() != null){
            lqw.eq(SubscriptionFee::getSubscriptionModelId ,subscriptionFee.getSubscriptionModelId());
        }
        if (subscriptionFee.getFeeTypeId() != null){
            lqw.eq(SubscriptionFee::getFeeTypeId ,subscriptionFee.getFeeTypeId());
        }
        if (subscriptionFee.getFixedAmount() != null){
            lqw.eq(SubscriptionFee::getFixedAmount ,subscriptionFee.getFixedAmount());
        }
        if (subscriptionFee.getPercentage() != null){
            lqw.eq(SubscriptionFee::getPercentage ,subscriptionFee.getPercentage());
        }
        if (subscriptionFee.getOption() != null){
            lqw.eq(SubscriptionFee::getOption ,subscriptionFee.getOption());
        }
        if (subscriptionFee.getCreatedAt() != null){
            lqw.eq(SubscriptionFee::getCreatedAt ,subscriptionFee.getCreatedAt());
        }
        if (subscriptionFee.getCreatedBy() != null){
            lqw.eq(SubscriptionFee::getCreatedBy ,subscriptionFee.getCreatedBy());
        }
        if (subscriptionFee.getDeletedAt() != null){
            lqw.eq(SubscriptionFee::getDeletedAt ,subscriptionFee.getDeletedAt());
        }
        if (subscriptionFee.getDeletedBy() != null){
            lqw.eq(SubscriptionFee::getDeletedBy ,subscriptionFee.getDeletedBy());
        }
        if (subscriptionFee.getDeleted() != null){
            lqw.eq(SubscriptionFee::getDeleted ,subscriptionFee.getDeleted());
        }
        return this.list(lqw);
    }




}
