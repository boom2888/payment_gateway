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
import com.games.payment.domain.AuditLog;
import com.games.payment.service.IAuditLogService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 审计日志Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/log" )
public class AuditLogController extends BaseController {

    private final IAuditLogService iAuditLogService;

    /**
     * 查询审计日志列表
     */
    @PreAuthorize("@ss.hasPermi('payment:log:list')")
    @GetMapping("/list")
    public TableDataInfo list(AuditLog auditLog) {
        startPage();
        List<AuditLog> list = iAuditLogService.selectAllList(auditLog);
        return getDataTable(list);
    }

    /**
     * 导出审计日志列表
     */
    @PreAuthorize("@ss.hasPermi('payment:log:export')" )
    @Log(title = "审计日志" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(AuditLog auditLog) {
        List<AuditLog> list = iAuditLogService.queryList(auditLog);
        ExcelUtil<AuditLog> util = new ExcelUtil<AuditLog>(AuditLog.class);
        return util.exportExcel(list, "log" );
    }

    /**
     * 获取审计日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:log:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iAuditLogService.getById(id));
    }

    /**
     * 新增审计日志
     */
    @PreAuthorize("@ss.hasPermi('payment:log:add')" )
    @Log(title = "审计日志" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AuditLog auditLog) {
        return toAjax(iAuditLogService.save(auditLog) ? 1 : 0);
    }

    /**
     * 修改审计日志
     */
    @PreAuthorize("@ss.hasPermi('payment:log:edit')" )
    @Log(title = "审计日志" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AuditLog auditLog) {
        return toAjax(iAuditLogService.updateById(auditLog) ? 1 : 0);
    }

    /**
     * 删除审计日志
     */
    @PreAuthorize("@ss.hasPermi('payment:log:remove')" )
    @Log(title = "审计日志" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iAuditLogService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
