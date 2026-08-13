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
import com.games.payment.domain.ReportFraudulent;
import com.games.payment.service.IReportFraudulentService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 欺诈报告Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/reportFraudulent" )
public class ReportFraudulentController extends BaseController {

    private final IReportFraudulentService iReportFraudulentService;

    /**
     * 查询欺诈报告列表
     */
    @PreAuthorize("@ss.hasPermi('payment:reportFraudulent:list')")
    @GetMapping("/list")
    public TableDataInfo list(ReportFraudulent reportFraudulent) {
        startPage();
        List<ReportFraudulent> list = iReportFraudulentService.selectAllList(reportFraudulent);
        return getDataTable(list);
    }

    /**
     * 导出欺诈报告列表
     */
    @PreAuthorize("@ss.hasPermi('payment:reportFraudulent:export')" )
    @Log(title = "欺诈报告" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(ReportFraudulent reportFraudulent) {
        List<ReportFraudulent> list = iReportFraudulentService.queryList(reportFraudulent);
        ExcelUtil<ReportFraudulent> util = new ExcelUtil<ReportFraudulent>(ReportFraudulent.class);
        return util.exportExcel(list, "reportFraudulent" );
    }

    /**
     * 获取欺诈报告详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:reportFraudulent:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iReportFraudulentService.getById(id));
    }

    /**
     * 新增欺诈报告
     */
    @PreAuthorize("@ss.hasPermi('payment:reportFraudulent:add')" )
    @Log(title = "欺诈报告" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ReportFraudulent reportFraudulent) {
        return toAjax(iReportFraudulentService.save(reportFraudulent) ? 1 : 0);
    }

    /**
     * 修改欺诈报告
     */
    @PreAuthorize("@ss.hasPermi('payment:reportFraudulent:edit')" )
    @Log(title = "欺诈报告" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ReportFraudulent reportFraudulent) {
        return toAjax(iReportFraudulentService.updateById(reportFraudulent) ? 1 : 0);
    }

    /**
     * 删除欺诈报告
     */
    @PreAuthorize("@ss.hasPermi('payment:reportFraudulent:remove')" )
    @Log(title = "欺诈报告" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iReportFraudulentService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
