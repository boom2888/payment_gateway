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
import com.games.payment.domain.Vip;
import com.games.payment.service.IVipService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * VIPController
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/vip" )
public class VipController extends BaseController {

    private final IVipService iVipService;

    /**
     * 查询VIP列表
     */
    @PreAuthorize("@ss.hasPermi('payment:vip:list')")
    @GetMapping("/list")
    public TableDataInfo list(Vip vip) {
        startPage();
        List<Vip> list = iVipService.selectAllList(vip);
        return getDataTable(list);
    }

    /**
     * 导出VIP列表
     */
    @PreAuthorize("@ss.hasPermi('payment:vip:export')" )
    @Log(title = "VIP" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(Vip vip) {
        List<Vip> list = iVipService.queryList(vip);
        ExcelUtil<Vip> util = new ExcelUtil<Vip>(Vip.class);
        return util.exportExcel(list, "vip" );
    }

    /**
     * 获取VIP详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:vip:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iVipService.getById(id));
    }

    /**
     * 新增VIP
     */
    @PreAuthorize("@ss.hasPermi('payment:vip:add')" )
    @Log(title = "VIP" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Vip vip) {
        return toAjax(iVipService.save(vip) ? 1 : 0);
    }

    /**
     * 修改VIP
     */
    @PreAuthorize("@ss.hasPermi('payment:vip:edit')" )
    @Log(title = "VIP" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Vip vip) {
        return toAjax(iVipService.updateById(vip) ? 1 : 0);
    }

    /**
     * 删除VIP
     */
    @PreAuthorize("@ss.hasPermi('payment:vip:remove')" )
    @Log(title = "VIP" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iVipService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
