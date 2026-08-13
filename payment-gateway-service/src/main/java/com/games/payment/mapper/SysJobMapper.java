package com.games.payment.mapper;

import com.games.payment.domain.SysJob;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 定时任务调度Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface SysJobMapper extends BaseMapper<SysJob> {
    /**
     * 查询定时任务调度列表
     *
     * @param sysJob 定时任务调度
     * @return 定时任务调度集合
     */
    List<SysJob> selectAllList(SysJob sysJob);

}
