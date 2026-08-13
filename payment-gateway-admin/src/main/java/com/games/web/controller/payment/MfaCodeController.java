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
import com.games.payment.domain.MfaCode;
import com.games.payment.service.IMfaCodeService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * MFA代码Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/mfaCode" )
public class MfaCodeController extends BaseController {

    private final IMfaCodeService iMfaCodeService;

    /**
     * 查询MFA代码列表
     */
    @PreAuthorize("@ss.hasPermi('payment:mfaCode:list')")
    @GetMapping("/list")
    public TableDataInfo list(MfaCode mfaCode) {
        startPage();
        List<MfaCode> list = iMfaCodeService.selectAllList(mfaCode);
        return getDataTable(list);
    }

    /**
     * 导出MFA代码列表
     */
    @PreAuthorize("@ss.hasPermi('payment:mfaCode:export')" )
    @Log(title = "MFA代码" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(MfaCode mfaCode) {
        List<MfaCode> list = iMfaCodeService.queryList(mfaCode);
        ExcelUtil<MfaCode> util = new ExcelUtil<MfaCode>(MfaCode.class);
        return util.exportExcel(list, "mfaCode" );
    }

    /**
     * 获取MFA代码详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:mfaCode:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iMfaCodeService.getById(id));
    }

    /**
     * 新增MFA代码
     */
    @PreAuthorize("@ss.hasPermi('payment:mfaCode:add')" )
    @Log(title = "MFA代码" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MfaCode mfaCode) {
        return toAjax(iMfaCodeService.save(mfaCode) ? 1 : 0);
    }

    /**
     * 修改MFA代码
     */
    @PreAuthorize("@ss.hasPermi('payment:mfaCode:edit')" )
    @Log(title = "MFA代码" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MfaCode mfaCode) {
        return toAjax(iMfaCodeService.updateById(mfaCode) ? 1 : 0);
    }

    /**
     * 删除MFA代码
     */
    @PreAuthorize("@ss.hasPermi('payment:mfaCode:remove')" )
    @Log(title = "MFA代码" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iMfaCodeService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
