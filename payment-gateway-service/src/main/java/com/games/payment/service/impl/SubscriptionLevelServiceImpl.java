package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.SubscriptionLevelMapper;
import com.games.payment.domain.SubscriptionLevel;
import com.games.payment.service.ISubscriptionLevelService;

import java.util.List;
import java.util.Map;

/**
 * 订阅级别Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class SubscriptionLevelServiceImpl extends ServiceImpl<SubscriptionLevelMapper, SubscriptionLevel> implements ISubscriptionLevelService {

    /**
     * 查询订阅级别列表
     *
     * @param subscriptionLevel 订阅级别
     * @return 订阅级别
     */
    @Override
    public List<SubscriptionLevel> selectAllList(SubscriptionLevel subscriptionLevel)
    {
        return getBaseMapper().selectAllList(subscriptionLevel);
    }


    @Override
    public List<SubscriptionLevel> queryList(SubscriptionLevel subscriptionLevel) {
        LambdaQueryWrapper<SubscriptionLevel> lqw = Wrappers.lambdaQuery();
        if (subscriptionLevel.getSaasUserCorporationId() != null){
            lqw.eq(SubscriptionLevel::getSaasUserCorporationId ,subscriptionLevel.getSaasUserCorporationId());
        }
        if (StringUtils.isNotBlank(subscriptionLevel.getName())){
            lqw.like(SubscriptionLevel::getName ,subscriptionLevel.getName());
        }
        if (subscriptionLevel.getCreatedAt() != null){
            lqw.eq(SubscriptionLevel::getCreatedAt ,subscriptionLevel.getCreatedAt());
        }
        if (subscriptionLevel.getCreatedBy() != null){
            lqw.eq(SubscriptionLevel::getCreatedBy ,subscriptionLevel.getCreatedBy());
        }
        if (subscriptionLevel.getDeletedAt() != null){
            lqw.eq(SubscriptionLevel::getDeletedAt ,subscriptionLevel.getDeletedAt());
        }
        if (subscriptionLevel.getDeletedBy() != null){
            lqw.eq(SubscriptionLevel::getDeletedBy ,subscriptionLevel.getDeletedBy());
        }
        if (subscriptionLevel.getDeleted() != null){
            lqw.eq(SubscriptionLevel::getDeleted ,subscriptionLevel.getDeleted());
        }
        return this.list(lqw);
    }




}
