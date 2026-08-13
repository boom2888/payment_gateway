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
import com.games.payment.domain.ThunesPayer;
import com.games.payment.service.IThunesPayerService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * Thunes付款人Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/thunesPayer" )
public class ThunesPayerController extends BaseController {

    private final IThunesPayerService iThunesPayerService;

    /**
     * 查询Thunes付款人列表
     */
    @PreAuthorize("@ss.hasPermi('payment:thunesPayer:list')")
    @GetMapping("/list")
    public TableDataInfo list(ThunesPayer thunesPayer) {
        startPage();
        List<ThunesPayer> list = iThunesPayerService.list();
        return getDataTable(list);
    }

    /**
     * 导出Thunes付款人列表
     */
    @PreAuthorize("@ss.hasPermi('payment:thunesPayer:export')" )
    @Log(title = "Thunes付款人" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(ThunesPayer thunesPayer) {
        List<ThunesPayer> list = iThunesPayerService.list();
        ExcelUtil<ThunesPayer> util = new ExcelUtil<ThunesPayer>(ThunesPayer.class);
        return util.exportExcel(list, "thunesPayer" );
    }

    /**
     * 获取Thunes付款人详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:thunesPayer:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iThunesPayerService.getById(id));
    }

    /**
     * 新增Thunes付款人
     */
    @PreAuthorize("@ss.hasPermi('payment:thunesPayer:add')" )
    @Log(title = "Thunes付款人" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ThunesPayer thunesPayer) {
        return toAjax(iThunesPayerService.save(thunesPayer) ? 1 : 0);
    }

    /**
     * 修改Thunes付款人
     */
    @PreAuthorize("@ss.hasPermi('payment:thunesPayer:edit')" )
    @Log(title = "Thunes付款人" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ThunesPayer thunesPayer) {
        return toAjax(iThunesPayerService.updateById(thunesPayer) ? 1 : 0);
    }

    /**
     * 删除Thunes付款人
     */
    @PreAuthorize("@ss.hasPermi('payment:thunesPayer:remove')" )
    @Log(title = "Thunes付款人" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iThunesPayerService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
