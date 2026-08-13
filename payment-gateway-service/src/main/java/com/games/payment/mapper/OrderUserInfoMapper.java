package com.games.payment.mapper;

import com.games.payment.domain.OrderUserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 订单用户信息Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface OrderUserInfoMapper extends BaseMapper<OrderUserInfo> {
    /**
     * 查询订单用户信息列表
     *
     * @param orderUserInfo 订单用户信息
     * @return 订单用户信息集合
     */
    List<OrderUserInfo> selectAllList(OrderUserInfo orderUserInfo);

}
