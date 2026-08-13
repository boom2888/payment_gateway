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
import com.games.payment.domain.SettlementLedger;
import com.games.payment.service.ISettlementLedgerService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 结算分类账Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/settlementLedger" )
public class SettlementLedgerController extends BaseController {

    private final ISettlementLedgerService iSettlementLedgerService;

    /**
     * 查询结算分类账列表
     */
    @PreAuthorize("@ss.hasPermi('payment:settlementLedger:list')")
    @GetMapping("/list")
    public TableDataInfo list(SettlementLedger settlementLedger) {
        startPage();
        List<SettlementLedger> list = iSettlementLedgerService.selectAllList(settlementLedger);
        return getDataTable(list);
    }

    /**
     * 导出结算分类账列表
     */
    @PreAuthorize("@ss.hasPermi('payment:settlementLedger:export')" )
    @Log(title = "结算分类账" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(SettlementLedger settlementLedger) {
        List<SettlementLedger> list = iSettlementLedgerService.queryList(settlementLedger);
        ExcelUtil<SettlementLedger> util = new ExcelUtil<SettlementLedger>(SettlementLedger.class);
        return util.exportExcel(list, "settlementLedger" );
    }

    /**
     * 获取结算分类账详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:settlementLedger:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iSettlementLedgerService.getById(id));
    }

    /**
     * 新增结算分类账
     */
    @PreAuthorize("@ss.hasPermi('payment:settlementLedger:add')" )
    @Log(title = "结算分类账" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SettlementLedger settlementLedger) {
        return toAjax(iSettlementLedgerService.save(settlementLedger) ? 1 : 0);
    }

    /**
     * 修改结算分类账
     */
    @PreAuthorize("@ss.hasPermi('payment:settlementLedger:edit')" )
    @Log(title = "结算分类账" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SettlementLedger settlementLedger) {
        return toAjax(iSettlementLedgerService.updateById(settlementLedger) ? 1 : 0);
    }

    /**
     * 删除结算分类账
     */
    @PreAuthorize("@ss.hasPermi('payment:settlementLedger:remove')" )
    @Log(title = "结算分类账" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iSettlementLedgerService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
