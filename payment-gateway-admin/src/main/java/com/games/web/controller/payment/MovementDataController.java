package com.games.web.controller.payment;

import java.util.List;
import java.util.Arrays;

import org.springframework.security.access.prepost.PreAuthorize;
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
import com.games.payment.domain.MovementData;
import com.games.payment.service.IMovementDataService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

import jakarta.annotation.Resource;

/**
 * nuvei导入数据明细Controller
 * 
 * @author Ticker
 * @date 2025-07-21
 */
@RestController
@RequestMapping("/payment/movementData" )
public class MovementDataController extends BaseController {

    @Resource
    private IMovementDataService iMovementDataService;

    /**
     * 查询nuvei导入数据明细列表
     */
    @PreAuthorize("@ss.hasPermi('payment:movementData:list')")
    @GetMapping("/list")
    public TableDataInfo list(MovementData movementData) {
        startPage();
        List<MovementData> list = iMovementDataService.selectAllList(movementData);
        return getDataTable(list);
    }

    /**
     * 导出nuvei导入数据明细列表
     */
    @PreAuthorize("@ss.hasPermi('payment:movementData:export')" )
    @Log(title = "nuvei导入数据明细" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(MovementData movementData) {
        List<MovementData> list = iMovementDataService.queryList(movementData);
        ExcelUtil<MovementData> util = new ExcelUtil<MovementData>(MovementData.class);
        return util.exportExcel(list, "movementData" );
    }

    /**
     * 获取nuvei导入数据明细详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:movementData:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iMovementDataService.getById(id));
    }

    /**
     * 新增nuvei导入数据明细
     */
    @PreAuthorize("@ss.hasPermi('payment:movementData:add')" )
    @Log(title = "nuvei导入数据明细" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MovementData movementData) {
        return toAjax(iMovementDataService.save(movementData) ? 1 : 0);
    }

    /**
     * 修改nuvei导入数据明细
     */
    @PreAuthorize("@ss.hasPermi('payment:movementData:edit')" )
    @Log(title = "nuvei导入数据明细" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MovementData movementData) {
        return toAjax(iMovementDataService.updateById(movementData) ? 1 : 0);
    }

    /**
     * 删除nuvei导入数据明细
     */
    @PreAuthorize("@ss.hasPermi('payment:movementData:remove')" )
    @Log(title = "nuvei导入数据明细" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iMovementDataService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
