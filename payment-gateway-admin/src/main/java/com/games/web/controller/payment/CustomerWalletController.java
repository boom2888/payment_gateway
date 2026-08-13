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
import com.games.payment.domain.CustomerWallet;
import com.games.payment.service.ICustomerWalletService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 客户钱包Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/customerWallet" )
public class CustomerWalletController extends BaseController {

    private final ICustomerWalletService iCustomerWalletService;

    /**
     * 查询客户钱包列表
     */
    @PreAuthorize("@ss.hasPermi('payment:customerWallet:list')")
    @GetMapping("/list")
    public TableDataInfo list(CustomerWallet customerWallet) {
        startPage();
        List<CustomerWallet> list = iCustomerWalletService.selectAllList(customerWallet);
        return getDataTable(list);
    }

    /**
     * 导出客户钱包列表
     */
    @PreAuthorize("@ss.hasPermi('payment:customerWallet:export')" )
    @Log(title = "客户钱包" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(CustomerWallet customerWallet) {
        List<CustomerWallet> list = iCustomerWalletService.queryList(customerWallet);
        ExcelUtil<CustomerWallet> util = new ExcelUtil<CustomerWallet>(CustomerWallet.class);
        return util.exportExcel(list, "customerWallet" );
    }

    /**
     * 获取客户钱包详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:customerWallet:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iCustomerWalletService.getById(id));
    }

    /**
     * 新增客户钱包
     */
    @PreAuthorize("@ss.hasPermi('payment:customerWallet:add')" )
    @Log(title = "客户钱包" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CustomerWallet customerWallet) {
        return toAjax(iCustomerWalletService.save(customerWallet) ? 1 : 0);
    }

    /**
     * 修改客户钱包
     */
    @PreAuthorize("@ss.hasPermi('payment:customerWallet:edit')" )
    @Log(title = "客户钱包" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CustomerWallet customerWallet) {
        return toAjax(iCustomerWalletService.updateById(customerWallet) ? 1 : 0);
    }

    /**
     * 删除客户钱包
     */
    @PreAuthorize("@ss.hasPermi('payment:customerWallet:remove')" )
    @Log(title = "客户钱包" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iCustomerWalletService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
