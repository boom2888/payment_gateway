package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.SysJobMapper;
import com.games.payment.domain.SysJob;
import com.games.payment.service.ISysJobService;

import java.util.List;
import java.util.Map;

/**
 * 定时任务调度Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class SysJobServiceImpl extends ServiceImpl<SysJobMapper, SysJob> implements ISysJobService {

    /**
     * 查询定时任务调度列表
     *
     * @param sysJob 定时任务调度
     * @return 定时任务调度
     */
    @Override
    public List<SysJob> selectAllList(SysJob sysJob)
    {
        return getBaseMapper().selectAllList(sysJob);
    }


    @Override
    public List<SysJob> queryList(SysJob sysJob) {
        LambdaQueryWrapper<SysJob> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(sysJob.getInvokeTarget())){
            lqw.eq(SysJob::getInvokeTarget ,sysJob.getInvokeTarget());
        }
        if (StringUtils.isNotBlank(sysJob.getCronExpression())){
            lqw.eq(SysJob::getCronExpression ,sysJob.getCronExpression());
        }
        if (StringUtils.isNotBlank(sysJob.getMisfirePolicy())){
            lqw.eq(SysJob::getMisfirePolicy ,sysJob.getMisfirePolicy());
        }
        if (StringUtils.isNotBlank(sysJob.getConcurrent())){
            lqw.eq(SysJob::getConcurrent ,sysJob.getConcurrent());
        }
        if (StringUtils.isNotBlank(sysJob.getStatus())){
            lqw.eq(SysJob::getStatus ,sysJob.getStatus());
        }
        return this.list(lqw);
    }




}
