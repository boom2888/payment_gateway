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
import com.games.payment.domain.ConfigCountry;
import com.games.payment.service.IConfigCountryService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 国家配置Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/configCountry" )
public class ConfigCountryController extends BaseController {

    private final IConfigCountryService iConfigCountryService;

    /**
     * 查询国家配置列表
     */
    @PreAuthorize("@ss.hasPermi('payment:configCountry:list')")
    @GetMapping("/list")
    public TableDataInfo list(ConfigCountry configCountry) {
        startPage();
        List<ConfigCountry> list = iConfigCountryService.selectAllList(configCountry);
        return getDataTable(list);
    }

    /**
     * 导出国家配置列表
     */
    @PreAuthorize("@ss.hasPermi('payment:configCountry:export')" )
    @Log(title = "国家配置" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(ConfigCountry configCountry) {
        List<ConfigCountry> list = iConfigCountryService.queryList(configCountry);
        ExcelUtil<ConfigCountry> util = new ExcelUtil<ConfigCountry>(ConfigCountry.class);
        return util.exportExcel(list, "configCountry" );
    }

    /**
     * 获取国家配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:configCountry:query')" )
    @GetMapping(value = "/{code}" )
    public AjaxResult getInfo(@PathVariable("code" ) String code) {
        return AjaxResult.success(iConfigCountryService.getById(code));
    }

    /**
     * 新增国家配置
     */
    @PreAuthorize("@ss.hasPermi('payment:configCountry:add')" )
    @Log(title = "国家配置" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ConfigCountry configCountry) {
        return toAjax(iConfigCountryService.save(configCountry) ? 1 : 0);
    }

    /**
     * 修改国家配置
     */
    @PreAuthorize("@ss.hasPermi('payment:configCountry:edit')" )
    @Log(title = "国家配置" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ConfigCountry configCountry) {
        return toAjax(iConfigCountryService.updateById(configCountry) ? 1 : 0);
    }

    /**
     * 删除国家配置
     */
    @PreAuthorize("@ss.hasPermi('payment:configCountry:remove')" )
    @Log(title = "国家配置" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{codes}" )
    public AjaxResult remove(@PathVariable String[] codes) {
        return toAjax(iConfigCountryService.removeByIds(Arrays.asList(codes)) ? 1 : 0);
    }
}
