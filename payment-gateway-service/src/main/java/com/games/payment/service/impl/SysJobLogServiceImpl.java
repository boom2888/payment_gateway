package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.SysJobLogMapper;
import com.games.payment.domain.SysJobLog;
import com.games.payment.service.ISysJobLogService;

import java.util.List;
import java.util.Map;

/**
 * 定时任务调度日志Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class SysJobLogServiceImpl extends ServiceImpl<SysJobLogMapper, SysJobLog> implements ISysJobLogService {

    /**
     * 查询定时任务调度日志列表
     *
     * @param sysJobLog 定时任务调度日志
     * @return 定时任务调度日志
     */
    @Override
    public List<SysJobLog> selectAllList(SysJobLog sysJobLog)
    {
        return getBaseMapper().selectAllList(sysJobLog);
    }


    @Override
    public List<SysJobLog> queryList(SysJobLog sysJobLog) {
        LambdaQueryWrapper<SysJobLog> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(sysJobLog.getJobName())){
            lqw.like(SysJobLog::getJobName ,sysJobLog.getJobName());
        }
        if (StringUtils.isNotBlank(sysJobLog.getJobGroup())){
            lqw.eq(SysJobLog::getJobGroup ,sysJobLog.getJobGroup());
        }
        if (StringUtils.isNotBlank(sysJobLog.getInvokeTarget())){
            lqw.eq(SysJobLog::getInvokeTarget ,sysJobLog.getInvokeTarget());
        }
        if (StringUtils.isNotBlank(sysJobLog.getJobMessage())){
            lqw.eq(SysJobLog::getJobMessage ,sysJobLog.getJobMessage());
        }
        if (StringUtils.isNotBlank(sysJobLog.getStatus())){
            lqw.eq(SysJobLog::getStatus ,sysJobLog.getStatus());
        }
        if (StringUtils.isNotBlank(sysJobLog.getExceptionInfo())){
            lqw.eq(SysJobLog::getExceptionInfo ,sysJobLog.getExceptionInfo());
        }
        return this.list(lqw);
    }




}
