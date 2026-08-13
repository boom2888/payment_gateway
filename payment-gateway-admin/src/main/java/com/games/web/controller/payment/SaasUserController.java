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
import com.games.payment.domain.SaasUser;
import com.games.payment.service.ISaasUserService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * SaaS用户Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/saasUser" )
public class SaasUserController extends BaseController {

    private final ISaasUserService iSaasUserService;

    /**
     * 查询SaaS用户列表
     */
    @PreAuthorize("@ss.hasPermi('payment:saasUser:list')")
    @GetMapping("/list")
    public TableDataInfo list(SaasUser saasUser) {
        startPage();
        List<SaasUser> list = iSaasUserService.selectAllList(saasUser);
        return getDataTable(list);
    }

    /**
     * 导出SaaS用户列表
     */
    @PreAuthorize("@ss.hasPermi('payment:saasUser:export')" )
    @Log(title = "SaaS用户" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(SaasUser saasUser) {
        List<SaasUser> list = iSaasUserService.queryList(saasUser);
        ExcelUtil<SaasUser> util = new ExcelUtil<SaasUser>(SaasUser.class);
        return util.exportExcel(list, "saasUser" );
    }

    /**
     * 获取SaaS用户详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:saasUser:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iSaasUserService.getById(id));
    }

    /**
     * 新增SaaS用户
     */
    @PreAuthorize("@ss.hasPermi('payment:saasUser:add')" )
    @Log(title = "SaaS用户" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SaasUser saasUser) {
        return toAjax(iSaasUserService.save(saasUser) ? 1 : 0);
    }

    /**
     * 修改SaaS用户
     */
    @PreAuthorize("@ss.hasPermi('payment:saasUser:edit')" )
    @Log(title = "SaaS用户" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SaasUser saasUser) {
        return toAjax(iSaasUserService.updateById(saasUser) ? 1 : 0);
    }

    /**
     * 删除SaaS用户
     */
    @PreAuthorize("@ss.hasPermi('payment:saasUser:remove')" )
    @Log(title = "SaaS用户" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iSaasUserService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
