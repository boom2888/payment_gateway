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
import com.games.payment.domain.SaasUserCorporation;
import com.games.payment.service.ISaasUserCorporationService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * SaaS用户公司Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/saasUserCorporation" )
public class SaasUserCorporationController extends BaseController {

    private final ISaasUserCorporationService iSaasUserCorporationService;

    /**
     * 查询SaaS用户公司列表
     */
    @PreAuthorize("@ss.hasPermi('payment:saasUserCorporation:list')")
    @GetMapping("/list")
    public TableDataInfo list(SaasUserCorporation saasUserCorporation) {
        startPage();
        List<SaasUserCorporation> list = iSaasUserCorporationService.selectAllList(saasUserCorporation);
        return getDataTable(list);
    }

    /**
     * 导出SaaS用户公司列表
     */
    @PreAuthorize("@ss.hasPermi('payment:saasUserCorporation:export')" )
    @Log(title = "SaaS用户公司" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(SaasUserCorporation saasUserCorporation) {
        List<SaasUserCorporation> list = iSaasUserCorporationService.queryList(saasUserCorporation);
        ExcelUtil<SaasUserCorporation> util = new ExcelUtil<SaasUserCorporation>(SaasUserCorporation.class);
        return util.exportExcel(list, "saasUserCorporation" );
    }

    /**
     * 获取SaaS用户公司详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:saasUserCorporation:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iSaasUserCorporationService.getById(id));
    }

    /**
     * 新增SaaS用户公司
     */
    @PreAuthorize("@ss.hasPermi('payment:saasUserCorporation:add')" )
    @Log(title = "SaaS用户公司" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SaasUserCorporation saasUserCorporation) {
        return toAjax(iSaasUserCorporationService.save(saasUserCorporation) ? 1 : 0);
    }

    /**
     * 修改SaaS用户公司
     */
    @PreAuthorize("@ss.hasPermi('payment:saasUserCorporation:edit')" )
    @Log(title = "SaaS用户公司" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SaasUserCorporation saasUserCorporation) {
        return toAjax(iSaasUserCorporationService.updateById(saasUserCorporation) ? 1 : 0);
    }

    /**
     * 删除SaaS用户公司
     */
    @PreAuthorize("@ss.hasPermi('payment:saasUserCorporation:remove')" )
    @Log(title = "SaaS用户公司" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iSaasUserCorporationService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
