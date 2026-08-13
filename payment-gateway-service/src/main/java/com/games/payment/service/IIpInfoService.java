package com.games.payment.service;

import com.games.payment.domain.IpInfo;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * IP地址Service接口
 *
 * @author Ticker
 * @date 2025-09-24
 */
public interface IIpInfoService extends IService<IpInfo> {

    //保存黑名单卡
    void saveBlackIp(String ip);
    //读区识别替换黑名单卡
    String getBlackIp(String ip);

    /**
     * 查询IP地址列表
     *
     * @param ipInfo IP地址
     * @return IP地址集合
     */
    List<IpInfo> selectAllList(IpInfo ipInfo);

    /**
     * 查询列表
     */
    List<IpInfo> queryList(IpInfo ipInfo);

    /**
     * 检查IP是否已存在
     */
    boolean existsByIp(String ip);

    /**
     * 检查除指定ID外IP是否存在
     */
    boolean existsByIpExceptId(String ip, Long id);

}
