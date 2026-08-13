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
import com.games.payment.domain.ConfigCheckoutErrorMapping;
import com.games.payment.service.IConfigCheckoutErrorMappingService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * Checkout错误映射配置Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/configCheckoutErrorMapping" )
public class ConfigCheckoutErrorMappingController extends BaseController {

    private final IConfigCheckoutErrorMappingService iConfigCheckoutErrorMappingService;

    /**
     * 查询Checkout错误映射配置列表
     */
    @PreAuthorize("@ss.hasPermi('payment:configCheckoutErrorMapping:list')")
    @GetMapping("/list")
    public TableDataInfo list(ConfigCheckoutErrorMapping configCheckoutErrorMapping) {
        startPage();
        List<ConfigCheckoutErrorMapping> list = iConfigCheckoutErrorMappingService.selectAllList(configCheckoutErrorMapping);
        return getDataTable(list);
    }

    /**
     * 导出Checkout错误映射配置列表
     */
    @PreAuthorize("@ss.hasPermi('payment:configCheckoutErrorMapping:export')" )
    @Log(title = "Checkout错误映射配置" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(ConfigCheckoutErrorMapping configCheckoutErrorMapping) {
        List<ConfigCheckoutErrorMapping> list = iConfigCheckoutErrorMappingService.queryList(configCheckoutErrorMapping);
        ExcelUtil<ConfigCheckoutErrorMapping> util = new ExcelUtil<ConfigCheckoutErrorMapping>(ConfigCheckoutErrorMapping.class);
        return util.exportExcel(list, "configCheckoutErrorMapping" );
    }

    /**
     * 获取Checkout错误映射配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:configCheckoutErrorMapping:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iConfigCheckoutErrorMappingService.getById(id));
    }

    /**
     * 新增Checkout错误映射配置
     */
    @PreAuthorize("@ss.hasPermi('payment:configCheckoutErrorMapping:add')" )
    @Log(title = "Checkout错误映射配置" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ConfigCheckoutErrorMapping configCheckoutErrorMapping) {
        return toAjax(iConfigCheckoutErrorMappingService.save(configCheckoutErrorMapping) ? 1 : 0);
    }

    /**
     * 修改Checkout错误映射配置
     */
    @PreAuthorize("@ss.hasPermi('payment:configCheckoutErrorMapping:edit')" )
    @Log(title = "Checkout错误映射配置" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ConfigCheckoutErrorMapping configCheckoutErrorMapping) {
        return toAjax(iConfigCheckoutErrorMappingService.updateById(configCheckoutErrorMapping) ? 1 : 0);
    }

    /**
     * 删除Checkout错误映射配置
     */
    @PreAuthorize("@ss.hasPermi('payment:configCheckoutErrorMapping:remove')" )
    @Log(title = "Checkout错误映射配置" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iConfigCheckoutErrorMappingService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
