package com.games.payment.service;

import com.games.payment.domain.Customer;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 客户Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface ICustomerService extends IService<Customer> {

    Customer getBySsoCustomerId(Long shopId, String ssoCustomerId);
    /**
     * 查询客户列表
     *
     * @param customer 客户
     * @return 客户集合
     */
    List<Customer> selectAllList(Customer customer);

    /**
     * 查询列表
     */
    List<Customer> queryList(Customer customer);

}
