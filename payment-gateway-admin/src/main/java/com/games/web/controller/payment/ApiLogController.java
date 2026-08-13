package com.games.web.controller.payment;

import java.util.List;
import java.util.Arrays;

import com.games.payment.domain.ApiLog;
import com.games.payment.service.IApiLogService;
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
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 接口日志Controller
 * 
 * @author Ticker
 * @date 2025-08-18
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/apiLog" )
public class ApiLogController extends BaseController {

    private final IApiLogService iApiLogService;

    /**
     * 查询接口日志列表
     */
    @PreAuthorize("@ss.hasPermi('payment:apiLog:list')")
    @GetMapping("/list")
    public TableDataInfo list(ApiLog apiLog) {
        startPage();
        List<ApiLog> list = iApiLogService.selectAllList(apiLog);
        return getDataTable(list);
    }

    /**
     * 导出接口日志列表
     */
    @PreAuthorize("@ss.hasPermi('payment:apiLog:export')" )
    @Log(title = "接口日志" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(ApiLog apiLog) {
        List<ApiLog> list = iApiLogService.queryList(apiLog);
        ExcelUtil<ApiLog> util = new ExcelUtil<ApiLog>(ApiLog.class);
        return util.exportExcel(list, "apiLog" );
    }

    /**
     * 获取接口日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:apiLog:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iApiLogService.getById(id));
    }

    /**
     * 新增接口日志
     */
    @PreAuthorize("@ss.hasPermi('payment:apiLog:add')" )
    @Log(title = "接口日志" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ApiLog apiLog) {
        return toAjax(iApiLogService.save(apiLog) ? 1 : 0);
    }

    /**
     * 修改接口日志
     */
    @PreAuthorize("@ss.hasPermi('payment:apiLog:edit')" )
    @Log(title = "接口日志" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ApiLog apiLog) {
        return toAjax(iApiLogService.updateById(apiLog) ? 1 : 0);
    }

    /**
     * 删除接口日志
     */
    @PreAuthorize("@ss.hasPermi('payment:apiLog:remove')" )
    @Log(title = "接口日志" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iApiLogService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
