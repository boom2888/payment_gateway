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
import com.games.payment.domain.SaasCorpCurrency;
import com.games.payment.service.ISaasCorpCurrencyService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * SaaS用户公司支持货币Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/saasCorpCurrency" )
public class SaasCorpCurrencyController extends BaseController {

    private final ISaasCorpCurrencyService iSaasCorpCurrencyService;

    /**
     * 查询SaaS用户公司支持货币列表
     */
    @PreAuthorize("@ss.hasPermi('payment:saasCorpCurrency:list')")
    @GetMapping("/list")
    public TableDataInfo list(SaasCorpCurrency saasCorpCurrency) {
        startPage();
        List<SaasCorpCurrency> list = iSaasCorpCurrencyService.selectAllList(saasCorpCurrency);
        return getDataTable(list);
    }

    /**
     * 导出SaaS用户公司支持货币列表
     */
    @PreAuthorize("@ss.hasPermi('payment:saasCorpCurrency:export')" )
    @Log(title = "SaaS用户公司支持货币" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(SaasCorpCurrency saasCorpCurrency) {
        List<SaasCorpCurrency> list = iSaasCorpCurrencyService.queryList(saasCorpCurrency);
        ExcelUtil<SaasCorpCurrency> util = new ExcelUtil<SaasCorpCurrency>(SaasCorpCurrency.class);
        return util.exportExcel(list, "saasCorpCurrency" );
    }

    /**
     * 获取SaaS用户公司支持货币详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:saasCorpCurrency:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iSaasCorpCurrencyService.getById(id));
    }

    /**
     * 新增SaaS用户公司支持货币
     */
    @PreAuthorize("@ss.hasPermi('payment:saasCorpCurrency:add')" )
    @Log(title = "SaaS用户公司支持货币" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SaasCorpCurrency saasCorpCurrency) {
        return toAjax(iSaasCorpCurrencyService.save(saasCorpCurrency) ? 1 : 0);
    }

    /**
     * 修改SaaS用户公司支持货币
     */
    @PreAuthorize("@ss.hasPermi('payment:saasCorpCurrency:edit')" )
    @Log(title = "SaaS用户公司支持货币" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SaasCorpCurrency saasCorpCurrency) {
        return toAjax(iSaasCorpCurrencyService.updateById(saasCorpCurrency) ? 1 : 0);
    }

    /**
     * 删除SaaS用户公司支持货币
     */
    @PreAuthorize("@ss.hasPermi('payment:saasCorpCurrency:remove')" )
    @Log(title = "SaaS用户公司支持货币" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iSaasCorpCurrencyService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
