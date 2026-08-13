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
import com.games.payment.domain.FeeType;
import com.games.payment.service.IFeeTypeService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 费用类型Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/feeType" )
public class FeeTypeController extends BaseController {

    private final IFeeTypeService iFeeTypeService;

    /**
     * 查询费用类型列表
     */
    @PreAuthorize("@ss.hasPermi('payment:feeType:list')")
    @GetMapping("/list")
    public TableDataInfo list(FeeType feeType) {
        startPage();
        List<FeeType> list = iFeeTypeService.selectAllList(feeType);
        return getDataTable(list);
    }

    /**
     * 导出费用类型列表
     */
    @PreAuthorize("@ss.hasPermi('payment:feeType:export')" )
    @Log(title = "费用类型" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(FeeType feeType) {
        List<FeeType> list = iFeeTypeService.queryList(feeType);
        ExcelUtil<FeeType> util = new ExcelUtil<FeeType>(FeeType.class);
        return util.exportExcel(list, "feeType" );
    }

    /**
     * 获取费用类型详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:feeType:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iFeeTypeService.getById(id));
    }

    /**
     * 新增费用类型
     */
    @PreAuthorize("@ss.hasPermi('payment:feeType:add')" )
    @Log(title = "费用类型" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FeeType feeType) {
        return toAjax(iFeeTypeService.save(feeType) ? 1 : 0);
    }

    /**
     * 修改费用类型
     */
    @PreAuthorize("@ss.hasPermi('payment:feeType:edit')" )
    @Log(title = "费用类型" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FeeType feeType) {
        return toAjax(iFeeTypeService.updateById(feeType) ? 1 : 0);
    }

    /**
     * 删除费用类型
     */
    @PreAuthorize("@ss.hasPermi('payment:feeType:remove')" )
    @Log(title = "费用类型" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iFeeTypeService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
