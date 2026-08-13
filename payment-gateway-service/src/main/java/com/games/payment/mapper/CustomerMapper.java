package com.games.payment.mapper;

import com.games.pay.dto.CustomerWithUserDto;
import com.games.payment.domain.Customer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 客户Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface CustomerMapper extends BaseMapper<Customer> {
    /**
     * 查询客户列表
     *
     * @param customer 客户
     * @return 客户集合
     */
    List<Customer> selectAllList(Customer customer);

    List<CustomerWithUserDto> selectCustomerWithUser();
}
