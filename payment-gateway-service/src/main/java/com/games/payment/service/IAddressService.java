package com.games.payment.service;

import com.games.payment.domain.Address;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 地址Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface IAddressService extends IService<Address> {

    /**
     * 查询地址列表
     *
     * @param address 地址
     * @return 地址集合
     */
    List<Address> selectAllList(Address address);

    /**
     * 查询列表
     */
    List<Address> queryList(Address address);

}
