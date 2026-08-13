package com.games.web.controller.emi;

import java.util.List;
import java.util.Arrays;

import com.games.payment.domain.EmiCollectionRecord;
import com.games.payment.service.IEmiCollectionRecordService;
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

import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 收款记录Controller
 * 
 * @author Ticker
 * @date 2025-10-16
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/recordA" )
public class EmiCollectionRecordController extends BaseController {

    private final IEmiCollectionRecordService iEmiCollectionRecordService;

    /**
     * 查询收款记录列表
     */
    @PreAuthorize("@ss.hasPermi('payment:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(EmiCollectionRecord emiCollectionRecord) {
        startPage();
        List<EmiCollectionRecord> list = iEmiCollectionRecordService.selectAllList(emiCollectionRecord);
        return getDataTable(list);
    }

    /**
     * 导出收款记录列表
     */
    @PreAuthorize("@ss.hasPermi('payment:record:export')" )
    @Log(title = "收款记录" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(EmiCollectionRecord emiCollectionRecord) {
        List<EmiCollectionRecord> list = iEmiCollectionRecordService.queryList(emiCollectionRecord);
        ExcelUtil<EmiCollectionRecord> util = new ExcelUtil<EmiCollectionRecord>(EmiCollectionRecord.class);
        return util.exportExcel(list, "record" );
    }

    /**
     * 获取收款记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:record:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iEmiCollectionRecordService.getById(id));
    }

    /**
     * 新增收款记录
     */
    @PreAuthorize("@ss.hasPermi('payment:record:add')" )
    @Log(title = "收款记录" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EmiCollectionRecord emiCollectionRecord) {
        return toAjax(iEmiCollectionRecordService.save(emiCollectionRecord) ? 1 : 0);
    }

    /**
     * 修改收款记录
     */
    @PreAuthorize("@ss.hasPermi('payment:record:edit')" )
    @Log(title = "收款记录" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EmiCollectionRecord emiCollectionRecord) {
        return toAjax(iEmiCollectionRecordService.updateById(emiCollectionRecord) ? 1 : 0);
    }

    /**
     * 删除收款记录
     */
    @PreAuthorize("@ss.hasPermi('payment:record:remove')" )
    @Log(title = "收款记录" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iEmiCollectionRecordService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
