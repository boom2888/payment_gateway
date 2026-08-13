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
import com.games.payment.domain.SignupCode;
import com.games.payment.service.ISignupCodeService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 注册码Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/signupCode" )
public class SignupCodeController extends BaseController {

    private final ISignupCodeService iSignupCodeService;

    /**
     * 查询注册码列表
     */
    @PreAuthorize("@ss.hasPermi('payment:signupCode:list')")
    @GetMapping("/list")
    public TableDataInfo list(SignupCode signupCode) {
        startPage();
        List<SignupCode> list = iSignupCodeService.selectAllList(signupCode);
        return getDataTable(list);
    }

    /**
     * 导出注册码列表
     */
    @PreAuthorize("@ss.hasPermi('payment:signupCode:export')" )
    @Log(title = "注册码" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(SignupCode signupCode) {
        List<SignupCode> list = iSignupCodeService.queryList(signupCode);
        ExcelUtil<SignupCode> util = new ExcelUtil<SignupCode>(SignupCode.class);
        return util.exportExcel(list, "signupCode" );
    }

    /**
     * 获取注册码详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:signupCode:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iSignupCodeService.getById(id));
    }

    /**
     * 新增注册码
     */
    @PreAuthorize("@ss.hasPermi('payment:signupCode:add')" )
    @Log(title = "注册码" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SignupCode signupCode) {
        return toAjax(iSignupCodeService.save(signupCode) ? 1 : 0);
    }

    /**
     * 修改注册码
     */
    @PreAuthorize("@ss.hasPermi('payment:signupCode:edit')" )
    @Log(title = "注册码" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SignupCode signupCode) {
        return toAjax(iSignupCodeService.updateById(signupCode) ? 1 : 0);
    }

    /**
     * 删除注册码
     */
    @PreAuthorize("@ss.hasPermi('payment:signupCode:remove')" )
    @Log(title = "注册码" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iSignupCodeService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
