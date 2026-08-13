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
import com.games.payment.domain.Role;
import com.games.payment.service.IRoleService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 角色Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/role" )
public class RoleController extends BaseController {

    private final IRoleService iRoleService;

    /**
     * 查询角色列表
     */
    @PreAuthorize("@ss.hasPermi('payment:role:list')")
    @GetMapping("/list")
    public TableDataInfo list(Role role) {
        startPage();
        List<Role> list = iRoleService.selectAllList(role);
        return getDataTable(list);
    }

    /**
     * 导出角色列表
     */
    @PreAuthorize("@ss.hasPermi('payment:role:export')" )
    @Log(title = "角色" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(Role role) {
        List<Role> list = iRoleService.queryList(role);
        ExcelUtil<Role> util = new ExcelUtil<Role>(Role.class);
        return util.exportExcel(list, "role" );
    }

    /**
     * 获取角色详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:role:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iRoleService.getById(id));
    }

    /**
     * 新增角色
     */
    @PreAuthorize("@ss.hasPermi('payment:role:add')" )
    @Log(title = "角色" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Role role) {
        return toAjax(iRoleService.save(role) ? 1 : 0);
    }

    /**
     * 修改角色
     */
    @PreAuthorize("@ss.hasPermi('payment:role:edit')" )
    @Log(title = "角色" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Role role) {
        return toAjax(iRoleService.updateById(role) ? 1 : 0);
    }

    /**
     * 删除角色
     */
    @PreAuthorize("@ss.hasPermi('payment:role:remove')" )
    @Log(title = "角色" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iRoleService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
