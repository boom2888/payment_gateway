package com.games.payment.service;

import com.games.payment.domain.SubscriptionLevel;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 订阅级别Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface ISubscriptionLevelService extends IService<SubscriptionLevel> {

    /**
     * 查询订阅级别列表
     *
     * @param subscriptionLevel 订阅级别
     * @return 订阅级别集合
     */
    List<SubscriptionLevel> selectAllList(SubscriptionLevel subscriptionLevel);

    /**
     * 查询列表
     */
    List<SubscriptionLevel> queryList(SubscriptionLevel subscriptionLevel);

}
