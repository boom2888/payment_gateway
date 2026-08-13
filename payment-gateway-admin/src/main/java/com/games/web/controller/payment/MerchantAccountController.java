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
import com.games.payment.domain.MerchantAccount;
import com.games.payment.service.IMerchantAccountService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 商户账户Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/merchantAccount" )
public class MerchantAccountController extends BaseController {

    private final IMerchantAccountService iMerchantAccountService;

    /**
     * 查询商户账户列表
     */
    @PreAuthorize("@ss.hasPermi('payment:merchantAccount:list')")
    @GetMapping("/list")
    public TableDataInfo list(MerchantAccount merchantAccount) {
        startPage();
        List<MerchantAccount> list = iMerchantAccountService.selectAllList(merchantAccount);
        return getDataTable(list);
    }

    /**
     * 导出商户账户列表
     */
    @PreAuthorize("@ss.hasPermi('payment:merchantAccount:export')" )
    @Log(title = "商户账户" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(MerchantAccount merchantAccount) {
        List<MerchantAccount> list = iMerchantAccountService.queryList(merchantAccount);
        ExcelUtil<MerchantAccount> util = new ExcelUtil<MerchantAccount>(MerchantAccount.class);
        return util.exportExcel(list, "merchantAccount" );
    }

    /**
     * 获取商户账户详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:merchantAccount:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) String id) {
        return AjaxResult.success(iMerchantAccountService.getById(id));
    }

    /**
     * 新增商户账户
     */
    @PreAuthorize("@ss.hasPermi('payment:merchantAccount:add')" )
    @Log(title = "商户账户" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MerchantAccount merchantAccount) {
        return toAjax(iMerchantAccountService.save(merchantAccount) ? 1 : 0);
    }

    /**
     * 修改商户账户
     */
    @PreAuthorize("@ss.hasPermi('payment:merchantAccount:edit')" )
    @Log(title = "商户账户" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MerchantAccount merchantAccount) {
        return toAjax(iMerchantAccountService.updateById(merchantAccount) ? 1 : 0);
    }

    /**
     * 删除商户账户
     */
    @PreAuthorize("@ss.hasPermi('payment:merchantAccount:remove')" )
    @Log(title = "商户账户" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(iMerchantAccountService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
