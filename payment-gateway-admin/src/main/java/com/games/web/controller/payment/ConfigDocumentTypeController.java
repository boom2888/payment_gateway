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
import com.games.payment.domain.ConfigDocumentType;
import com.games.payment.service.IConfigDocumentTypeService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 证件类型配置Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/configDocumentType" )
public class ConfigDocumentTypeController extends BaseController {

    private final IConfigDocumentTypeService iConfigDocumentTypeService;

    /**
     * 查询证件类型配置列表
     */
    @PreAuthorize("@ss.hasPermi('payment:configDocumentType:list')")
    @GetMapping("/list")
    public TableDataInfo list(ConfigDocumentType configDocumentType) {
        startPage();
        List<ConfigDocumentType> list = iConfigDocumentTypeService.selectAllList(configDocumentType);
        return getDataTable(list);
    }

    /**
     * 导出证件类型配置列表
     */
    @PreAuthorize("@ss.hasPermi('payment:configDocumentType:export')" )
    @Log(title = "证件类型配置" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(ConfigDocumentType configDocumentType) {
        List<ConfigDocumentType> list = iConfigDocumentTypeService.queryList(configDocumentType);
        ExcelUtil<ConfigDocumentType> util = new ExcelUtil<ConfigDocumentType>(ConfigDocumentType.class);
        return util.exportExcel(list, "configDocumentType" );
    }

    /**
     * 获取证件类型配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:configDocumentType:query')" )
    @GetMapping(value = "/{code}" )
    public AjaxResult getInfo(@PathVariable("code" ) String code) {
        return AjaxResult.success(iConfigDocumentTypeService.getById(code));
    }

    /**
     * 新增证件类型配置
     */
    @PreAuthorize("@ss.hasPermi('payment:configDocumentType:add')" )
    @Log(title = "证件类型配置" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ConfigDocumentType configDocumentType) {
        return toAjax(iConfigDocumentTypeService.save(configDocumentType) ? 1 : 0);
    }

    /**
     * 修改证件类型配置
     */
    @PreAuthorize("@ss.hasPermi('payment:configDocumentType:edit')" )
    @Log(title = "证件类型配置" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ConfigDocumentType configDocumentType) {
        return toAjax(iConfigDocumentTypeService.updateById(configDocumentType) ? 1 : 0);
    }

    /**
     * 删除证件类型配置
     */
    @PreAuthorize("@ss.hasPermi('payment:configDocumentType:remove')" )
    @Log(title = "证件类型配置" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{codes}" )
    public AjaxResult remove(@PathVariable String[] codes) {
        return toAjax(iConfigDocumentTypeService.removeByIds(Arrays.asList(codes)) ? 1 : 0);
    }
}
