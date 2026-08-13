package com.games.payment.service;

import com.games.payment.domain.AcquirerOrder;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 收单机构订单Service接口
 *
 * @author Ticker
 * @date 2025-07-08
 */
public interface IAcquirerOrderService extends IService<AcquirerOrder> {


    AcquirerOrder getByAcsTransactionId(String acsTransactionId);
    /**
     * 查询收单机构订单列表
     *
     * @param acquirerOrder 收单机构订单
     * @return 收单机构订单集合
     */
    List<AcquirerOrder> selectAllList(AcquirerOrder acquirerOrder);

    /**
     * 查询列表
     */
    List<AcquirerOrder> queryList(AcquirerOrder acquirerOrder);

}
