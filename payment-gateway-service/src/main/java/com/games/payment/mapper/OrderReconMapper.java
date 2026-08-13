package com.games.payment.mapper;

import com.games.payment.domain.OrderRecon;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 订单对账Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface OrderReconMapper extends BaseMapper<OrderRecon> {
    /**
     * 查询订单对账列表
     *
     * @param orderRecon 订单对账
     * @return 订单对账集合
     */
    List<OrderRecon> selectAllList(OrderRecon orderRecon);

}
