package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.SubscriptionModelMapper;
import com.games.payment.domain.SubscriptionModel;
import com.games.payment.service.ISubscriptionModelService;

import java.util.List;
import java.util.Map;

/**
 * 订阅模型Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class SubscriptionModelServiceImpl extends ServiceImpl<SubscriptionModelMapper, SubscriptionModel> implements ISubscriptionModelService {

    /**
     * 查询订阅模型列表
     *
     * @param subscriptionModel 订阅模型
     * @return 订阅模型
     */
    @Override
    public List<SubscriptionModel> selectAllList(SubscriptionModel subscriptionModel)
    {
        return getBaseMapper().selectAllList(subscriptionModel);
    }


    @Override
    public List<SubscriptionModel> queryList(SubscriptionModel subscriptionModel) {
        LambdaQueryWrapper<SubscriptionModel> lqw = Wrappers.lambdaQuery();
        if (subscriptionModel.getSaasUserCorporationId() != null){
            lqw.eq(SubscriptionModel::getSaasUserCorporationId ,subscriptionModel.getSaasUserCorporationId());
        }
        if (StringUtils.isNotBlank(subscriptionModel.getModelName())){
            lqw.like(SubscriptionModel::getModelName ,subscriptionModel.getModelName());
        }
        if (StringUtils.isNotBlank(subscriptionModel.getMemo())){
            lqw.eq(SubscriptionModel::getMemo ,subscriptionModel.getMemo());
        }
        if (subscriptionModel.getCustomerType() != null){
            lqw.eq(SubscriptionModel::getCustomerType ,subscriptionModel.getCustomerType());
        }
        if (subscriptionModel.getSubscriptionLevelId() != null){
            lqw.eq(SubscriptionModel::getSubscriptionLevelId ,subscriptionModel.getSubscriptionLevelId());
        }
        if (subscriptionModel.getCurrencyId() != null){
            lqw.eq(SubscriptionModel::getCurrencyId ,subscriptionModel.getCurrencyId());
        }
//        if (subscriptionModel.getDefault() != null){
//            lqw.eq(SubscriptionModel::getDefault ,subscriptionModel.getDefault());
//        }
        if (subscriptionModel.getStatus() != null){
            lqw.eq(SubscriptionModel::getStatus ,subscriptionModel.getStatus());
        }
        if (subscriptionModel.getCreatedAt() != null){
            lqw.eq(SubscriptionModel::getCreatedAt ,subscriptionModel.getCreatedAt());
        }
        if (subscriptionModel.getCreatedBy() != null){
            lqw.eq(SubscriptionModel::getCreatedBy ,subscriptionModel.getCreatedBy());
        }
        if (subscriptionModel.getDeletedAt() != null){
            lqw.eq(SubscriptionModel::getDeletedAt ,subscriptionModel.getDeletedAt());
        }
        if (subscriptionModel.getDeletedBy() != null){
            lqw.eq(SubscriptionModel::getDeletedBy ,subscriptionModel.getDeletedBy());
        }
        if (subscriptionModel.getDeleted() != null){
            lqw.eq(SubscriptionModel::getDeleted ,subscriptionModel.getDeleted());
        }
        return this.list(lqw);
    }




}
