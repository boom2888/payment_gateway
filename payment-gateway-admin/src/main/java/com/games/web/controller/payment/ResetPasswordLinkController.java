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
import com.games.payment.domain.ResetPasswordLink;
import com.games.payment.service.IResetPasswordLinkService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 重置密码链接Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/resetPasswordLink" )
public class ResetPasswordLinkController extends BaseController {

    private final IResetPasswordLinkService iResetPasswordLinkService;

    /**
     * 查询重置密码链接列表
     */
    @PreAuthorize("@ss.hasPermi('payment:resetPasswordLink:list')")
    @GetMapping("/list")
    public TableDataInfo list(ResetPasswordLink resetPasswordLink) {
        startPage();
        List<ResetPasswordLink> list = iResetPasswordLinkService.selectAllList(resetPasswordLink);
        return getDataTable(list);
    }

    /**
     * 导出重置密码链接列表
     */
    @PreAuthorize("@ss.hasPermi('payment:resetPasswordLink:export')" )
    @Log(title = "重置密码链接" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(ResetPasswordLink resetPasswordLink) {
        List<ResetPasswordLink> list = iResetPasswordLinkService.queryList(resetPasswordLink);
        ExcelUtil<ResetPasswordLink> util = new ExcelUtil<ResetPasswordLink>(ResetPasswordLink.class);
        return util.exportExcel(list, "resetPasswordLink" );
    }

    /**
     * 获取重置密码链接详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:resetPasswordLink:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iResetPasswordLinkService.getById(id));
    }

    /**
     * 新增重置密码链接
     */
    @PreAuthorize("@ss.hasPermi('payment:resetPasswordLink:add')" )
    @Log(title = "重置密码链接" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ResetPasswordLink resetPasswordLink) {
        return toAjax(iResetPasswordLinkService.save(resetPasswordLink) ? 1 : 0);
    }

    /**
     * 修改重置密码链接
     */
    @PreAuthorize("@ss.hasPermi('payment:resetPasswordLink:edit')" )
    @Log(title = "重置密码链接" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ResetPasswordLink resetPasswordLink) {
        return toAjax(iResetPasswordLinkService.updateById(resetPasswordLink) ? 1 : 0);
    }

    /**
     * 删除重置密码链接
     */
    @PreAuthorize("@ss.hasPermi('payment:resetPasswordLink:remove')" )
    @Log(title = "重置密码链接" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iResetPasswordLinkService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
