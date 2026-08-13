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
import com.games.payment.domain.OffRampingCustomerWallet;
import com.games.payment.service.IOffRampingCustomerWalletService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 出金客户钱包Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/offRampingCustomerWallet" )
public class OffRampingCustomerWalletController extends BaseController {

    private final IOffRampingCustomerWalletService iOffRampingCustomerWalletService;

    /**
     * 查询出金客户钱包列表
     */
    @PreAuthorize("@ss.hasPermi('payment:offRampingCustomerWallet:list')")
    @GetMapping("/list")
    public TableDataInfo list(OffRampingCustomerWallet offRampingCustomerWallet) {
        startPage();
        List<OffRampingCustomerWallet> list = iOffRampingCustomerWalletService.selectAllList(offRampingCustomerWallet);
        return getDataTable(list);
    }

    /**
     * 导出出金客户钱包列表
     */
    @PreAuthorize("@ss.hasPermi('payment:offRampingCustomerWallet:export')" )
    @Log(title = "出金客户钱包" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(OffRampingCustomerWallet offRampingCustomerWallet) {
        List<OffRampingCustomerWallet> list = iOffRampingCustomerWalletService.queryList(offRampingCustomerWallet);
        ExcelUtil<OffRampingCustomerWallet> util = new ExcelUtil<OffRampingCustomerWallet>(OffRampingCustomerWallet.class);
        return util.exportExcel(list, "offRampingCustomerWallet" );
    }

    /**
     * 获取出金客户钱包详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:offRampingCustomerWallet:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iOffRampingCustomerWalletService.getById(id));
    }

    /**
     * 新增出金客户钱包
     */
    @PreAuthorize("@ss.hasPermi('payment:offRampingCustomerWallet:add')" )
    @Log(title = "出金客户钱包" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OffRampingCustomerWallet offRampingCustomerWallet) {
        return toAjax(iOffRampingCustomerWalletService.save(offRampingCustomerWallet) ? 1 : 0);
    }

    /**
     * 修改出金客户钱包
     */
    @PreAuthorize("@ss.hasPermi('payment:offRampingCustomerWallet:edit')" )
    @Log(title = "出金客户钱包" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OffRampingCustomerWallet offRampingCustomerWallet) {
        return toAjax(iOffRampingCustomerWalletService.updateById(offRampingCustomerWallet) ? 1 : 0);
    }

    /**
     * 删除出金客户钱包
     */
    @PreAuthorize("@ss.hasPermi('payment:offRampingCustomerWallet:remove')" )
    @Log(title = "出金客户钱包" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iOffRampingCustomerWalletService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
