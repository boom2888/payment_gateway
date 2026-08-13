package com.games.web.controller.payment;

import com.games.common.annotation.Log;
import com.games.common.core.controller.BaseController;
import com.games.common.core.domain.AjaxResult;
import com.games.common.core.page.TableDataInfo;
import com.games.common.enums.BusinessType;
import com.games.common.utils.poi.ExcelUtil;
import com.games.payment.domain.WhiteUploadRecord;
import com.games.payment.service.IWhiteUploadRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

/**
 * 白名单上传记录Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/whiteUploadRecord")
public class WhiteUploadRecordController extends BaseController {

    private final IWhiteUploadRecordService iWhiteUploadRecordService;

    /**
     * 查询白名单上传记录列表
     */
    @PreAuthorize("@ss.hasPermi('payment:whiteUploadRecord:list')")
    @GetMapping("/list")
    public TableDataInfo list(WhiteUploadRecord whiteUploadRecord) {
        startPage();
        List<WhiteUploadRecord> list = iWhiteUploadRecordService.selectAllList(whiteUploadRecord);
        return getDataTable(list);
    }

    /**
     * 导出白名单上传记录列表
     */
    @PreAuthorize("@ss.hasPermi('payment:whiteUploadRecord:export')")
    @Log(title = "白名单上传记录", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(WhiteUploadRecord whiteUploadRecord) {
        List<WhiteUploadRecord> list = iWhiteUploadRecordService.queryList(whiteUploadRecord);
        ExcelUtil<WhiteUploadRecord> util = new ExcelUtil<WhiteUploadRecord>(WhiteUploadRecord.class);
        return util.exportExcel(list, "whiteUploadRecord");
    }

    /**
     * 获取白名单上传记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:whiteUploadRecord:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(iWhiteUploadRecordService.getById(id));
    }

    /**
     * 新增白名单上传记录
     */
    @PreAuthorize("@ss.hasPermi('payment:whiteUploadRecord:add')")
    @Log(title = "白名单上传记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WhiteUploadRecord whiteUploadRecord) {
        return toAjax(iWhiteUploadRecordService.save(whiteUploadRecord) ? 1 : 0);
    }

    /**
     * 修改白名单上传记录
     */
    @PreAuthorize("@ss.hasPermi('payment:whiteUploadRecord:edit')")
    @Log(title = "白名单上传记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WhiteUploadRecord whiteUploadRecord) {
        return toAjax(iWhiteUploadRecordService.updateById(whiteUploadRecord) ? 1 : 0);
    }

    /**
     * 删除白名单上传记录
     */
    @PreAuthorize("@ss.hasPermi('payment:whiteUploadRecord:remove')")
    @Log(title = "白名单上传记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iWhiteUploadRecordService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }

    @Log(title = "白名单导入", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('payment:whiteUploadRecord:import')")
    @PostMapping("/import")
    public AjaxResult importWhite(MultipartFile file, Integer importType) throws Exception {
        log.info("接收到导入请求 - 文件名: {}, importType: {}", file.getOriginalFilename(), importType);
        return AjaxResult.success(iWhiteUploadRecordService.importWhite(file, importType));
    }
}
