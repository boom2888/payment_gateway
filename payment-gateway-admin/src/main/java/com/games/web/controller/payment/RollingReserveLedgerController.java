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
import com.games.payment.domain.RollingReserveLedger;
import com.games.payment.service.IRollingReserveLedgerService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 滚动准备金分类账Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/rollingReserveLedger" )
public class RollingReserveLedgerController extends BaseController {

    private final IRollingReserveLedgerService iRollingReserveLedgerService;

    /**
     * 查询滚动准备金分类账列表
     */
    @PreAuthorize("@ss.hasPermi('payment:rollingReserveLedger:list')")
    @GetMapping("/list")
    public TableDataInfo list(RollingReserveLedger rollingReserveLedger) {
        startPage();
        List<RollingReserveLedger> list = iRollingReserveLedgerService.selectAllList(rollingReserveLedger);
        return getDataTable(list);
    }

    /**
     * 导出滚动准备金分类账列表
     */
    @PreAuthorize("@ss.hasPermi('payment:rollingReserveLedger:export')" )
    @Log(title = "滚动准备金分类账" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(RollingReserveLedger rollingReserveLedger) {
        List<RollingReserveLedger> list = iRollingReserveLedgerService.queryList(rollingReserveLedger);
        ExcelUtil<RollingReserveLedger> util = new ExcelUtil<RollingReserveLedger>(RollingReserveLedger.class);
        return util.exportExcel(list, "rollingReserveLedger" );
    }

    /**
     * 获取滚动准备金分类账详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:rollingReserveLedger:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iRollingReserveLedgerService.getById(id));
    }

    /**
     * 新增滚动准备金分类账
     */
    @PreAuthorize("@ss.hasPermi('payment:rollingReserveLedger:add')" )
    @Log(title = "滚动准备金分类账" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RollingReserveLedger rollingReserveLedger) {
        return toAjax(iRollingReserveLedgerService.save(rollingReserveLedger) ? 1 : 0);
    }

    /**
     * 修改滚动准备金分类账
     */
    @PreAuthorize("@ss.hasPermi('payment:rollingReserveLedger:edit')" )
    @Log(title = "滚动准备金分类账" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RollingReserveLedger rollingReserveLedger) {
        return toAjax(iRollingReserveLedgerService.updateById(rollingReserveLedger) ? 1 : 0);
    }

    /**
     * 删除滚动准备金分类账
     */
    @PreAuthorize("@ss.hasPermi('payment:rollingReserveLedger:remove')" )
    @Log(title = "滚动准备金分类账" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iRollingReserveLedgerService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
