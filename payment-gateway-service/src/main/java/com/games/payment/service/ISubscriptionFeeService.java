package com.games.payment.service;

import com.games.payment.domain.SubscriptionFee;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 订阅费用Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface ISubscriptionFeeService extends IService<SubscriptionFee> {

    /**
     * 查询订阅费用列表
     *
     * @param subscriptionFee 订阅费用
     * @return 订阅费用集合
     */
    List<SubscriptionFee> selectAllList(SubscriptionFee subscriptionFee);

    /**
     * 查询列表
     */
    List<SubscriptionFee> queryList(SubscriptionFee subscriptionFee);

}
