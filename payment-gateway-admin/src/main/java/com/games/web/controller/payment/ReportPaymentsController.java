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
import com.games.payment.domain.ReportPayments;
import com.games.payment.service.IReportPaymentsService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 支付报告Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/reportPayments" )
public class ReportPaymentsController extends BaseController {

    private final IReportPaymentsService iReportPaymentsService;

    /**
     * 查询支付报告列表
     */
    @PreAuthorize("@ss.hasPermi('payment:reportPayments:list')")
    @GetMapping("/list")
    public TableDataInfo list(ReportPayments reportPayments) {
        startPage();
        List<ReportPayments> list = iReportPaymentsService.selectAllList(reportPayments);
        return getDataTable(list);
    }

    /**
     * 导出支付报告列表
     */
    @PreAuthorize("@ss.hasPermi('payment:reportPayments:export')" )
    @Log(title = "支付报告" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(ReportPayments reportPayments) {
        List<ReportPayments> list = iReportPaymentsService.queryList(reportPayments);
        ExcelUtil<ReportPayments> util = new ExcelUtil<ReportPayments>(ReportPayments.class);
        return util.exportExcel(list, "reportPayments" );
    }

    /**
     * 获取支付报告详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:reportPayments:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iReportPaymentsService.getById(id));
    }

    /**
     * 新增支付报告
     */
    @PreAuthorize("@ss.hasPermi('payment:reportPayments:add')" )
    @Log(title = "支付报告" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ReportPayments reportPayments) {
        return toAjax(iReportPaymentsService.save(reportPayments) ? 1 : 0);
    }

    /**
     * 修改支付报告
     */
    @PreAuthorize("@ss.hasPermi('payment:reportPayments:edit')" )
    @Log(title = "支付报告" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ReportPayments reportPayments) {
        return toAjax(iReportPaymentsService.updateById(reportPayments) ? 1 : 0);
    }

    /**
     * 删除支付报告
     */
    @PreAuthorize("@ss.hasPermi('payment:reportPayments:remove')" )
    @Log(title = "支付报告" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iReportPaymentsService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
