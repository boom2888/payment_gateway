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
import com.games.payment.domain.ReconBatchJobFile;
import com.games.payment.service.IReconBatchJobFileService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 对账批处理作业文件Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/reconBatchJobFile" )
public class ReconBatchJobFileController extends BaseController {

    private final IReconBatchJobFileService iReconBatchJobFileService;

    /**
     * 查询对账批处理作业文件列表
     */
    @PreAuthorize("@ss.hasPermi('payment:reconBatchJobFile:list')")
    @GetMapping("/list")
    public TableDataInfo list(ReconBatchJobFile reconBatchJobFile) {
        startPage();
        List<ReconBatchJobFile> list = iReconBatchJobFileService.selectAllList(reconBatchJobFile);
        return getDataTable(list);
    }

    /**
     * 导出对账批处理作业文件列表
     */
    @PreAuthorize("@ss.hasPermi('payment:reconBatchJobFile:export')" )
    @Log(title = "对账批处理作业文件" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(ReconBatchJobFile reconBatchJobFile) {
        List<ReconBatchJobFile> list = iReconBatchJobFileService.queryList(reconBatchJobFile);
        ExcelUtil<ReconBatchJobFile> util = new ExcelUtil<ReconBatchJobFile>(ReconBatchJobFile.class);
        return util.exportExcel(list, "reconBatchJobFile" );
    }

    /**
     * 获取对账批处理作业文件详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:reconBatchJobFile:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iReconBatchJobFileService.getById(id));
    }

    /**
     * 新增对账批处理作业文件
     */
    @PreAuthorize("@ss.hasPermi('payment:reconBatchJobFile:add')" )
    @Log(title = "对账批处理作业文件" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ReconBatchJobFile reconBatchJobFile) {
        return toAjax(iReconBatchJobFileService.save(reconBatchJobFile) ? 1 : 0);
    }

    /**
     * 修改对账批处理作业文件
     */
    @PreAuthorize("@ss.hasPermi('payment:reconBatchJobFile:edit')" )
    @Log(title = "对账批处理作业文件" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ReconBatchJobFile reconBatchJobFile) {
        return toAjax(iReconBatchJobFileService.updateById(reconBatchJobFile) ? 1 : 0);
    }

    /**
     * 删除对账批处理作业文件
     */
    @PreAuthorize("@ss.hasPermi('payment:reconBatchJobFile:remove')" )
    @Log(title = "对账批处理作业文件" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iReconBatchJobFileService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
