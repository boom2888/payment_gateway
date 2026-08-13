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
import com.games.payment.domain.ConfigContinent;
import com.games.payment.service.IConfigContinentService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 大洲配置Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/configContinent" )
public class ConfigContinentController extends BaseController {

    private final IConfigContinentService iConfigContinentService;

    /**
     * 查询大洲配置列表
     */
    @PreAuthorize("@ss.hasPermi('payment:configContinent:list')")
    @GetMapping("/list")
    public TableDataInfo list(ConfigContinent configContinent) {
        startPage();
        List<ConfigContinent> list = iConfigContinentService.selectAllList(configContinent);
        return getDataTable(list);
    }

    /**
     * 导出大洲配置列表
     */
    @PreAuthorize("@ss.hasPermi('payment:configContinent:export')" )
    @Log(title = "大洲配置" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(ConfigContinent configContinent) {
        List<ConfigContinent> list = iConfigContinentService.queryList(configContinent);
        ExcelUtil<ConfigContinent> util = new ExcelUtil<ConfigContinent>(ConfigContinent.class);
        return util.exportExcel(list, "configContinent" );
    }

    /**
     * 获取大洲配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:configContinent:query')" )
    @GetMapping(value = "/{code}" )
    public AjaxResult getInfo(@PathVariable("code" ) String code) {
        return AjaxResult.success(iConfigContinentService.getById(code));
    }

    /**
     * 新增大洲配置
     */
    @PreAuthorize("@ss.hasPermi('payment:configContinent:add')" )
    @Log(title = "大洲配置" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ConfigContinent configContinent) {
        return toAjax(iConfigContinentService.save(configContinent) ? 1 : 0);
    }

    /**
     * 修改大洲配置
     */
    @PreAuthorize("@ss.hasPermi('payment:configContinent:edit')" )
    @Log(title = "大洲配置" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ConfigContinent configContinent) {
        return toAjax(iConfigContinentService.updateById(configContinent) ? 1 : 0);
    }

    /**
     * 删除大洲配置
     */
    @PreAuthorize("@ss.hasPermi('payment:configContinent:remove')" )
    @Log(title = "大洲配置" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{codes}" )
    public AjaxResult remove(@PathVariable String[] codes) {
        return toAjax(iConfigContinentService.removeByIds(Arrays.asList(codes)) ? 1 : 0);
    }
}
