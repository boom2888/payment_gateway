package com.games.payment.service;

import com.games.payment.domain.SubscriptionModel;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 订阅模型Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface ISubscriptionModelService extends IService<SubscriptionModel> {

    /**
     * 查询订阅模型列表
     *
     * @param subscriptionModel 订阅模型
     * @return 订阅模型集合
     */
    List<SubscriptionModel> selectAllList(SubscriptionModel subscriptionModel);

    /**
     * 查询列表
     */
    List<SubscriptionModel> queryList(SubscriptionModel subscriptionModel);

}
