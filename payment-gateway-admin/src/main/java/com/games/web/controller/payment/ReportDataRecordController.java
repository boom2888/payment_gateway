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
import com.games.payment.domain.ReportDataRecord;
import com.games.payment.service.IReportDataRecordService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 报告数据记录Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/reportDataRecord" )
public class ReportDataRecordController extends BaseController {

    private final IReportDataRecordService iReportDataRecordService;

    /**
     * 查询报告数据记录列表
     */
    @PreAuthorize("@ss.hasPermi('payment:reportDataRecord:list')")
    @GetMapping("/list")
    public TableDataInfo list(ReportDataRecord reportDataRecord) {
        startPage();
        List<ReportDataRecord> list = iReportDataRecordService.selectAllList(reportDataRecord);
        return getDataTable(list);
    }

    /**
     * 导出报告数据记录列表
     */
    @PreAuthorize("@ss.hasPermi('payment:reportDataRecord:export')" )
    @Log(title = "报告数据记录" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(ReportDataRecord reportDataRecord) {
        List<ReportDataRecord> list = iReportDataRecordService.queryList(reportDataRecord);
        ExcelUtil<ReportDataRecord> util = new ExcelUtil<ReportDataRecord>(ReportDataRecord.class);
        return util.exportExcel(list, "reportDataRecord" );
    }

    /**
     * 获取报告数据记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:reportDataRecord:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iReportDataRecordService.getById(id));
    }

    /**
     * 新增报告数据记录
     */
    @PreAuthorize("@ss.hasPermi('payment:reportDataRecord:add')" )
    @Log(title = "报告数据记录" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ReportDataRecord reportDataRecord) {
        return toAjax(iReportDataRecordService.save(reportDataRecord) ? 1 : 0);
    }

    /**
     * 修改报告数据记录
     */
    @PreAuthorize("@ss.hasPermi('payment:reportDataRecord:edit')" )
    @Log(title = "报告数据记录" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ReportDataRecord reportDataRecord) {
        return toAjax(iReportDataRecordService.updateById(reportDataRecord) ? 1 : 0);
    }

    /**
     * 删除报告数据记录
     */
    @PreAuthorize("@ss.hasPermi('payment:reportDataRecord:remove')" )
    @Log(title = "报告数据记录" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iReportDataRecordService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
