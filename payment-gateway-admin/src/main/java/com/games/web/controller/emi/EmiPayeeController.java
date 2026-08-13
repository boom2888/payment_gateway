package com.games.web.controller.emi;

import java.util.List;
import java.util.Arrays;

import com.games.common.utils.ServletUtils;
import com.games.payment.domain.EmiPayee;
import com.games.payment.service.IEmiPayeeService;
import com.games.common.core.domain.model.LoginUser;
import com.games.framework.web.service.TokenService;
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

import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 收款方列Controller
 * 
 * @author Ticker
 * @date 2025-10-16
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/payee" )
public class EmiPayeeController extends BaseController {

    private final IEmiPayeeService iEmiPayeeService;
    private final TokenService tokenService;

    /**
     * 查询收款方列列表
     */
    @PreAuthorize("@ss.hasPermi('payment:payee:list')")
    @GetMapping("/list")
    public TableDataInfo list(EmiPayee emiPayee) {
        startPage();
        
        // 获取当前登录用户信息
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long currentUserId = loginUser.getUserId();
        
        // 如果不是admin用户（ID=1），则只查询当前用户的数据
        if (currentUserId != 138) {
            emiPayee.setUserId(currentUserId);
        }
        
        List<EmiPayee> list = iEmiPayeeService.selectAllList(emiPayee);
        return getDataTable(list);
    }

    /**
     * 导出收款方列列表
     */
    @PreAuthorize("@ss.hasPermi('payment:payee:export')" )
    @Log(title = "收款方列" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(EmiPayee emiPayee) {
        List<EmiPayee> list = iEmiPayeeService.queryList(emiPayee);
        ExcelUtil<EmiPayee> util = new ExcelUtil<EmiPayee>(EmiPayee.class);
        return util.exportExcel(list, "payee" );
    }

    /**
     * 获取收款方列详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:payee:query')" )
    @GetMapping(value = "/{id}" )



    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iEmiPayeeService.getById(id));
    }

    /**
     * 新增收款方列
     */
    @PreAuthorize("@ss.hasPermi('payment:payee:add')" )
    @Log(title = "收款方列" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EmiPayee emiPayee) {
        return toAjax(iEmiPayeeService.save(emiPayee) ? 1 : 0);
    }

    /**
     * 修改收款方列
     */
    @PreAuthorize("@ss.hasPermi('payment:payee:edit')" )
    @Log(title = "收款方列" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EmiPayee emiPayee) {
        return toAjax(iEmiPayeeService.updateById(emiPayee) ? 1 : 0);
    }

    /**
     * 删除收款方列
     */
    @PreAuthorize("@ss.hasPermi('payment:payee:remove')" )
    @Log(title = "收款方列" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iEmiPayeeService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
