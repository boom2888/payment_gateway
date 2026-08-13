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
import com.games.payment.domain.ConfigOrderStatus;
import com.games.payment.service.IConfigOrderStatusService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 订单状态配置Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/configOrderStatus" )
public class ConfigOrderStatusController extends BaseController {

    private final IConfigOrderStatusService iConfigOrderStatusService;

    /**
     * 查询订单状态配置列表
     */
    @PreAuthorize("@ss.hasPermi('payment:configOrderStatus:list')")
    @GetMapping("/list")
    public TableDataInfo list(ConfigOrderStatus configOrderStatus) {
        startPage();
        List<ConfigOrderStatus> list = iConfigOrderStatusService.selectAllList(configOrderStatus);
        return getDataTable(list);
    }

    /**
     * 导出订单状态配置列表
     */
    @PreAuthorize("@ss.hasPermi('payment:configOrderStatus:export')" )
    @Log(title = "订单状态配置" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(ConfigOrderStatus configOrderStatus) {
        List<ConfigOrderStatus> list = iConfigOrderStatusService.queryList(configOrderStatus);
        ExcelUtil<ConfigOrderStatus> util = new ExcelUtil<ConfigOrderStatus>(ConfigOrderStatus.class);
        return util.exportExcel(list, "configOrderStatus" );
    }

    /**
     * 获取订单状态配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:configOrderStatus:query')" )
    @GetMapping(value = "/{code}" )
    public AjaxResult getInfo(@PathVariable("code" ) String code) {
        return AjaxResult.success(iConfigOrderStatusService.getById(code));
    }

    /**
     * 新增订单状态配置
     */
    @PreAuthorize("@ss.hasPermi('payment:configOrderStatus:add')" )
    @Log(title = "订单状态配置" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ConfigOrderStatus configOrderStatus) {
        return toAjax(iConfigOrderStatusService.save(configOrderStatus) ? 1 : 0);
    }

    /**
     * 修改订单状态配置
     */
    @PreAuthorize("@ss.hasPermi('payment:configOrderStatus:edit')" )
    @Log(title = "订单状态配置" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ConfigOrderStatus configOrderStatus) {
        return toAjax(iConfigOrderStatusService.updateById(configOrderStatus) ? 1 : 0);
    }

    /**
     * 删除订单状态配置
     */
    @PreAuthorize("@ss.hasPermi('payment:configOrderStatus:remove')" )
    @Log(title = "订单状态配置" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{codes}" )
    public AjaxResult remove(@PathVariable String[] codes) {
        return toAjax(iConfigOrderStatusService.removeByIds(Arrays.asList(codes)) ? 1 : 0);
    }
}
