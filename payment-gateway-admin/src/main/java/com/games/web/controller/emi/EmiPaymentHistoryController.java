package com.games.web.controller.emi;

import java.util.List;
import java.util.Arrays;

import com.games.common.utils.ServletUtils;
import com.games.payment.domain.EmiPaymentHistory;
import com.games.payment.service.IEmiPaymentHistoryService;
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
 * 付款记录Controller
 * 
 * @author Ticker
 * @date 2025-10-16
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/history" )
public class EmiPaymentHistoryController extends BaseController {

    private final IEmiPaymentHistoryService iEmiPaymentHistoryService;
    private final TokenService tokenService;

    /**
     * 查询付款记录列表
     */
    @PreAuthorize("@ss.hasPermi('payment:history:list')")
    @GetMapping("/list")
    public TableDataInfo list(EmiPaymentHistory emiPaymentHistory) {
        startPage();
        
        // 获取当前登录用户信息
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long currentUserId = loginUser.getUserId();
        
        // 如果不是admin用户（ID=1），则只查询当前用户的数据
        if (currentUserId != 138) {
            emiPaymentHistory.setUserId(currentUserId);
        }
        
        List<EmiPaymentHistory> list = iEmiPaymentHistoryService.selectAllList(emiPaymentHistory);
        return getDataTable(list);
    }

    /**
     * 导出付款记录列表
     */
    @PreAuthorize("@ss.hasPermi('payment:history:export')" )
    @Log(title = "付款记录" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(EmiPaymentHistory emiPaymentHistory) {
        List<EmiPaymentHistory> list = iEmiPaymentHistoryService.queryList(emiPaymentHistory);
        ExcelUtil<EmiPaymentHistory> util = new ExcelUtil<EmiPaymentHistory>(EmiPaymentHistory.class);
        return util.exportExcel(list, "history" );
    }

    /**
     * 获取付款记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:history:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iEmiPaymentHistoryService.getById(id));
    }

    /**
     * 新增付款记录
     */
    @PreAuthorize("@ss.hasPermi('payment:history:add')" )
    @Log(title = "付款记录" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EmiPaymentHistory emiPaymentHistory) {
        return toAjax(iEmiPaymentHistoryService.save(emiPaymentHistory) ? 1 : 0);
    }

    /**
     * 修改付款记录
     */
    @PreAuthorize("@ss.hasPermi('payment:history:edit')" )
    @Log(title = "付款记录" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EmiPaymentHistory emiPaymentHistory) {
        return toAjax(iEmiPaymentHistoryService.updateById(emiPaymentHistory) ? 1 : 0);
    }

    /**
     * 删除付款记录
     */
    @PreAuthorize("@ss.hasPermi('payment:history:remove')" )
    @Log(title = "付款记录" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iEmiPaymentHistoryService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
