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
import com.games.payment.domain.Address;
import com.games.payment.service.IAddressService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 地址Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/address" )
public class AddressController extends BaseController {

    private final IAddressService iAddressService;

    /**
     * 查询地址列表
     */
    @PreAuthorize("@ss.hasPermi('payment:address:list')")
    @GetMapping("/list")
    public TableDataInfo list(Address address) {
        startPage();
        List<Address> list = iAddressService.selectAllList(address);
        return getDataTable(list);
    }

    /**
     * 导出地址列表
     */
    @PreAuthorize("@ss.hasPermi('payment:address:export')" )
    @Log(title = "地址" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(Address address) {
        List<Address> list = iAddressService.queryList(address);
        ExcelUtil<Address> util = new ExcelUtil<Address>(Address.class);
        return util.exportExcel(list, "address" );
    }

    /**
     * 获取地址详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:address:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iAddressService.getById(id));
    }

    /**
     * 新增地址
     */
    @PreAuthorize("@ss.hasPermi('payment:address:add')" )
    @Log(title = "地址" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Address address) {
        return toAjax(iAddressService.save(address) ? 1 : 0);
    }

    /**
     * 修改地址
     */
    @PreAuthorize("@ss.hasPermi('payment:address:edit')" )
    @Log(title = "地址" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Address address) {
        return toAjax(iAddressService.updateById(address) ? 1 : 0);
    }

    /**
     * 删除地址
     */
    @PreAuthorize("@ss.hasPermi('payment:address:remove')" )
    @Log(title = "地址" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iAddressService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
