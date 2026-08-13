package com.games.web.controller.payment;

import com.games.common.annotation.Log;
import com.games.common.core.domain.AjaxResult;
import com.games.common.core.page.TableDataInfo;
import com.games.common.enums.BusinessType;
import com.games.payment.domain.*;
import com.games.payment.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 订单Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
@RestController
@RequestMapping("/payment/order")
public class OrderController extends BasePayController {

    private final IOrderService iOrderService;
    private final IShopService iBusinessCustomerCorporationService;
    private final BackService backService;

    /**
     * 查询订单列表
     */
    @PreAuthorize("@ss.hasPermi('payment:order:list')")
    @GetMapping("/list")
    public TableDataInfo list(Order order) {
        filterShopData(order);
        startPage();
        List<Order> list = iOrderService.selectAllList(order);
        return getDataTable(list);
    }

    /**
     * 导出订单列表
     */
    @PreAuthorize("@ss.hasPermi('payment:order:export')")
    @Log(title = "订单", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(Order order,
            @RequestParam(required = false) String lang,
            @RequestParam(required = false) String exportFields) {
        filterShopData(order);
        return iOrderService.exportOrderToExcel(order, lang, exportFields);
    }

    /**
     * 获取订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:order:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        // 如果是商户角色，需要传入商户ID进行权限校验
        Long merchantId = isShopRole() ? getShopId() : null;
        Order order = iOrderService.getOrderDetailWithRelatedData(id, merchantId);

        if (order == null) {
            return AjaxResult.error("订单不存在或无权访问");
        }

        return AjaxResult.success(order);
    }

    /**
     * 新增订单
     */
    @PreAuthorize("@ss.hasPermi('payment:order:add')")
    @Log(title = "订单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Order order) {
        return toAjax(iOrderService.save(order) ? 1 : 0);
    }

    /**
     * 修改订单
     */
    @PreAuthorize("@ss.hasPermi('payment:order:edit')")
    @Log(title = "订单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Order order) {
        return toAjax(iOrderService.updateById(order) ? 1 : 0);
    }

    /**
     * 删除订单
     */
    @PreAuthorize("@ss.hasPermi('payment:order:remove')")
    @Log(title = "订单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iOrderService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }

    /**
     * 交易记录回调
     */
    @Log(title = "交易记录回调", businessType = BusinessType.UPDATE)
    @PostMapping("/tradeCallBack")
    public AjaxResult tradeCallBack(@RequestBody Order order) {
        Order dbOrder = iOrderService.getById(order.getId());
        mustShopData(dbOrder);
        Shop shop = iBusinessCustomerCorporationService.getById(dbOrder.getMerchantId());
        backService.orderCallBack(shop, dbOrder, shop.getNotifyOrderStatusEndpoint());
        return AjaxResult.success();
    }

}
