package com.games.payment.mapper;

import com.games.payment.domain.SysJobLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 定时任务调度日志Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface SysJobLogMapper extends BaseMapper<SysJobLog> {
    /**
     * 查询定时任务调度日志列表
     *
     * @param sysJobLog 定时任务调度日志
     * @return 定时任务调度日志集合
     */
    List<SysJobLog> selectAllList(SysJobLog sysJobLog);

}
