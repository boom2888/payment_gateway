package com.games.web.controller.payment;

import com.games.common.annotation.Log;
import com.games.common.core.domain.AjaxResult;
import com.games.common.core.page.TableDataInfo;
import com.games.common.enums.BusinessType;
import com.games.common.utils.poi.ExcelUtil;
import com.games.payment.domain.RefundOrder;
import com.games.payment.service.IRefundOrderService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 【订单退款】Controller
 * 
 * @author Ticker
 * @date 2025-09-30
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/refundOrder")
public class RefundOrderController extends BasePayController {

    private final IRefundOrderService iRefundOrderService;

    /**
     * 查询【订单退款】列表
     */
    @PreAuthorize("@ss.hasPermi('payment:refundOrder:list')")
    @GetMapping("/list")
    public TableDataInfo list(RefundOrder refundOrder) {
        filterShopData(refundOrder);
        startPage();
        List<RefundOrder> list = iRefundOrderService.selectAllList(refundOrder);
        list.stream()
                .filter(item -> item != null && Integer.valueOf(0).equals(item.getStatus()))
                .forEach(item -> item.setFailReason(""));
        return getDataTable(list);
    }

    /**
     * 导出【订单退款】列表
     */
    @PreAuthorize("@ss.hasPermi('payment:refundOrder:export')")
    @Log(title = "【订单退款】", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(RefundOrder refundOrder,
                            @RequestParam(required = false) String ids) {
        filterShopData(refundOrder);
        if (StringUtils.isNotBlank(ids)) {
            List<Long> exportIds = Arrays.stream(ids.split(","))
                    .map(String::trim)
                    .filter(StringUtils::isNotBlank)
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
            if (!exportIds.isEmpty()) {
                refundOrder.setIds(exportIds);
            }
        }
        List<RefundOrder> list = iRefundOrderService.selectAllList(refundOrder);
        ExcelUtil<RefundOrder> util = new ExcelUtil<>(RefundOrder.class);
        return util.exportExcel(list, "refund_order");
    }

    /**
     * 获取【订单退款】详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:refundOrder:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        RefundOrder refundOrder = iRefundOrderService.getById(id);
        if (refundOrder == null) {
            return AjaxResult.error("数据不存在");
        }
        mustShopData(refundOrder);
        return AjaxResult.success(refundOrder);
    }

    /**
     * 新增【订单退款】
     */
    @PreAuthorize("@ss.hasPermi('payment:refundOrder:add')")
    @Log(title = "【订单退款】", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RefundOrder refundOrder) {
        return toAjax(iRefundOrderService.save(refundOrder) ? 1 : 0);
    }

    /**
     * 修改【订单退款】
     */
    @PreAuthorize("@ss.hasPermi('payment:order:edit')")
    @Log(title = "【订单退款】", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RefundOrder refundOrder) {
        return toAjax(iRefundOrderService.updateById(refundOrder) ? 1 : 0);
    }

    /**
     * 删除【订单退款】
     */
    @PreAuthorize("@ss.hasPermi('payment:order:remove')")
    @Log(title = "【订单退款】", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iRefundOrderService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
