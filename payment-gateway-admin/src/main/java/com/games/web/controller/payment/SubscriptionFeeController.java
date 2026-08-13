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
import com.games.payment.domain.SubscriptionFee;
import com.games.payment.service.ISubscriptionFeeService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 订阅费用Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/subscriptionFee" )
public class SubscriptionFeeController extends BaseController {

    private final ISubscriptionFeeService iSubscriptionFeeService;

    /**
     * 查询订阅费用列表
     */
    @PreAuthorize("@ss.hasPermi('payment:subscriptionFee:list')")
    @GetMapping("/list")
    public TableDataInfo list(SubscriptionFee subscriptionFee) {
        startPage();
        List<SubscriptionFee> list = iSubscriptionFeeService.selectAllList(subscriptionFee);
        return getDataTable(list);
    }

    /**
     * 导出订阅费用列表
     */
    @PreAuthorize("@ss.hasPermi('payment:subscriptionFee:export')" )
    @Log(title = "订阅费用" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(SubscriptionFee subscriptionFee) {
        List<SubscriptionFee> list = iSubscriptionFeeService.queryList(subscriptionFee);
        ExcelUtil<SubscriptionFee> util = new ExcelUtil<SubscriptionFee>(SubscriptionFee.class);
        return util.exportExcel(list, "subscriptionFee" );
    }

    /**
     * 获取订阅费用详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:subscriptionFee:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iSubscriptionFeeService.getById(id));
    }

    /**
     * 新增订阅费用
     */
    @PreAuthorize("@ss.hasPermi('payment:subscriptionFee:add')" )
    @Log(title = "订阅费用" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SubscriptionFee subscriptionFee) {
        return toAjax(iSubscriptionFeeService.save(subscriptionFee) ? 1 : 0);
    }

    /**
     * 修改订阅费用
     */
    @PreAuthorize("@ss.hasPermi('payment:subscriptionFee:edit')" )
    @Log(title = "订阅费用" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SubscriptionFee subscriptionFee) {
        return toAjax(iSubscriptionFeeService.updateById(subscriptionFee) ? 1 : 0);
    }

    /**
     * 删除订阅费用
     */
    @PreAuthorize("@ss.hasPermi('payment:subscriptionFee:remove')" )
    @Log(title = "订阅费用" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iSubscriptionFeeService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
