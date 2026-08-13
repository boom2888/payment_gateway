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
import com.games.payment.domain.KycInfo;
import com.games.payment.service.IKycInfoService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * KYC信息Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/kycInfo" )
public class KycInfoController extends BaseController {

    private final IKycInfoService iKycInfoService;

    /**
     * 查询KYC信息列表
     */
    @PreAuthorize("@ss.hasPermi('payment:kycInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(KycInfo kycInfo) {
        startPage();
        List<KycInfo> list = iKycInfoService.selectAllList(kycInfo);
        return getDataTable(list);
    }

    /**
     * 导出KYC信息列表
     */
    @PreAuthorize("@ss.hasPermi('payment:kycInfo:export')" )
    @Log(title = "KYC信息" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(KycInfo kycInfo) {
        List<KycInfo> list = iKycInfoService.queryList(kycInfo);
        ExcelUtil<KycInfo> util = new ExcelUtil<KycInfo>(KycInfo.class);
        return util.exportExcel(list, "kycInfo" );
    }

    /**
     * 获取KYC信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:kycInfo:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iKycInfoService.getById(id));
    }

    /**
     * 新增KYC信息
     */
    @PreAuthorize("@ss.hasPermi('payment:kycInfo:add')" )
    @Log(title = "KYC信息" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody KycInfo kycInfo) {
        return toAjax(iKycInfoService.save(kycInfo) ? 1 : 0);
    }

    /**
     * 修改KYC信息
     */
    @PreAuthorize("@ss.hasPermi('payment:kycInfo:edit')" )
    @Log(title = "KYC信息" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody KycInfo kycInfo) {
        return toAjax(iKycInfoService.updateById(kycInfo) ? 1 : 0);
    }

    /**
     * 删除KYC信息
     */
    @PreAuthorize("@ss.hasPermi('payment:kycInfo:remove')" )
    @Log(title = "KYC信息" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iKycInfoService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
