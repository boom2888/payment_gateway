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
import com.games.payment.domain.SubscriptionLevel;
import com.games.payment.service.ISubscriptionLevelService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 订阅级别Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/subscriptionLevel" )
public class SubscriptionLevelController extends BaseController {

    private final ISubscriptionLevelService iSubscriptionLevelService;

    /**
     * 查询订阅级别列表
     */
    @PreAuthorize("@ss.hasPermi('payment:subscriptionLevel:list')")
    @GetMapping("/list")
    public TableDataInfo list(SubscriptionLevel subscriptionLevel) {
        startPage();
        List<SubscriptionLevel> list = iSubscriptionLevelService.selectAllList(subscriptionLevel);
        return getDataTable(list);
    }

    /**
     * 导出订阅级别列表
     */
    @PreAuthorize("@ss.hasPermi('payment:subscriptionLevel:export')" )
    @Log(title = "订阅级别" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(SubscriptionLevel subscriptionLevel) {
        List<SubscriptionLevel> list = iSubscriptionLevelService.queryList(subscriptionLevel);
        ExcelUtil<SubscriptionLevel> util = new ExcelUtil<SubscriptionLevel>(SubscriptionLevel.class);
        return util.exportExcel(list, "subscriptionLevel" );
    }

    /**
     * 获取订阅级别详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:subscriptionLevel:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iSubscriptionLevelService.getById(id));
    }

    /**
     * 新增订阅级别
     */
    @PreAuthorize("@ss.hasPermi('payment:subscriptionLevel:add')" )
    @Log(title = "订阅级别" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SubscriptionLevel subscriptionLevel) {
        return toAjax(iSubscriptionLevelService.save(subscriptionLevel) ? 1 : 0);
    }

    /**
     * 修改订阅级别
     */
    @PreAuthorize("@ss.hasPermi('payment:subscriptionLevel:edit')" )
    @Log(title = "订阅级别" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SubscriptionLevel subscriptionLevel) {
        return toAjax(iSubscriptionLevelService.updateById(subscriptionLevel) ? 1 : 0);
    }

    /**
     * 删除订阅级别
     */
    @PreAuthorize("@ss.hasPermi('payment:subscriptionLevel:remove')" )
    @Log(title = "订阅级别" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iSubscriptionLevelService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
