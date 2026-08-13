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
import com.games.payment.domain.SaasCorpCryptoCurrency;
import com.games.payment.service.ISaasCorpCryptoCurrencyService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * SaaS用户公司支持加密货币Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/saasCorpCryptoCurrency" )
public class SaasCorpCryptoCurrencyController extends BaseController {

    private final ISaasCorpCryptoCurrencyService iSaasCorpCryptoCurrencyService;

    /**
     * 查询SaaS用户公司支持加密货币列表
     */
    @PreAuthorize("@ss.hasPermi('payment:saasCorpCryptoCurrency:list')")
    @GetMapping("/list")
    public TableDataInfo list(SaasCorpCryptoCurrency saasCorpCryptoCurrency) {
        startPage();
        List<SaasCorpCryptoCurrency> list = iSaasCorpCryptoCurrencyService.selectAllList(saasCorpCryptoCurrency);
        return getDataTable(list);
    }

    /**
     * 导出SaaS用户公司支持加密货币列表
     */
    @PreAuthorize("@ss.hasPermi('payment:saasCorpCryptoCurrency:export')" )
    @Log(title = "SaaS用户公司支持加密货币" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(SaasCorpCryptoCurrency saasCorpCryptoCurrency) {
        List<SaasCorpCryptoCurrency> list = iSaasCorpCryptoCurrencyService.queryList(saasCorpCryptoCurrency);
        ExcelUtil<SaasCorpCryptoCurrency> util = new ExcelUtil<SaasCorpCryptoCurrency>(SaasCorpCryptoCurrency.class);
        return util.exportExcel(list, "saasCorpCryptoCurrency" );
    }

    /**
     * 获取SaaS用户公司支持加密货币详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:saasCorpCryptoCurrency:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iSaasCorpCryptoCurrencyService.getById(id));
    }

    /**
     * 新增SaaS用户公司支持加密货币
     */
    @PreAuthorize("@ss.hasPermi('payment:saasCorpCryptoCurrency:add')" )
    @Log(title = "SaaS用户公司支持加密货币" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SaasCorpCryptoCurrency saasCorpCryptoCurrency) {
        return toAjax(iSaasCorpCryptoCurrencyService.save(saasCorpCryptoCurrency) ? 1 : 0);
    }

    /**
     * 修改SaaS用户公司支持加密货币
     */
    @PreAuthorize("@ss.hasPermi('payment:saasCorpCryptoCurrency:edit')" )
    @Log(title = "SaaS用户公司支持加密货币" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SaasCorpCryptoCurrency saasCorpCryptoCurrency) {
        return toAjax(iSaasCorpCryptoCurrencyService.updateById(saasCorpCryptoCurrency) ? 1 : 0);
    }

    /**
     * 删除SaaS用户公司支持加密货币
     */
    @PreAuthorize("@ss.hasPermi('payment:saasCorpCryptoCurrency:remove')" )
    @Log(title = "SaaS用户公司支持加密货币" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iSaasCorpCryptoCurrencyService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
