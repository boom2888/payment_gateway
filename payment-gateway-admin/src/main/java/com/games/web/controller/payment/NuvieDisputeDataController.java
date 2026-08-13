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
import com.games.payment.domain.NuvieDisputeData;
import com.games.payment.service.INuvieDisputeDataService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * Nuvei争议数据Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/nuvieDisputeData" )
public class NuvieDisputeDataController extends BaseController {

    private final INuvieDisputeDataService iNuvieDisputeDataService;

    /**
     * 查询Nuvei争议数据列表
     */
    @PreAuthorize("@ss.hasPermi('payment:nuvieDisputeData:list')")
    @GetMapping("/list")
    public TableDataInfo list(NuvieDisputeData nuvieDisputeData) {
        startPage();
        List<NuvieDisputeData> list = iNuvieDisputeDataService.selectAllList(nuvieDisputeData);
        return getDataTable(list);
    }

    /**
     * 导出Nuvei争议数据列表
     */
    @PreAuthorize("@ss.hasPermi('payment:nuvieDisputeData:export')" )
    @Log(title = "Nuvei争议数据" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(NuvieDisputeData nuvieDisputeData) {
        List<NuvieDisputeData> list = iNuvieDisputeDataService.queryList(nuvieDisputeData);
        ExcelUtil<NuvieDisputeData> util = new ExcelUtil<NuvieDisputeData>(NuvieDisputeData.class);
        return util.exportExcel(list, "nuvieDisputeData" );
    }

    /**
     * 获取Nuvei争议数据详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:nuvieDisputeData:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iNuvieDisputeDataService.getById(id));
    }

    /**
     * 新增Nuvei争议数据
     */
    @PreAuthorize("@ss.hasPermi('payment:nuvieDisputeData:add')" )
    @Log(title = "Nuvei争议数据" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody NuvieDisputeData nuvieDisputeData) {
        return toAjax(iNuvieDisputeDataService.save(nuvieDisputeData) ? 1 : 0);
    }

    /**
     * 修改Nuvei争议数据
     */
    @PreAuthorize("@ss.hasPermi('payment:nuvieDisputeData:edit')" )
    @Log(title = "Nuvei争议数据" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody NuvieDisputeData nuvieDisputeData) {
        return toAjax(iNuvieDisputeDataService.updateById(nuvieDisputeData) ? 1 : 0);
    }

    /**
     * 删除Nuvei争议数据
     */
    @PreAuthorize("@ss.hasPermi('payment:nuvieDisputeData:remove')" )
    @Log(title = "Nuvei争议数据" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iNuvieDisputeDataService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
