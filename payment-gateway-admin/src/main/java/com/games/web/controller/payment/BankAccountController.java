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
import com.games.payment.domain.BankAccount;
import com.games.payment.service.IBankAccountService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 银行账户Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/bankAccount" )
public class BankAccountController extends BaseController {

    private final IBankAccountService iBankAccountService;

    /**
     * 查询银行账户列表
     */
    @PreAuthorize("@ss.hasPermi('payment:bankAccount:list')")
    @GetMapping("/list")
    public TableDataInfo list(BankAccount bankAccount) {
        startPage();
        List<BankAccount> list = iBankAccountService.selectAllList(bankAccount);
        return getDataTable(list);
    }

    /**
     * 导出银行账户列表
     */
    @PreAuthorize("@ss.hasPermi('payment:bankAccount:export')" )
    @Log(title = "银行账户" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(BankAccount bankAccount) {
        List<BankAccount> list = iBankAccountService.queryList(bankAccount);
        ExcelUtil<BankAccount> util = new ExcelUtil<BankAccount>(BankAccount.class);
        return util.exportExcel(list, "bankAccount" );
    }

    /**
     * 获取银行账户详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:bankAccount:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iBankAccountService.getById(id));
    }

    /**
     * 新增银行账户
     */
    @PreAuthorize("@ss.hasPermi('payment:bankAccount:add')" )
    @Log(title = "银行账户" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BankAccount bankAccount) {
        return toAjax(iBankAccountService.save(bankAccount) ? 1 : 0);
    }

    /**
     * 修改银行账户
     */
    @PreAuthorize("@ss.hasPermi('payment:bankAccount:edit')" )
    @Log(title = "银行账户" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BankAccount bankAccount) {
        return toAjax(iBankAccountService.updateById(bankAccount) ? 1 : 0);
    }

    /**
     * 删除银行账户
     */
    @PreAuthorize("@ss.hasPermi('payment:bankAccount:remove')" )
    @Log(title = "银行账户" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iBankAccountService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
