package com.games.web.controller.payment;

import com.games.common.annotation.Log;
import com.games.common.core.controller.BaseController;
import com.games.common.core.domain.AjaxResult;
import com.games.common.core.page.TableDataInfo;
import com.games.common.enums.BusinessType;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.utils.MessageUtils;
import com.games.common.utils.TimezoneUtils;
import com.games.payment.domain.ShopLimit;
import com.games.payment.service.IShopLimitService;
import com.games.payment.service.IOrderService;
import com.games.common.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * 商户限额Controller
 * 
 * @author Ticker
 * @date 2025-09-24
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/limit" )
public class ShopLimitController extends BaseController {

    private final IShopLimitService iShopLimitService;
    private final IOrderService orderService;

    /**
     * 查询商户限额列表
     */
    @PreAuthorize("@ss.hasPermi('payment:limit:list')")
    @GetMapping("/list")
    public TableDataInfo list(ShopLimit shopLimit) {
        startPage();
        List<ShopLimit> list = iShopLimitService.selectAllList(shopLimit);
        return getDataTable(list);
    }

    /**
     * 导出商户限额列表
     */
    @PreAuthorize("@ss.hasPermi('payment:limit:export')" )
    @Log(title = "商户限额" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(ShopLimit shopLimit) {
        List<ShopLimit> list = iShopLimitService.queryList(shopLimit);
        ExcelUtil<ShopLimit> util = new ExcelUtil<ShopLimit>(ShopLimit.class);
        return util.exportExcel(list, "limit" );
    }

    /**
     * 获取商户限额详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:limit:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iShopLimitService.getById(id));
    }

    /**
     * 新增商户限额
     */
    @PreAuthorize("@ss.hasPermi('payment:limit:add')" )
    @Log(title = "商户限额" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ShopLimit shopLimit) {
        if (shopLimit.getShopId() == null) {
            return AjaxResult.error(MessageUtils.message("shop.limit.shopId.required"));
        }
        if (iShopLimitService.existsByShopId(shopLimit.getShopId())) {
            return AjaxResult.error(MessageUtils.message("shop.limit.exists"));
        }
        // 查询该商户当天的订单总额（已完成），没有订单则置 0
        // 从header中获取客户端时区
        String timezone = TimezoneUtils.getClientTimezone();
        String today = DateUtils.getDate();
        BigDecimal todayAmount = orderService.getCompletedFiatAmountBetween(
                today + " 00:00:00",
                today + " 23:59:59",
                shopLimit.getShopId(),
                timezone
        );
        shopLimit.setValue(todayAmount == null ? BigDecimal.ZERO : todayAmount);
        return toAjax(iShopLimitService.save(shopLimit) ? 1 : 0);
    }

    /**
     * 修改商户限额
     */
    @PreAuthorize("@ss.hasPermi('payment:limit:edit')" )
    @Log(title = "商户限额" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ShopLimit shopLimit) {
        if (shopLimit.getShopId() == null) {
            return AjaxResult.error(MessageUtils.message("shop.limit.shopId.required"));
        }
        if (iShopLimitService.existsByShopIdExceptId(shopLimit.getShopId(), shopLimit.getId())) {
            return AjaxResult.error(MessageUtils.message("shop.limit.exists"));
        }
        return toAjax(iShopLimitService.updateById(shopLimit) ? 1 : 0);
    }

    /**
     * 删除商户限额
     */
    @PreAuthorize("@ss.hasPermi('payment:limit:remove')" )
    @Log(title = "商户限额" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iShopLimitService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
