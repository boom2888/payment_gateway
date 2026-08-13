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
import com.games.payment.domain.AcquirerOrder;
import com.games.payment.service.IAcquirerOrderService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 收单机构订单Controller
 * 
 * @author Ticker
 * @date 2025-07-08
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/acquirerOrder" )
public class AcquirerOrderController extends BaseController {

    private final IAcquirerOrderService iAcquirerOrderService;

    /**
     * 查询收单机构订单列表
     */
    @PreAuthorize("@ss.hasPermi('payment:acquirerOrder:list')")
    @GetMapping("/list")
    public TableDataInfo list(AcquirerOrder acquirerOrder) {
        startPage();
        List<AcquirerOrder> list = iAcquirerOrderService.selectAllList(acquirerOrder);
        return getDataTable(list);
    }

    /**
     * 导出收单机构订单列表
     */
    @PreAuthorize("@ss.hasPermi('payment:acquirerOrder:export')" )
    @Log(title = "收单机构订单" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(AcquirerOrder acquirerOrder) {
        List<AcquirerOrder> list = iAcquirerOrderService.queryList(acquirerOrder);
        ExcelUtil<AcquirerOrder> util = new ExcelUtil<AcquirerOrder>(AcquirerOrder.class);
        return util.exportExcel(list, "acquirerOrder" );
    }

    /**
     * 获取收单机构订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:acquirerOrder:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iAcquirerOrderService.getById(id));
    }

    /**
     * 新增收单机构订单
     */
    @PreAuthorize("@ss.hasPermi('payment:acquirerOrder:add')" )
    @Log(title = "收单机构订单" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AcquirerOrder acquirerOrder) {
        return toAjax(iAcquirerOrderService.save(acquirerOrder) ? 1 : 0);
    }

    /**
     * 修改收单机构订单
     */
    @PreAuthorize("@ss.hasPermi('payment:acquirerOrder:edit')" )
    @Log(title = "收单机构订单" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AcquirerOrder acquirerOrder) {
        return toAjax(iAcquirerOrderService.updateById(acquirerOrder) ? 1 : 0);
    }

    /**
     * 删除收单机构订单
     */
    @PreAuthorize("@ss.hasPermi('payment:acquirerOrder:remove')" )
    @Log(title = "收单机构订单" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iAcquirerOrderService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
