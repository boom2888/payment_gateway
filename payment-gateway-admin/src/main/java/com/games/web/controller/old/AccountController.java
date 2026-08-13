package com.games.web.controller.old;

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
import com.games.payment.domain.Account;
import com.games.payment.service.IAccountService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 账户Controller
 * 
 * @author Ticker
 * @date 2025-07-07
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/account" )
public class AccountController extends BaseController {

    private final IAccountService iAccountService;

    /**
     * 查询账户列表
     */
    @PreAuthorize("@ss.hasPermi('payment:account:list')")
    @GetMapping("/list")
    public TableDataInfo list(Account account) {
        startPage();
        List<Account> list = iAccountService.selectAllList(account);
        return getDataTable(list);
    }

    /**
     * 导出账户列表
     */
    @PreAuthorize("@ss.hasPermi('payment:account:export')" )
    @Log(title = "账户" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(Account account) {
        List<Account> list = iAccountService.queryList(account);
        ExcelUtil<Account> util = new ExcelUtil<Account>(Account.class);
        return util.exportExcel(list, "account" );
    }

    /**
     * 获取账户详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:account:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iAccountService.getById(id));
    }

    /**
     * 新增账户
     */
    @PreAuthorize("@ss.hasPermi('payment:account:add')" )
    @Log(title = "账户" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Account account) {
        return toAjax(iAccountService.save(account) ? 1 : 0);
    }

    /**
     * 修改账户
     */
    @PreAuthorize("@ss.hasPermi('payment:account:edit')" )
    @Log(title = "账户" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Account account) {
        return toAjax(iAccountService.updateById(account) ? 1 : 0);
    }

    /**
     * 删除账户
     */
    @PreAuthorize("@ss.hasPermi('payment:account:remove')" )
    @Log(title = "账户" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iAccountService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
