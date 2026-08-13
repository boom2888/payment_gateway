package com.games.web.controller.payment;

import java.util.Date;
import java.util.List;
import java.util.Arrays;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
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
import com.games.common.utils.MessageUtils;
import com.games.common.enums.BusinessType;
import com.games.payment.domain.ReportQuote;
import com.games.payment.service.IReportQuoteService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 汇率报告Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/reportQuote" )
public class ReportQuoteController extends BaseController {

    private final IReportQuoteService iReportQuoteService;

    /**
     * 查询汇率报告列表
     */
    @PreAuthorize("@ss.hasPermi('payment:reportQuote:list')")
    @GetMapping("/list")
    public TableDataInfo list(ReportQuote reportQuote) {
        startPage();
        List<ReportQuote> list = iReportQuoteService.selectAllList(reportQuote);
        return getDataTable(list);
    }

    /**
     * 导出汇率报告列表
     */
    @PreAuthorize("@ss.hasPermi('payment:reportQuote:export')" )
    @Log(title = "汇率报告" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(ReportQuote reportQuote) {
        List<ReportQuote> list = iReportQuoteService.queryList(reportQuote);
        ExcelUtil<ReportQuote> util = new ExcelUtil<ReportQuote>(ReportQuote.class);
        return util.exportExcel(list, "reportQuote" );
    }

    /**
     * 获取汇率报告详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:reportQuote:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iReportQuoteService.getById(id));
    }

    /**
     * 新增汇率报告
     */
    @PreAuthorize("@ss.hasPermi('payment:reportQuote:add')" )
    @Log(title = "汇率报告" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ReportQuote reportQuote) {
        try {
            reportQuote.setCreatedAt(new Date());
            reportQuote.setUpdatedAt(new Date());
            return toAjax(iReportQuoteService.save(reportQuote) ? 1 : 0);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return AjaxResult.error(MessageUtils.message("report.quote.exists"));
            }
            throw e;
        }

    }

    /**
     * 修改汇率报告
     */
    @PreAuthorize("@ss.hasPermi('payment:reportQuote:edit')" )
    @Log(title = "汇率报告" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ReportQuote reportQuote) {
        return toAjax(iReportQuoteService.updateById(reportQuote) ? 1 : 0);
    }

    /**
     * 删除汇率报告
     */
    @PreAuthorize("@ss.hasPermi('payment:reportQuote:remove')" )
    @Log(title = "汇率报告" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iReportQuoteService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
