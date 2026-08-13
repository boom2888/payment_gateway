package com.games.web.controller.payment;

import java.util.List;
import java.util.Arrays;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.games.common.annotation.Log;
import com.games.common.core.controller.BaseController;
import com.games.common.core.domain.AjaxResult;
import com.games.common.enums.BusinessType;
import com.games.payment.domain.SysJobLog;
import com.games.payment.service.ISysJobLogService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 定时任务调度日志Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/sysJobLog" )
public class SysJobLogController extends BaseController {

    private final ISysJobLogService iSysJobLogService;

    /**
     * 查询定时任务调度日志列表
     */
    @PreAuthorize("@ss.hasPermi('payment:sysJobLog:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysJobLog sysJobLog) {
        startPage();
        List<SysJobLog> list = iSysJobLogService.selectAllList(sysJobLog);
        return getDataTable(list);
    }

    /**
     * 导出定时任务调度日志列表
     */
    @PreAuthorize("@ss.hasPermi('payment:sysJobLog:export')" )
    @Log(title = "定时任务调度日志" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(SysJobLog sysJobLog) {
        List<SysJobLog> list = iSysJobLogService.queryList(sysJobLog);
        ExcelUtil<SysJobLog> util = new ExcelUtil<SysJobLog>(SysJobLog.class);
        return util.exportExcel(list, "sysJobLog" );
    }

    /**
     * 获取定时任务调度日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:sysJobLog:query')" )
    @GetMapping(value = "/{jobLogId}" )
    public AjaxResult getInfo(@PathVariable("jobLogId" ) Long jobLogId) {
        return AjaxResult.success(iSysJobLogService.getById(jobLogId));
    }

    /**
     * 新增定时任务调度日志
     */
    @PreAuthorize("@ss.hasPermi('payment:sysJobLog:add')" )
    @Log(title = "定时任务调度日志" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysJobLog sysJobLog) {
        return toAjax(iSysJobLogService.save(sysJobLog) ? 1 : 0);
    }

    /**
     * 修改定时任务调度日志
     */
    @PreAuthorize("@ss.hasPermi('payment:sysJobLog:edit')" )
    @Log(title = "定时任务调度日志" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysJobLog sysJobLog) {
        return toAjax(iSysJobLogService.updateById(sysJobLog) ? 1 : 0);
    }

    /**
     * 删除定时任务调度日志
     */
    @PreAuthorize("@ss.hasPermi('payment:sysJobLog:remove')" )
    @Log(title = "定时任务调度日志" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{jobLogIds}" )
    public AjaxResult remove(@PathVariable Long[] jobLogIds) {
        return toAjax(iSysJobLogService.removeByIds(Arrays.asList(jobLogIds)) ? 1 : 0);
    }
}
