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
import com.games.payment.domain.NuvieTransactionData;
import com.games.payment.service.INuvieTransactionDataService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * Nuvei交易数据Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/nuvieTransactionData" )
public class NuvieTransactionDataController extends BaseController {

    private final INuvieTransactionDataService iNuvieTransactionDataService;

    /**
     * 查询Nuvei交易数据列表
     */
    @PreAuthorize("@ss.hasPermi('payment:nuvieTransactionData:list')")
    @GetMapping("/list")
    public TableDataInfo list(NuvieTransactionData nuvieTransactionData) {
        startPage();
        List<NuvieTransactionData> list = iNuvieTransactionDataService.selectAllList(nuvieTransactionData);
        return getDataTable(list);
    }

    /**
     * 导出Nuvei交易数据列表
     */
    @PreAuthorize("@ss.hasPermi('payment:nuvieTransactionData:export')" )
    @Log(title = "Nuvei交易数据" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(NuvieTransactionData nuvieTransactionData) {
        List<NuvieTransactionData> list = iNuvieTransactionDataService.queryList(nuvieTransactionData);
        ExcelUtil<NuvieTransactionData> util = new ExcelUtil<NuvieTransactionData>(NuvieTransactionData.class);
        return util.exportExcel(list, "nuvieTransactionData" );
    }

    /**
     * 获取Nuvei交易数据详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:nuvieTransactionData:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iNuvieTransactionDataService.getById(id));
    }

    /**
     * 新增Nuvei交易数据
     */
    @PreAuthorize("@ss.hasPermi('payment:nuvieTransactionData:add')" )
    @Log(title = "Nuvei交易数据" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody NuvieTransactionData nuvieTransactionData) {
        return toAjax(iNuvieTransactionDataService.save(nuvieTransactionData) ? 1 : 0);
    }

    /**
     * 修改Nuvei交易数据
     */
    @PreAuthorize("@ss.hasPermi('payment:nuvieTransactionData:edit')" )
    @Log(title = "Nuvei交易数据" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody NuvieTransactionData nuvieTransactionData) {
        return toAjax(iNuvieTransactionDataService.updateById(nuvieTransactionData) ? 1 : 0);
    }

    /**
     * 删除Nuvei交易数据
     */
    @PreAuthorize("@ss.hasPermi('payment:nuvieTransactionData:remove')" )
    @Log(title = "Nuvei交易数据" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iNuvieTransactionDataService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
