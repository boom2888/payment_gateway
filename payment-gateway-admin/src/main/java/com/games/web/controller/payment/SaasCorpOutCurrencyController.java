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
import com.games.payment.domain.SaasCorpOutCurrency;
import com.games.payment.service.ISaasCorpOutCurrencyService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * SaaS用户公司出金支持货币Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/saasCorpOutCurrency" )
public class SaasCorpOutCurrencyController extends BaseController {

    private final ISaasCorpOutCurrencyService iSaasCorpOutCurrencyService;

    /**
     * 查询SaaS用户公司出金支持货币列表
     */
    @PreAuthorize("@ss.hasPermi('payment:saasCorpOutCurrency:list')")
    @GetMapping("/list")
    public TableDataInfo list(SaasCorpOutCurrency saasCorpOutCurrency) {
        startPage();
        List<SaasCorpOutCurrency> list = iSaasCorpOutCurrencyService.selectAllList(saasCorpOutCurrency);
        return getDataTable(list);
    }

    /**
     * 导出SaaS用户公司出金支持货币列表
     */
    @PreAuthorize("@ss.hasPermi('payment:saasCorpOutCurrency:export')" )
    @Log(title = "SaaS用户公司出金支持货币" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(SaasCorpOutCurrency saasCorpOutCurrency) {
        List<SaasCorpOutCurrency> list = iSaasCorpOutCurrencyService.queryList(saasCorpOutCurrency);
        ExcelUtil<SaasCorpOutCurrency> util = new ExcelUtil<SaasCorpOutCurrency>(SaasCorpOutCurrency.class);
        return util.exportExcel(list, "saasCorpOutCurrency" );
    }

    /**
     * 获取SaaS用户公司出金支持货币详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:saasCorpOutCurrency:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iSaasCorpOutCurrencyService.getById(id));
    }

    /**
     * 新增SaaS用户公司出金支持货币
     */
    @PreAuthorize("@ss.hasPermi('payment:saasCorpOutCurrency:add')" )
    @Log(title = "SaaS用户公司出金支持货币" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SaasCorpOutCurrency saasCorpOutCurrency) {
        return toAjax(iSaasCorpOutCurrencyService.save(saasCorpOutCurrency) ? 1 : 0);
    }

    /**
     * 修改SaaS用户公司出金支持货币
     */
    @PreAuthorize("@ss.hasPermi('payment:saasCorpOutCurrency:edit')" )
    @Log(title = "SaaS用户公司出金支持货币" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SaasCorpOutCurrency saasCorpOutCurrency) {
        return toAjax(iSaasCorpOutCurrencyService.updateById(saasCorpOutCurrency) ? 1 : 0);
    }

    /**
     * 删除SaaS用户公司出金支持货币
     */
    @PreAuthorize("@ss.hasPermi('payment:saasCorpOutCurrency:remove')" )
    @Log(title = "SaaS用户公司出金支持货币" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iSaasCorpOutCurrencyService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
