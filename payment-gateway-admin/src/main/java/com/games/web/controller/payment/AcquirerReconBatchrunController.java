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
import com.games.payment.domain.AcquirerReconBatchrun;
import com.games.payment.service.IAcquirerReconBatchrunService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 收单机构对账批处理Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/acquirerReconBatchrun" )
public class AcquirerReconBatchrunController extends BaseController {

    private final IAcquirerReconBatchrunService iAcquirerReconBatchrunService;

    /**
     * 查询收单机构对账批处理列表
     */
    @PreAuthorize("@ss.hasPermi('payment:acquirerReconBatchrun:list')")
    @GetMapping("/list")
    public TableDataInfo list(AcquirerReconBatchrun acquirerReconBatchrun) {
        startPage();
        List<AcquirerReconBatchrun> list = iAcquirerReconBatchrunService.selectAllList(acquirerReconBatchrun);
        return getDataTable(list);
    }

    /**
     * 导出收单机构对账批处理列表
     */
    @PreAuthorize("@ss.hasPermi('payment:acquirerReconBatchrun:export')" )
    @Log(title = "收单机构对账批处理" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(AcquirerReconBatchrun acquirerReconBatchrun) {
        List<AcquirerReconBatchrun> list = iAcquirerReconBatchrunService.queryList(acquirerReconBatchrun);
        ExcelUtil<AcquirerReconBatchrun> util = new ExcelUtil<AcquirerReconBatchrun>(AcquirerReconBatchrun.class);
        return util.exportExcel(list, "acquirerReconBatchrun" );
    }

    /**
     * 获取收单机构对账批处理详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:acquirerReconBatchrun:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iAcquirerReconBatchrunService.getById(id));
    }

    /**
     * 新增收单机构对账批处理
     */
    @PreAuthorize("@ss.hasPermi('payment:acquirerReconBatchrun:add')" )
    @Log(title = "收单机构对账批处理" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AcquirerReconBatchrun acquirerReconBatchrun) {
        return toAjax(iAcquirerReconBatchrunService.save(acquirerReconBatchrun) ? 1 : 0);
    }

    /**
     * 修改收单机构对账批处理
     */
    @PreAuthorize("@ss.hasPermi('payment:acquirerReconBatchrun:edit')" )
    @Log(title = "收单机构对账批处理" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AcquirerReconBatchrun acquirerReconBatchrun) {
        return toAjax(iAcquirerReconBatchrunService.updateById(acquirerReconBatchrun) ? 1 : 0);
    }

    /**
     * 删除收单机构对账批处理
     */
    @PreAuthorize("@ss.hasPermi('payment:acquirerReconBatchrun:remove')" )
    @Log(title = "收单机构对账批处理" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iAcquirerReconBatchrunService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
