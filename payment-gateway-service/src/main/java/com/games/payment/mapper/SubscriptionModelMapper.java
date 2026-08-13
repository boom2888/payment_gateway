package com.games.payment.mapper;

import com.games.payment.domain.SubscriptionModel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 订阅模型Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface SubscriptionModelMapper extends BaseMapper<SubscriptionModel> {
    /**
     * 查询订阅模型列表
     *
     * @param subscriptionModel 订阅模型
     * @return 订阅模型集合
     */
    List<SubscriptionModel> selectAllList(SubscriptionModel subscriptionModel);

}
