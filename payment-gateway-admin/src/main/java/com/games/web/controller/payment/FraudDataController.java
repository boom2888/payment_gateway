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
import com.games.payment.domain.FraudData;
import com.games.payment.service.IFraudDataService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 欺诈数据Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/fraudData" )
public class FraudDataController extends BaseController {

    private final IFraudDataService iFraudDataService;

    /**
     * 查询欺诈数据列表
     */
    @PreAuthorize("@ss.hasPermi('payment:fraudData:list')")
    @GetMapping("/list")
    public TableDataInfo list(FraudData fraudData) {
        startPage();
        List<FraudData> list = iFraudDataService.selectAllList(fraudData);
        return getDataTable(list);
    }

    /**
     * 导出欺诈数据列表
     */
    @PreAuthorize("@ss.hasPermi('payment:fraudData:export')" )
    @Log(title = "欺诈数据" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(FraudData fraudData) {
        List<FraudData> list = iFraudDataService.queryList(fraudData);
        ExcelUtil<FraudData> util = new ExcelUtil<FraudData>(FraudData.class);
        return util.exportExcel(list, "fraudData" );
    }

    /**
     * 获取欺诈数据详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:fraudData:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iFraudDataService.getById(id));
    }

    /**
     * 新增欺诈数据
     */
    @PreAuthorize("@ss.hasPermi('payment:fraudData:add')" )
    @Log(title = "欺诈数据" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FraudData fraudData) {
        return toAjax(iFraudDataService.save(fraudData) ? 1 : 0);
    }

    /**
     * 修改欺诈数据
     */
    @PreAuthorize("@ss.hasPermi('payment:fraudData:edit')" )
    @Log(title = "欺诈数据" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FraudData fraudData) {
        return toAjax(iFraudDataService.updateById(fraudData) ? 1 : 0);
    }

    /**
     * 删除欺诈数据
     */
    @PreAuthorize("@ss.hasPermi('payment:fraudData:remove')" )
    @Log(title = "欺诈数据" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iFraudDataService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
