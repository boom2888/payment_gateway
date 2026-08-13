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
import com.games.payment.domain.UploadCardRecord;
import com.games.payment.service.IUploadCardRecordService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 上传卡记录Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/record" )
public class UploadCardRecordController extends BaseController {

    private final IUploadCardRecordService iUploadCardRecordService;

    /**
     * 查询上传卡记录列表
     */
    @PreAuthorize("@ss.hasPermi('payment:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(UploadCardRecord uploadCardRecord) {
        startPage();
        List<UploadCardRecord> list = iUploadCardRecordService.selectAllList(uploadCardRecord);
        return getDataTable(list);
    }

    /**
     * 导出上传卡记录列表
     */
    @PreAuthorize("@ss.hasPermi('payment:record:export')" )
    @Log(title = "上传卡记录" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(UploadCardRecord uploadCardRecord) {
        List<UploadCardRecord> list = iUploadCardRecordService.queryList(uploadCardRecord);
        ExcelUtil<UploadCardRecord> util = new ExcelUtil<UploadCardRecord>(UploadCardRecord.class);
        return util.exportExcel(list, "record" );
    }

    /**
     * 获取上传卡记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:record:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iUploadCardRecordService.getById(id));
    }

    /**
     * 新增上传卡记录
     */
    @PreAuthorize("@ss.hasPermi('payment:record:add')" )
    @Log(title = "上传卡记录" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UploadCardRecord uploadCardRecord) {
        return toAjax(iUploadCardRecordService.save(uploadCardRecord) ? 1 : 0);
    }

    /**
     * 修改上传卡记录
     */
    @PreAuthorize("@ss.hasPermi('payment:record:edit')" )
    @Log(title = "上传卡记录" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UploadCardRecord uploadCardRecord) {
        return toAjax(iUploadCardRecordService.updateById(uploadCardRecord) ? 1 : 0);
    }

    /**
     * 删除上传卡记录
     */
    @PreAuthorize("@ss.hasPermi('payment:record:remove')" )
    @Log(title = "上传卡记录" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iUploadCardRecordService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
