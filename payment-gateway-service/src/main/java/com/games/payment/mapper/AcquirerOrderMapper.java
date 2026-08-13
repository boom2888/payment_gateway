package com.games.payment.mapper;

import com.games.payment.domain.AcquirerOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 收单机构订单Mapper接口
 *
 * @author Ticker
 * @date 2025-07-08
 */
public interface AcquirerOrderMapper extends BaseMapper<AcquirerOrder> {
    /**
     * 查询收单机构订单列表
     *
     * @param acquirerOrder 收单机构订单
     * @return 收单机构订单集合
     */
    List<AcquirerOrder> selectAllList(AcquirerOrder acquirerOrder);

}
