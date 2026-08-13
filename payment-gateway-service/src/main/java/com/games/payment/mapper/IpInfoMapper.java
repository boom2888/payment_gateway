package com.games.payment.mapper;

import com.games.payment.domain.IpInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * IP地址Mapper接口
 *
 * @author Ticker
 * @date 2025-09-24
 */
public interface IpInfoMapper extends BaseMapper<IpInfo> {
    /**
     * 查询IP地址列表
     *
     * @param ipInfo IP地址
     * @return IP地址集合
     */
    List<IpInfo> selectAllList(IpInfo ipInfo);

}
