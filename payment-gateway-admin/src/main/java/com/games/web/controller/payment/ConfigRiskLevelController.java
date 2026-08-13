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
import com.games.payment.domain.ConfigRiskLevel;
import com.games.payment.service.IConfigRiskLevelService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 风险等级配置Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/configRiskLevel" )
public class ConfigRiskLevelController extends BaseController {

    private final IConfigRiskLevelService iConfigRiskLevelService;

    /**
     * 查询风险等级配置列表
     */
    @PreAuthorize("@ss.hasPermi('payment:configRiskLevel:list')")
    @GetMapping("/list")
    public TableDataInfo list(ConfigRiskLevel configRiskLevel) {
        startPage();
        List<ConfigRiskLevel> list = iConfigRiskLevelService.selectAllList(configRiskLevel);
        return getDataTable(list);
    }

    /**
     * 导出风险等级配置列表
     */
    @PreAuthorize("@ss.hasPermi('payment:configRiskLevel:export')" )
    @Log(title = "风险等级配置" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(ConfigRiskLevel configRiskLevel) {
        List<ConfigRiskLevel> list = iConfigRiskLevelService.queryList(configRiskLevel);
        ExcelUtil<ConfigRiskLevel> util = new ExcelUtil<ConfigRiskLevel>(ConfigRiskLevel.class);
        return util.exportExcel(list, "configRiskLevel" );
    }

    /**
     * 获取风险等级配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:configRiskLevel:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iConfigRiskLevelService.getById(id));
    }

    /**
     * 新增风险等级配置
     */
    @PreAuthorize("@ss.hasPermi('payment:configRiskLevel:add')" )
    @Log(title = "风险等级配置" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ConfigRiskLevel configRiskLevel) {
        return toAjax(iConfigRiskLevelService.save(configRiskLevel) ? 1 : 0);
    }

    /**
     * 修改风险等级配置
     */
    @PreAuthorize("@ss.hasPermi('payment:configRiskLevel:edit')" )
    @Log(title = "风险等级配置" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ConfigRiskLevel configRiskLevel) {
        return toAjax(iConfigRiskLevelService.updateById(configRiskLevel) ? 1 : 0);
    }

    /**
     * 删除风险等级配置
     */
    @PreAuthorize("@ss.hasPermi('payment:configRiskLevel:remove')" )
    @Log(title = "风险等级配置" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iConfigRiskLevelService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
