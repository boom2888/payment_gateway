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
import com.games.payment.domain.SubscriptionModel;
import com.games.payment.service.ISubscriptionModelService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 订阅模型Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/subscriptionModel" )
public class SubscriptionModelController extends BaseController {

    private final ISubscriptionModelService iSubscriptionModelService;

    /**
     * 查询订阅模型列表
     */
    @PreAuthorize("@ss.hasPermi('payment:subscriptionModel:list')")
    @GetMapping("/list")
    public TableDataInfo list(SubscriptionModel subscriptionModel) {
        startPage();
        List<SubscriptionModel> list = iSubscriptionModelService.selectAllList(subscriptionModel);
        return getDataTable(list);
    }

    /**
     * 导出订阅模型列表
     */
    @PreAuthorize("@ss.hasPermi('payment:subscriptionModel:export')" )
    @Log(title = "订阅模型" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(SubscriptionModel subscriptionModel) {
        List<SubscriptionModel> list = iSubscriptionModelService.queryList(subscriptionModel);
        ExcelUtil<SubscriptionModel> util = new ExcelUtil<SubscriptionModel>(SubscriptionModel.class);
        return util.exportExcel(list, "subscriptionModel" );
    }

    /**
     * 获取订阅模型详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:subscriptionModel:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iSubscriptionModelService.getById(id));
    }

    /**
     * 新增订阅模型
     */
    @PreAuthorize("@ss.hasPermi('payment:subscriptionModel:add')" )
    @Log(title = "订阅模型" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SubscriptionModel subscriptionModel) {
        return toAjax(iSubscriptionModelService.save(subscriptionModel) ? 1 : 0);
    }

    /**
     * 修改订阅模型
     */
    @PreAuthorize("@ss.hasPermi('payment:subscriptionModel:edit')" )
    @Log(title = "订阅模型" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SubscriptionModel subscriptionModel) {
        return toAjax(iSubscriptionModelService.updateById(subscriptionModel) ? 1 : 0);
    }

    /**
     * 删除订阅模型
     */
    @PreAuthorize("@ss.hasPermi('payment:subscriptionModel:remove')" )
    @Log(title = "订阅模型" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iSubscriptionModelService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
