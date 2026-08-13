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
import com.games.payment.domain.OrderUserInfo;
import com.games.payment.service.IOrderUserInfoService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 订单用户信息Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/orderUserInfo" )
public class OrderUserInfoController extends BaseController {

    private final IOrderUserInfoService iOrderUserInfoService;

    /**
     * 查询订单用户信息列表
     */
    @PreAuthorize("@ss.hasPermi('payment:orderUserInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(OrderUserInfo orderUserInfo) {
        startPage();
        List<OrderUserInfo> list = iOrderUserInfoService.selectAllList(orderUserInfo);
        return getDataTable(list);
    }

    /**
     * 导出订单用户信息列表
     */
    @PreAuthorize("@ss.hasPermi('payment:orderUserInfo:export')" )
    @Log(title = "订单用户信息" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(OrderUserInfo orderUserInfo) {
        List<OrderUserInfo> list = iOrderUserInfoService.queryList(orderUserInfo);
        ExcelUtil<OrderUserInfo> util = new ExcelUtil<OrderUserInfo>(OrderUserInfo.class);
        return util.exportExcel(list, "orderUserInfo" );
    }

    /**
     * 获取订单用户信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:orderUserInfo:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iOrderUserInfoService.getById(id));
    }

    /**
     * 新增订单用户信息
     */
    @PreAuthorize("@ss.hasPermi('payment:orderUserInfo:add')" )
    @Log(title = "订单用户信息" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OrderUserInfo orderUserInfo) {
        return toAjax(iOrderUserInfoService.save(orderUserInfo) ? 1 : 0);
    }

    /**
     * 修改订单用户信息
     */
    @PreAuthorize("@ss.hasPermi('payment:orderUserInfo:edit')" )
    @Log(title = "订单用户信息" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OrderUserInfo orderUserInfo) {
        return toAjax(iOrderUserInfoService.updateById(orderUserInfo) ? 1 : 0);
    }

    /**
     * 删除订单用户信息
     */
    @PreAuthorize("@ss.hasPermi('payment:orderUserInfo:remove')" )
    @Log(title = "订单用户信息" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iOrderUserInfoService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
