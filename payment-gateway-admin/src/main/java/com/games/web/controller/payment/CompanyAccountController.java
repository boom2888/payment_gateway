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
import com.games.payment.domain.CompanyAccount;
import com.games.payment.service.ICompanyAccountService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 公司账户Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/companyAccount" )
public class CompanyAccountController extends BaseController {

    private final ICompanyAccountService iCompanyAccountService;

    /**
     * 查询公司账户列表
     */
    @PreAuthorize("@ss.hasPermi('payment:companyAccount:list')")
    @GetMapping("/list")
    public TableDataInfo list(CompanyAccount companyAccount) {
        startPage();
        List<CompanyAccount> list = iCompanyAccountService.selectAllList(companyAccount);
        return getDataTable(list);
    }

    /**
     * 导出公司账户列表
     */
    @PreAuthorize("@ss.hasPermi('payment:companyAccount:export')" )
    @Log(title = "公司账户" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(CompanyAccount companyAccount) {
        List<CompanyAccount> list = iCompanyAccountService.queryList(companyAccount);
        ExcelUtil<CompanyAccount> util = new ExcelUtil<CompanyAccount>(CompanyAccount.class);
        return util.exportExcel(list, "companyAccount" );
    }

    /**
     * 获取公司账户详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:companyAccount:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) String id) {
        return AjaxResult.success(iCompanyAccountService.getById(id));
    }

    /**
     * 新增公司账户
     */
    @PreAuthorize("@ss.hasPermi('payment:companyAccount:add')" )
    @Log(title = "公司账户" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CompanyAccount companyAccount) {
        return toAjax(iCompanyAccountService.save(companyAccount) ? 1 : 0);
    }

    /**
     * 修改公司账户
     */
    @PreAuthorize("@ss.hasPermi('payment:companyAccount:edit')" )
    @Log(title = "公司账户" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CompanyAccount companyAccount) {
        return toAjax(iCompanyAccountService.updateById(companyAccount) ? 1 : 0);
    }

    /**
     * 删除公司账户
     */
    @PreAuthorize("@ss.hasPermi('payment:companyAccount:remove')" )
    @Log(title = "公司账户" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(iCompanyAccountService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
