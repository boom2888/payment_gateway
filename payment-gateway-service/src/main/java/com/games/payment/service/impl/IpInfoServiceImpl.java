package com.games.payment.service.impl;

import cn.hutool.core.util.StrUtil;
import com.games.common.enums.YesNo;
import com.games.payment.PushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.IpInfoMapper;
import com.games.payment.domain.IpInfo;
import com.games.payment.service.IIpInfoService;

import java.util.Date;
import java.util.List;

/**
 * IP地址Service业务层处理
 *
 * @author Ticker
 * @date 2025-09-24
 */
@Slf4j
@Service
public class IpInfoServiceImpl extends ServiceImpl<IpInfoMapper, IpInfo> implements IIpInfoService {
    @Autowired
    PushService pushService;

    @Override
    public void saveBlackIp(String ip) {
        LambdaQueryWrapper<IpInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(IpInfo::getIp, ip);
        List<IpInfo> ips = list(lqw);
        if (ips.isEmpty()) {
            IpInfo ipInfo = new IpInfo();
            ipInfo.setIp(ip);
            ipInfo.setCreateTime(new Date());
            ipInfo.setUpdateTime(new Date());
            ipInfo.setStatus(YesNo.NO.getValue());
            this.save(ipInfo);
            log.info("保存了黑名单IP:{}", ip);
            pushService.pushSafeMsg(StrUtil.format("【注意】有新的合并单IP需要处理:{}", ip));
        }
    }

    @Override
    public String getBlackIp(String ip) {
        LambdaQueryWrapper<IpInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(IpInfo::getIp, ip);
        lqw.eq(IpInfo::getStatus, YesNo.YES.getValue());
        List<IpInfo> ips = list(lqw);
        if (!ips.isEmpty()) {
            String value = ips.get(0).getValue();
            if (StrUtil.isNotBlank(value)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 查询IP地址列表
     *
     * @param ipInfo IP地址
     * @return IP地址
     */
    @Override
    public List<IpInfo> selectAllList(IpInfo ipInfo)
    {
        return getBaseMapper().selectAllList(ipInfo);
    }


    @Override
    public List<IpInfo> queryList(IpInfo ipInfo) {
        LambdaQueryWrapper<IpInfo> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(ipInfo.getIp())){
            lqw.eq(IpInfo::getIp ,ipInfo.getIp());
        }
        if (StringUtils.isNotBlank(ipInfo.getValue())){
            lqw.eq(IpInfo::getValue ,ipInfo.getValue());
        }
        if (StringUtils.isNotBlank(ipInfo.getStatus())){
            lqw.eq(IpInfo::getStatus ,ipInfo.getStatus());
        }
        return this.list(lqw);
    }

    @Override
    public boolean existsByIp(String ip) {
        if (StringUtils.isBlank(ip)) {
            return false;
        }
        LambdaQueryWrapper<IpInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(IpInfo::getIp, ip);
        return this.count(lqw) > 0;
    }

    @Override
    public boolean existsByIpExceptId(String ip, Long id) {
        if (StringUtils.isBlank(ip)) {
            return false;
        }
        LambdaQueryWrapper<IpInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(IpInfo::getIp, ip);
        if (id != null) {
            lqw.ne(IpInfo::getId, id);
        }
        return this.count(lqw) > 0;
    }

}
