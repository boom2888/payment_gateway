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
import com.games.payment.domain.SaasCorpCountry;
import com.games.payment.service.ISaasCorpCountryService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * SaaS用户公司支持国家Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/saasCorpCountry" )
public class SaasCorpCountryController extends BaseController {

    private final ISaasCorpCountryService iSaasCorpCountryService;

    /**
     * 查询SaaS用户公司支持国家列表
     */
    @PreAuthorize("@ss.hasPermi('payment:saasCorpCountry:list')")
    @GetMapping("/list")
    public TableDataInfo list(SaasCorpCountry saasCorpCountry) {
        startPage();
        List<SaasCorpCountry> list = iSaasCorpCountryService.selectAllList(saasCorpCountry);
        return getDataTable(list);
    }

    /**
     * 导出SaaS用户公司支持国家列表
     */
    @PreAuthorize("@ss.hasPermi('payment:saasCorpCountry:export')" )
    @Log(title = "SaaS用户公司支持国家" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(SaasCorpCountry saasCorpCountry) {
        List<SaasCorpCountry> list = iSaasCorpCountryService.queryList(saasCorpCountry);
        ExcelUtil<SaasCorpCountry> util = new ExcelUtil<SaasCorpCountry>(SaasCorpCountry.class);
        return util.exportExcel(list, "saasCorpCountry" );
    }

    /**
     * 获取SaaS用户公司支持国家详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:saasCorpCountry:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iSaasCorpCountryService.getById(id));
    }

    /**
     * 新增SaaS用户公司支持国家
     */
    @PreAuthorize("@ss.hasPermi('payment:saasCorpCountry:add')" )
    @Log(title = "SaaS用户公司支持国家" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SaasCorpCountry saasCorpCountry) {
        return toAjax(iSaasCorpCountryService.save(saasCorpCountry) ? 1 : 0);
    }

    /**
     * 修改SaaS用户公司支持国家
     */
    @PreAuthorize("@ss.hasPermi('payment:saasCorpCountry:edit')" )
    @Log(title = "SaaS用户公司支持国家" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SaasCorpCountry saasCorpCountry) {
        return toAjax(iSaasCorpCountryService.updateById(saasCorpCountry) ? 1 : 0);
    }

    /**
     * 删除SaaS用户公司支持国家
     */
    @PreAuthorize("@ss.hasPermi('payment:saasCorpCountry:remove')" )
    @Log(title = "SaaS用户公司支持国家" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iSaasCorpCountryService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
