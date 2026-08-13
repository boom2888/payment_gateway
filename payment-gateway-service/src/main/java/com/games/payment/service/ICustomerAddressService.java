package com.games.payment.service;

import com.games.payment.domain.CustomerAddress;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 客户地址Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface ICustomerAddressService extends IService<CustomerAddress> {

    /**
     * 查询客户地址列表
     *
     * @param customerAddress 客户地址
     * @return 客户地址集合
     */
    List<CustomerAddress> selectAllList(CustomerAddress customerAddress);

    /**
     * 查询列表
     */
    List<CustomerAddress> queryList(CustomerAddress customerAddress);

}
