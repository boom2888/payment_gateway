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
import com.games.payment.domain.PayoutInfo;
import com.games.payment.service.IPayoutInfoService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 支付信息Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/payoutInfo" )
public class PayoutInfoController extends BaseController {

    private final IPayoutInfoService iPayoutInfoService;

    /**
     * 查询支付信息列表
     */
    @PreAuthorize("@ss.hasPermi('payment:payoutInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(PayoutInfo payoutInfo) {
        startPage();
        List<PayoutInfo> list = iPayoutInfoService.selectAllList(payoutInfo);
        return getDataTable(list);
    }

    /**
     * 导出支付信息列表
     */
    @PreAuthorize("@ss.hasPermi('payment:payoutInfo:export')" )
    @Log(title = "支付信息" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(PayoutInfo payoutInfo) {
        List<PayoutInfo> list = iPayoutInfoService.queryList(payoutInfo);
        ExcelUtil<PayoutInfo> util = new ExcelUtil<PayoutInfo>(PayoutInfo.class);
        return util.exportExcel(list, "payoutInfo" );
    }

    /**
     * 获取支付信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:payoutInfo:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iPayoutInfoService.getById(id));
    }

    /**
     * 新增支付信息
     */
    @PreAuthorize("@ss.hasPermi('payment:payoutInfo:add')" )
    @Log(title = "支付信息" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PayoutInfo payoutInfo) {
        return toAjax(iPayoutInfoService.save(payoutInfo) ? 1 : 0);
    }

    /**
     * 修改支付信息
     */
    @PreAuthorize("@ss.hasPermi('payment:payoutInfo:edit')" )
    @Log(title = "支付信息" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PayoutInfo payoutInfo) {
        return toAjax(iPayoutInfoService.updateById(payoutInfo) ? 1 : 0);
    }

    /**
     * 删除支付信息
     */
    @PreAuthorize("@ss.hasPermi('payment:payoutInfo:remove')" )
    @Log(title = "支付信息" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iPayoutInfoService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
