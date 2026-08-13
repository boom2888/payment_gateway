package com.games.web.controller.emi;

import java.util.List;
import java.util.Arrays;

import com.games.common.utils.ServletUtils;
import com.games.payment.domain.EmiPayerTable;
import com.games.payment.service.IEmiPayerTableService;
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
 * 付款方列Controller
 * 
 * @author Ticker
 * @date 2025-10-16
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/table" )
public class EmiPayerTableController extends BaseController {

    private final IEmiPayerTableService iEmiPayerTableService;
    private final TokenService tokenService;

    /**
     * 查询付款方列列表
     */
    @PreAuthorize("@ss.hasPermi('payment:table:list')")
    @GetMapping("/list")
    public TableDataInfo list(EmiPayerTable emiPayerTable) {
        startPage();
        
        // 获取当前登录用户信息
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long currentUserId = loginUser.getUserId();
        
        // 如果不是admin用户（ID=1），则只查询当前用户的数据
        if (currentUserId != 138) {
            emiPayerTable.setUserId(currentUserId);
        }
        
        List<EmiPayerTable> list = iEmiPayerTableService.selectAllList(emiPayerTable);
        return getDataTable(list);
    }

    /**
     * 导出付款方列列表
     */
    @PreAuthorize("@ss.hasPermi('payment:table:export')" )
    @Log(title = "付款方列" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(EmiPayerTable emiPayerTable) {
        List<EmiPayerTable> list = iEmiPayerTableService.queryList(emiPayerTable);
        ExcelUtil<EmiPayerTable> util = new ExcelUtil<EmiPayerTable>(EmiPayerTable.class);
        return util.exportExcel(list, "table" );
    }

    /**
     * 获取付款方列详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:table:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iEmiPayerTableService.getById(id));
    }

    /**
     * 新增付款方列
     */
    @PreAuthorize("@ss.hasPermi('payment:table:add')" )
    @Log(title = "付款方列" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EmiPayerTable emiPayerTable) {
        return toAjax(iEmiPayerTableService.save(emiPayerTable) ? 1 : 0);
    }

    /**
     * 修改付款方列
     */
    @PreAuthorize("@ss.hasPermi('payment:table:edit')" )
    @Log(title = "付款方列" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EmiPayerTable emiPayerTable) {
        return toAjax(iEmiPayerTableService.updateById(emiPayerTable) ? 1 : 0);
    }

    /**
     * 删除付款方列
     */
    @PreAuthorize("@ss.hasPermi('payment:table:remove')" )
    @Log(title = "付款方列" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iEmiPayerTableService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
