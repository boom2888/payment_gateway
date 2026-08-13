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
import com.games.payment.domain.PaymentSetting;
import com.games.payment.service.IPaymentSettingService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 支付设置Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/paymentSetting" )
public class PaymentSettingController extends BaseController {

    private final IPaymentSettingService iPaymentSettingService;

    /**
     * 查询支付设置列表
     */
    @PreAuthorize("@ss.hasPermi('payment:paymentSetting:list')")
    @GetMapping("/list")
    public TableDataInfo list(PaymentSetting paymentSetting) {
        startPage();
        List<PaymentSetting> list = iPaymentSettingService.selectAllList(paymentSetting);
        return getDataTable(list);
    }

    /**
     * 导出支付设置列表
     */
    @PreAuthorize("@ss.hasPermi('payment:paymentSetting:export')" )
    @Log(title = "支付设置" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(PaymentSetting paymentSetting) {
        List<PaymentSetting> list = iPaymentSettingService.queryList(paymentSetting);
        ExcelUtil<PaymentSetting> util = new ExcelUtil<PaymentSetting>(PaymentSetting.class);
        return util.exportExcel(list, "paymentSetting" );
    }

    /**
     * 获取支付设置详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:paymentSetting:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) String id) {
        return AjaxResult.success(iPaymentSettingService.getById(id));
    }

    /**
     * 新增支付设置
     */
    @PreAuthorize("@ss.hasPermi('payment:paymentSetting:add')" )
    @Log(title = "支付设置" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PaymentSetting paymentSetting) {
        return toAjax(iPaymentSettingService.save(paymentSetting) ? 1 : 0);
    }

    /**
     * 修改支付设置
     */
    @PreAuthorize("@ss.hasPermi('payment:paymentSetting:edit')" )
    @Log(title = "支付设置" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PaymentSetting paymentSetting) {
        return toAjax(iPaymentSettingService.updateById(paymentSetting) ? 1 : 0);
    }

    /**
     * 删除支付设置
     */
    @PreAuthorize("@ss.hasPermi('payment:paymentSetting:remove')" )
    @Log(title = "支付设置" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(iPaymentSettingService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
