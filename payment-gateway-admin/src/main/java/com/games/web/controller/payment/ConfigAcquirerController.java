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
import com.games.payment.domain.ConfigAcquirer;
import com.games.payment.service.IConfigAcquirerService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 收单机构配置Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/configAcquirer" )
public class ConfigAcquirerController extends BaseController {

    private final IConfigAcquirerService iConfigAcquirerService;

    /**
     * 查询收单机构配置列表
     */
    @PreAuthorize("@ss.hasPermi('payment:configAcquirer:list')")
    @GetMapping("/list")
    public TableDataInfo list(ConfigAcquirer configAcquirer) {
        startPage();
        List<ConfigAcquirer> list = iConfigAcquirerService.selectAllList(configAcquirer);
        return getDataTable(list);
    }

    /**
     * 导出收单机构配置列表
     */
    @PreAuthorize("@ss.hasPermi('payment:configAcquirer:export')" )
    @Log(title = "收单机构配置" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(ConfigAcquirer configAcquirer) {
        List<ConfigAcquirer> list = iConfigAcquirerService.queryList(configAcquirer);
        ExcelUtil<ConfigAcquirer> util = new ExcelUtil<ConfigAcquirer>(ConfigAcquirer.class);
        return util.exportExcel(list, "configAcquirer" );
    }

    /**
     * 获取收单机构配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:configAcquirer:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iConfigAcquirerService.getById(id));
    }

    /**
     * 新增收单机构配置
     */
    @PreAuthorize("@ss.hasPermi('payment:configAcquirer:add')" )
    @Log(title = "收单机构配置" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ConfigAcquirer configAcquirer) {
        return toAjax(iConfigAcquirerService.save(configAcquirer) ? 1 : 0);
    }

    /**
     * 修改收单机构配置
     */
    @PreAuthorize("@ss.hasPermi('payment:configAcquirer:edit')" )
    @Log(title = "收单机构配置" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ConfigAcquirer configAcquirer) {
        return toAjax(iConfigAcquirerService.updateById(configAcquirer) ? 1 : 0);
    }

    /**
     * 删除收单机构配置
     */
    @PreAuthorize("@ss.hasPermi('payment:configAcquirer:remove')" )
    @Log(title = "收单机构配置" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iConfigAcquirerService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
