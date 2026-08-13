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
import com.games.payment.domain.Customer;
import com.games.payment.service.ICustomerService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 客户Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/customer" )
public class CustomerController extends BaseController {

    private final ICustomerService iCustomerService;

    /**
     * 查询客户列表
     */
    @PreAuthorize("@ss.hasPermi('payment:customer:list')")
    @GetMapping("/list")
    public TableDataInfo list(Customer customer) {
        startPage();
        List<Customer> list = iCustomerService.selectAllList(customer);
        return getDataTable(list);
    }

    /**
     * 导出客户列表
     */
    @PreAuthorize("@ss.hasPermi('payment:customer:export')" )
    @Log(title = "客户" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(Customer customer) {
        List<Customer> list = iCustomerService.queryList(customer);
        ExcelUtil<Customer> util = new ExcelUtil<Customer>(Customer.class);
        return util.exportExcel(list, "customer" );
    }

    /**
     * 获取客户详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:customer:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iCustomerService.getById(id));
    }

    /**
     * 新增客户
     */
    @PreAuthorize("@ss.hasPermi('payment:customer:add')" )
    @Log(title = "客户" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Customer customer) {
        return toAjax(iCustomerService.save(customer) ? 1 : 0);
    }

    /**
     * 修改客户
     */
    @PreAuthorize("@ss.hasPermi('payment:customer:edit')" )
    @Log(title = "客户" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Customer customer) {
        return toAjax(iCustomerService.updateById(customer) ? 1 : 0);
    }

    /**
     * 删除客户
     */
    @PreAuthorize("@ss.hasPermi('payment:customer:remove')" )
    @Log(title = "客户" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iCustomerService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
