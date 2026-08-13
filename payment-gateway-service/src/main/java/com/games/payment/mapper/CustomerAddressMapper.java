package com.games.payment.mapper;

import com.games.payment.domain.CustomerAddress;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 客户地址Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface CustomerAddressMapper extends BaseMapper<CustomerAddress> {
    /**
     * 查询客户地址列表
     *
     * @param customerAddress 客户地址
     * @return 客户地址集合
     */
    List<CustomerAddress> selectAllList(CustomerAddress customerAddress);

}
