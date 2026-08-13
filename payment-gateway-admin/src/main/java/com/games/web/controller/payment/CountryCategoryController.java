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
import com.games.payment.domain.CountryCategory;
import com.games.payment.service.ICountryCategoryService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 欧洲国家的地区Controller
 * 
 * @author Ticker
 * @date 2025-07-22
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/category" )
public class CountryCategoryController extends BaseController {

    private final ICountryCategoryService iCountryCategoryService;

    /**
     * 查询欧洲国家的地区列表
     */
    @PreAuthorize("@ss.hasPermi('payment:category:list')")
    @GetMapping("/list")
    public TableDataInfo list(CountryCategory countryCategory) {
        startPage();
        List<CountryCategory> list = iCountryCategoryService.selectAllList(countryCategory);
        return getDataTable(list);
    }

    /**
     * 导出欧洲国家的地区列表
     */
    @PreAuthorize("@ss.hasPermi('payment:category:export')" )
    @Log(title = "欧洲国家的地区" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(CountryCategory countryCategory) {
        List<CountryCategory> list = iCountryCategoryService.queryList(countryCategory);
        ExcelUtil<CountryCategory> util = new ExcelUtil<CountryCategory>(CountryCategory.class);
        return util.exportExcel(list, "category" );
    }

    /**
     * 获取欧洲国家的地区详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:category:query')" )
    @GetMapping(value = "/{country}" )
    public AjaxResult getInfo(@PathVariable("country" ) String country) {
        return AjaxResult.success(iCountryCategoryService.getById(country));
    }

    /**
     * 新增欧洲国家的地区
     */
    @PreAuthorize("@ss.hasPermi('payment:category:add')" )
    @Log(title = "欧洲国家的地区" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CountryCategory countryCategory) {
        return toAjax(iCountryCategoryService.save(countryCategory) ? 1 : 0);
    }

    /**
     * 修改欧洲国家的地区
     */
    @PreAuthorize("@ss.hasPermi('payment:category:edit')" )
    @Log(title = "欧洲国家的地区" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CountryCategory countryCategory) {
        return toAjax(iCountryCategoryService.updateById(countryCategory) ? 1 : 0);
    }

    /**
     * 删除欧洲国家的地区
     */
    @PreAuthorize("@ss.hasPermi('payment:category:remove')" )
    @Log(title = "欧洲国家的地区" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{countrys}" )
    public AjaxResult remove(@PathVariable String[] countrys) {
        return toAjax(iCountryCategoryService.removeByIds(Arrays.asList(countrys)) ? 1 : 0);
    }
}
