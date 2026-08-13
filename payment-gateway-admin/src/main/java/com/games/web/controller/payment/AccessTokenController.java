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
import com.games.payment.domain.AccessToken;
import com.games.payment.service.IAccessTokenService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 访问令牌Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/token" )
public class AccessTokenController extends BaseController {

    private final IAccessTokenService iAccessTokenService;

    /**
     * 查询访问令牌列表
     */
    @PreAuthorize("@ss.hasPermi('payment:token:list')")
    @GetMapping("/list")
    public TableDataInfo list(AccessToken accessToken) {
        startPage();
        List<AccessToken> list = iAccessTokenService.selectAllList(accessToken);
        return getDataTable(list);
    }

    /**
     * 导出访问令牌列表
     */
    @PreAuthorize("@ss.hasPermi('payment:token:export')" )
    @Log(title = "访问令牌" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(AccessToken accessToken) {
        List<AccessToken> list = iAccessTokenService.queryList(accessToken);
        ExcelUtil<AccessToken> util = new ExcelUtil<AccessToken>(AccessToken.class);
        return util.exportExcel(list, "token" );
    }

    /**
     * 获取访问令牌详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:token:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) String id) {
        return AjaxResult.success(iAccessTokenService.getById(id));
    }

    /**
     * 新增访问令牌
     */
    @PreAuthorize("@ss.hasPermi('payment:token:add')" )
    @Log(title = "访问令牌" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AccessToken accessToken) {
        return toAjax(iAccessTokenService.save(accessToken) ? 1 : 0);
    }

    /**
     * 修改访问令牌
     */
    @PreAuthorize("@ss.hasPermi('payment:token:edit')" )
    @Log(title = "访问令牌" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AccessToken accessToken) {
        return toAjax(iAccessTokenService.updateById(accessToken) ? 1 : 0);
    }

    /**
     * 删除访问令牌
     */
    @PreAuthorize("@ss.hasPermi('payment:token:remove')" )
    @Log(title = "访问令牌" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(iAccessTokenService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
