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
import com.games.payment.domain.SysJob;
import com.games.payment.service.ISysJobService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 定时任务调度Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/sysJob" )
public class SysJobController extends BaseController {

    private final ISysJobService iSysJobService;

    /**
     * 查询定时任务调度列表
     */
    @PreAuthorize("@ss.hasPermi('payment:sysJob:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysJob sysJob) {
        startPage();
        List<SysJob> list = iSysJobService.selectAllList(sysJob);
        return getDataTable(list);
    }

    /**
     * 导出定时任务调度列表
     */
    @PreAuthorize("@ss.hasPermi('payment:sysJob:export')" )
    @Log(title = "定时任务调度" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(SysJob sysJob) {
        List<SysJob> list = iSysJobService.queryList(sysJob);
        ExcelUtil<SysJob> util = new ExcelUtil<SysJob>(SysJob.class);
        return util.exportExcel(list, "sysJob" );
    }

    /**
     * 获取定时任务调度详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:sysJob:query')" )
    @GetMapping(value = "/{jobId}" )
    public AjaxResult getInfo(@PathVariable("jobId" ) Long jobId) {
        return AjaxResult.success(iSysJobService.getById(jobId));
    }

    /**
     * 新增定时任务调度
     */
    @PreAuthorize("@ss.hasPermi('payment:sysJob:add')" )
    @Log(title = "定时任务调度" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysJob sysJob) {
        return toAjax(iSysJobService.save(sysJob) ? 1 : 0);
    }

    /**
     * 修改定时任务调度
     */
    @PreAuthorize("@ss.hasPermi('payment:sysJob:edit')" )
    @Log(title = "定时任务调度" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysJob sysJob) {
        return toAjax(iSysJobService.updateById(sysJob) ? 1 : 0);
    }

    /**
     * 删除定时任务调度
     */
    @PreAuthorize("@ss.hasPermi('payment:sysJob:remove')" )
    @Log(title = "定时任务调度" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{jobIds}" )
    public AjaxResult remove(@PathVariable Long[] jobIds) {
        return toAjax(iSysJobService.removeByIds(Arrays.asList(jobIds)) ? 1 : 0);
    }
}
