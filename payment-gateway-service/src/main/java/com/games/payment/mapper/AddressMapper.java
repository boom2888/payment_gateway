package com.games.payment.mapper;

import com.games.payment.domain.Address;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 地址Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface AddressMapper extends BaseMapper<Address> {
    /**
     * 查询地址列表
     *
     * @param address 地址
     * @return 地址集合
     */
    List<Address> selectAllList(Address address);

}
