package com.games.payment.service;

import com.games.payment.domain.SysJobLog;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 定时任务调度日志Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface ISysJobLogService extends IService<SysJobLog> {

    /**
     * 查询定时任务调度日志列表
     *
     * @param sysJobLog 定时任务调度日志
     * @return 定时任务调度日志集合
     */
    List<SysJobLog> selectAllList(SysJobLog sysJobLog);

    /**
     * 查询列表
     */
    List<SysJobLog> queryList(SysJobLog sysJobLog);

}
