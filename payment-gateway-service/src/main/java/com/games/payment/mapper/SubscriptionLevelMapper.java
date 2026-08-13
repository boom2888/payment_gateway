package com.games.payment.mapper;

import com.games.payment.domain.SubscriptionLevel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 订阅级别Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface SubscriptionLevelMapper extends BaseMapper<SubscriptionLevel> {
    /**
     * 查询订阅级别列表
     *
     * @param subscriptionLevel 订阅级别
     * @return 订阅级别集合
     */
    List<SubscriptionLevel> selectAllList(SubscriptionLevel subscriptionLevel);

}
