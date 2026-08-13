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
import com.games.payment.domain.ConfigMid;
import com.games.payment.service.IConfigMidService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * MID配置Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/configMid" )
public class ConfigMidController extends BaseController {

    private final IConfigMidService iConfigMidService;

    /**
     * 查询MID配置列表
     */
    @PreAuthorize("@ss.hasPermi('payment:configMid:list')")
    @GetMapping("/list")
    public TableDataInfo list(ConfigMid configMid) {
        startPage();
        List<ConfigMid> list = iConfigMidService.selectAllList(configMid);
        return getDataTable(list);
    }

    /**
     * 导出MID配置列表
     */
    @PreAuthorize("@ss.hasPermi('payment:configMid:export')" )
    @Log(title = "MID配置" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(ConfigMid configMid) {
        List<ConfigMid> list = iConfigMidService.queryList(configMid);
        ExcelUtil<ConfigMid> util = new ExcelUtil<ConfigMid>(ConfigMid.class);
        return util.exportExcel(list, "configMid" );
    }

    /**
     * 获取MID配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:configMid:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iConfigMidService.getById(id));
    }

    /**
     * 新增MID配置
     */
    @PreAuthorize("@ss.hasPermi('payment:configMid:add')" )
    @Log(title = "MID配置" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ConfigMid configMid) {
        return toAjax(iConfigMidService.save(configMid) ? 1 : 0);
    }

    /**
     * 修改MID配置
     */
    @PreAuthorize("@ss.hasPermi('payment:configMid:edit')" )
    @Log(title = "MID配置" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ConfigMid configMid) {
        boolean update = iConfigMidService.updateById(configMid);
        return toAjax(update ? 1 : 0);
    }

    /**
     * 删除MID配置
     */
    @PreAuthorize("@ss.hasPermi('payment:configMid:remove')" )
    @Log(title = "MID配置" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iConfigMidService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
