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
import com.games.payment.domain.CustomerAddress;
import com.games.payment.service.ICustomerAddressService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 客户地址Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/customerAddress" )
public class CustomerAddressController extends BaseController {

    private final ICustomerAddressService iCustomerAddressService;

    /**
     * 查询客户地址列表
     */
    @PreAuthorize("@ss.hasPermi('payment:customerAddress:list')")
    @GetMapping("/list")
    public TableDataInfo list(CustomerAddress customerAddress) {
        startPage();
        List<CustomerAddress> list = iCustomerAddressService.selectAllList(customerAddress);
        return getDataTable(list);
    }

    /**
     * 导出客户地址列表
     */
    @PreAuthorize("@ss.hasPermi('payment:customerAddress:export')" )
    @Log(title = "客户地址" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(CustomerAddress customerAddress) {
        List<CustomerAddress> list = iCustomerAddressService.queryList(customerAddress);
        ExcelUtil<CustomerAddress> util = new ExcelUtil<CustomerAddress>(CustomerAddress.class);
        return util.exportExcel(list, "customerAddress" );
    }

    /**
     * 获取客户地址详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:customerAddress:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iCustomerAddressService.getById(id));
    }

    /**
     * 新增客户地址
     */
    @PreAuthorize("@ss.hasPermi('payment:customerAddress:add')" )
    @Log(title = "客户地址" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CustomerAddress customerAddress) {
        return toAjax(iCustomerAddressService.save(customerAddress) ? 1 : 0);
    }

    /**
     * 修改客户地址
     */
    @PreAuthorize("@ss.hasPermi('payment:customerAddress:edit')" )
    @Log(title = "客户地址" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CustomerAddress customerAddress) {
        return toAjax(iCustomerAddressService.updateById(customerAddress) ? 1 : 0);
    }

    /**
     * 删除客户地址
     */
    @PreAuthorize("@ss.hasPermi('payment:customerAddress:remove')" )
    @Log(title = "客户地址" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iCustomerAddressService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
