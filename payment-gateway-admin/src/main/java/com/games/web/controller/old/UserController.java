package com.games.web.controller.old;

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
import com.games.payment.domain.User;
import com.games.payment.service.IUserService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 用户Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/user" )
public class UserController extends BaseController {

    private final IUserService iUserService;

    /**
     * 查询用户列表
     */
    @PreAuthorize("@ss.hasPermi('payment:user:list')")
    @GetMapping("/list")
    public TableDataInfo list(User user) {
        startPage();
        List<User> list = iUserService.selectAllList(user);
        return getDataTable(list);
    }

    /**
     * 导出用户列表
     */
    @PreAuthorize("@ss.hasPermi('payment:user:export')" )
    @Log(title = "用户" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(User user) {
        List<User> list = iUserService.queryList(user);
        ExcelUtil<User> util = new ExcelUtil<User>(User.class);
        return util.exportExcel(list, "user" );
    }

    /**
     * 获取用户详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:user:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iUserService.getById(id));
    }

    /**
     * 新增用户
     */
    @PreAuthorize("@ss.hasPermi('payment:user:add')" )
    @Log(title = "用户" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody User user) {
        return toAjax(iUserService.save(user) ? 1 : 0);
    }

    /**
     * 修改用户
     */
    @PreAuthorize("@ss.hasPermi('payment:user:edit')" )
    @Log(title = "用户" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody User user) {
        return toAjax(iUserService.updateById(user) ? 1 : 0);
    }

    /**
     * 删除用户
     */
    @PreAuthorize("@ss.hasPermi('payment:user:remove')" )
    @Log(title = "用户" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iUserService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
