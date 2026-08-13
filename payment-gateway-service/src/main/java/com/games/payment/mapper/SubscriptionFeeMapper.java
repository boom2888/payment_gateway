package com.games.payment.mapper;

import com.games.payment.domain.SubscriptionFee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 订阅费用Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface SubscriptionFeeMapper extends BaseMapper<SubscriptionFee> {
    /**
     * 查询订阅费用列表
     *
     * @param subscriptionFee 订阅费用
     * @return 订阅费用集合
     */
    List<SubscriptionFee> selectAllList(SubscriptionFee subscriptionFee);

}
