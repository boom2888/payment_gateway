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
import com.games.payment.domain.OrderRecon;
import com.games.payment.service.IOrderReconService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 订单对账Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/orderRecon" )
public class OrderReconController extends BaseController {

    private final IOrderReconService iOrderReconService;

    /**
     * 查询订单对账列表
     */
    @PreAuthorize("@ss.hasPermi('payment:orderRecon:list')")
    @GetMapping("/list")
    public TableDataInfo list(OrderRecon orderRecon) {
        startPage();
        List<OrderRecon> list = iOrderReconService.selectAllList(orderRecon);
        return getDataTable(list);
    }

    /**
     * 导出订单对账列表
     */
    @PreAuthorize("@ss.hasPermi('payment:orderRecon:export')" )
    @Log(title = "订单对账" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(OrderRecon orderRecon) {
        List<OrderRecon> list = iOrderReconService.queryList(orderRecon);
        ExcelUtil<OrderRecon> util = new ExcelUtil<OrderRecon>(OrderRecon.class);
        return util.exportExcel(list, "orderRecon" );
    }

    /**
     * 获取订单对账详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:orderRecon:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iOrderReconService.getById(id));
    }

    /**
     * 新增订单对账
     */
    @PreAuthorize("@ss.hasPermi('payment:orderRecon:add')" )
    @Log(title = "订单对账" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OrderRecon orderRecon) {
        return toAjax(iOrderReconService.save(orderRecon) ? 1 : 0);
    }

    /**
     * 修改订单对账
     */
    @PreAuthorize("@ss.hasPermi('payment:orderRecon:edit')" )
    @Log(title = "订单对账" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OrderRecon orderRecon) {
        return toAjax(iOrderReconService.updateById(orderRecon) ? 1 : 0);
    }

    /**
     * 删除订单对账
     */
    @PreAuthorize("@ss.hasPermi('payment:orderRecon:remove')" )
    @Log(title = "订单对账" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iOrderReconService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
