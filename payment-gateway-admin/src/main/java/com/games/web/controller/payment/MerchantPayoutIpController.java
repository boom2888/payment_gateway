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
import com.games.payment.domain.MerchantPayoutIp;
import com.games.payment.service.IMerchantPayoutIpService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 商户支付IPController
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/merchantPayoutIp" )
public class MerchantPayoutIpController extends BaseController {

    private final IMerchantPayoutIpService iMerchantPayoutIpService;

    /**
     * 查询商户支付IP列表
     */
    @PreAuthorize("@ss.hasPermi('payment:merchantPayoutIp:list')")
    @GetMapping("/list")
    public TableDataInfo list(MerchantPayoutIp merchantPayoutIp) {
        startPage();
        List<MerchantPayoutIp> list = iMerchantPayoutIpService.selectAllList(merchantPayoutIp);
        return getDataTable(list);
    }

    /**
     * 导出商户支付IP列表
     */
    @PreAuthorize("@ss.hasPermi('payment:merchantPayoutIp:export')" )
    @Log(title = "商户支付IP" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(MerchantPayoutIp merchantPayoutIp) {
        List<MerchantPayoutIp> list = iMerchantPayoutIpService.queryList(merchantPayoutIp);
        ExcelUtil<MerchantPayoutIp> util = new ExcelUtil<MerchantPayoutIp>(MerchantPayoutIp.class);
        return util.exportExcel(list, "merchantPayoutIp" );
    }

    /**
     * 获取商户支付IP详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:merchantPayoutIp:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) String id) {
        return AjaxResult.success(iMerchantPayoutIpService.getById(id));
    }

    /**
     * 新增商户支付IP
     */
    @PreAuthorize("@ss.hasPermi('payment:merchantPayoutIp:add')" )
    @Log(title = "商户支付IP" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MerchantPayoutIp merchantPayoutIp) {
        return toAjax(iMerchantPayoutIpService.save(merchantPayoutIp) ? 1 : 0);
    }

    /**
     * 修改商户支付IP
     */
    @PreAuthorize("@ss.hasPermi('payment:merchantPayoutIp:edit')" )
    @Log(title = "商户支付IP" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MerchantPayoutIp merchantPayoutIp) {
        return toAjax(iMerchantPayoutIpService.updateById(merchantPayoutIp) ? 1 : 0);
    }

    /**
     * 删除商户支付IP
     */
    @PreAuthorize("@ss.hasPermi('payment:merchantPayoutIp:remove')" )
    @Log(title = "商户支付IP" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(iMerchantPayoutIpService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
