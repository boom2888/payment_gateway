package com.games.payment.service;

import com.games.payment.domain.SysJob;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 定时任务调度Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface ISysJobService extends IService<SysJob> {

    /**
     * 查询定时任务调度列表
     *
     * @param sysJob 定时任务调度
     * @return 定时任务调度集合
     */
    List<SysJob> selectAllList(SysJob sysJob);

    /**
     * 查询列表
     */
    List<SysJob> queryList(SysJob sysJob);

}
