package com.games.web.controller.payment;

import com.games.common.annotation.Log;
import com.games.common.core.controller.BaseController;
import com.games.common.core.domain.AjaxResult;
import com.games.common.core.page.TableDataInfo;
import com.games.common.enums.BusinessType;
import com.games.common.enums.MovementImportStatus;
import com.games.common.utils.MessageUtils;
import com.games.common.utils.poi.ExcelUtil;
import com.games.payment.domain.MovementImport;
import com.games.payment.service.IMovementDataService;
import com.games.payment.service.IMovementImportService;
import org.apache.commons.io.IOUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * nuvei报告导入记录Controller
 *
 * @author Ticker
 * @date 2025-07-21
 */
@RestController
@RequestMapping("/payment/movementImport")
public class MovementImportController extends BaseController {
    @Resource
    private IMovementImportService movementImportService;
    @Resource
    private IMovementDataService movementDataService;

    /**
     * 查询nuvei报告导入记录列表
     */
    @PreAuthorize("@ss.hasPermi('payment:movementImport:list')")
    @GetMapping("/list")
    public TableDataInfo list(MovementImport movementImport) {
        startPage();
        List<MovementImport> list = movementImportService.selectAllList(movementImport);
        return getDataTable(list);
    }

    /**
     * 导出nuvei报告导入记录列表
     */
    @PreAuthorize("@ss.hasPermi('payment:movementImport:export')")
    @Log(title = "nuvei报告导入记录", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(MovementImport movementImport) {
        List<MovementImport> list = movementImportService.queryList(movementImport);
        ExcelUtil<MovementImport> util = new ExcelUtil<MovementImport>(MovementImport.class);
        return util.exportExcel(list, "movementImport");
    }

    /**
     * 获取nuvei报告导入记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:movementImport:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(movementImportService.getById(id));
    }

    /**
     * 新增nuvei报告导入记录
     */
    @PreAuthorize("@ss.hasPermi('payment:movementImport:add')")
    @Log(title = "nuvei报告导入记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MovementImport movementImport) {
        return toAjax(movementImportService.save(movementImport) ? 1 : 0);
    }

    /**
     * 修改nuvei报告导入记录
     */
    @PreAuthorize("@ss.hasPermi('payment:movementImport:edit')")
    @Log(title = "nuvei报告导入记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MovementImport movementImport) {
        return toAjax(movementImportService.updateById(movementImport) ? 1 : 0);
    }

    /**
     * 删除nuvei报告导入记录
     */
    @PreAuthorize("@ss.hasPermi('payment:movementImport:remove')")
    @Log(title = "nuvei报告导入记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        // 异步删除 MovementData 数据（后台执行，不阻塞）
        movementDataService.removeImportAndMovementData(Arrays.asList(ids));
        
        // 删除 MovementImport 记录
        boolean success = movementImportService.removeByIds(Arrays.asList(ids));
        
        return toAjax(success ? 1 : 0);
    }

    @Log(title = "nuvei报告导入记录", businessType = BusinessType.IMPORT)
//    @PreAuthorize("@ss.hasPermi('payment:movementImport:import')")
    @PostMapping("/importNuvieReport")
    public AjaxResult importNuvieReport(MultipartFile file) {
        return AjaxResult.success(movementImportService.importNuvieReport(file));
    }

    @Log(title = "订单结账导出", businessType = BusinessType.EXPORT)
//    @PreAuthorize("@ss.hasPermi('payment:movementImport:orderExport')")
    @GetMapping("/exportReconReport/{movementImportId}")
    public void exportReconReport(@PathVariable Long movementImportId, HttpServletResponse response) throws Exception {
        byte[] zipBytes = movementImportService.exportReconReport(movementImportId);
        response.reset();
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Content-Disposition", "attachment; filename=\"recon_report_" + movementImportId + ".zip\"");
        response.addHeader("Content-Length", String.valueOf(zipBytes.length));
        response.setContentType("application/octet-stream");
        IOUtils.write(zipBytes, response.getOutputStream());
    }

    @Log(title = "商户日报导出", businessType = BusinessType.EXPORT)
    @GetMapping("/exportDailySummary/{movementImportIds}")
    public AjaxResult exportDailySummary(@PathVariable String[] movementImportIds) throws Exception {
        return AjaxResult.success(movementImportService.exportDailySummary(Arrays.asList(movementImportIds)));
    }

    /**
     * 失效nuvei报告
     */
    @Log(title = "nuvei报告导入记录", businessType = BusinessType.DELETE)
    @PostMapping("invalid/{id}")
    public AjaxResult invalid(@PathVariable Long id) {
        return toAjax(movementImportService.invalid(id) ? 1 : 0);
    }

    /**
     * 重新生成nuvei的对账报告
     */
    @Log(title = "重新生成nuvei对账报告", businessType = BusinessType.UPDATE)
    @PostMapping("regenerate/{id}")
    public AjaxResult regenerate(@PathVariable Long id){
        movementDataService.calculateMerchantSettlementFee(id);
        return AjaxResult.success();
    }
}
