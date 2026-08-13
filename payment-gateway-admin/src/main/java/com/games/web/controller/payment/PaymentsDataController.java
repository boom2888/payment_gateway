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
import com.games.payment.domain.PaymentsData;
import com.games.payment.service.IPaymentsDataService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 支付数据Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/paymentsData" )
public class PaymentsDataController extends BaseController {

    private final IPaymentsDataService iPaymentsDataService;

    /**
     * 查询支付数据列表
     */
    @PreAuthorize("@ss.hasPermi('payment:paymentsData:list')")
    @GetMapping("/list")
    public TableDataInfo list(PaymentsData paymentsData) {
        startPage();
        List<PaymentsData> list = iPaymentsDataService.selectAllList(paymentsData);
        return getDataTable(list);
    }

    /**
     * 导出支付数据列表
     */
    @PreAuthorize("@ss.hasPermi('payment:paymentsData:export')" )
    @Log(title = "支付数据" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(PaymentsData paymentsData) {
        List<PaymentsData> list = iPaymentsDataService.queryList(paymentsData);
        ExcelUtil<PaymentsData> util = new ExcelUtil<PaymentsData>(PaymentsData.class);
        return util.exportExcel(list, "paymentsData" );
    }

    /**
     * 获取支付数据详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:paymentsData:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iPaymentsDataService.getById(id));
    }

    /**
     * 新增支付数据
     */
    @PreAuthorize("@ss.hasPermi('payment:paymentsData:add')" )
    @Log(title = "支付数据" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PaymentsData paymentsData) {
        return toAjax(iPaymentsDataService.save(paymentsData) ? 1 : 0);
    }

    /**
     * 修改支付数据
     */
    @PreAuthorize("@ss.hasPermi('payment:paymentsData:edit')" )
    @Log(title = "支付数据" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PaymentsData paymentsData) {
        return toAjax(iPaymentsDataService.updateById(paymentsData) ? 1 : 0);
    }

    /**
     * 删除支付数据
     */
    @PreAuthorize("@ss.hasPermi('payment:paymentsData:remove')" )
    @Log(title = "支付数据" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iPaymentsDataService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
