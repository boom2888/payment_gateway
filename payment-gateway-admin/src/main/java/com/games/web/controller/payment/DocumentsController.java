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
import com.games.payment.domain.Documents;
import com.games.payment.service.IDocumentsService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 文档Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/documents" )
public class DocumentsController extends BaseController {

    private final IDocumentsService iDocumentsService;

    /**
     * 查询文档列表
     */
    @PreAuthorize("@ss.hasPermi('payment:documents:list')")
    @GetMapping("/list")
    public TableDataInfo list(Documents documents) {
        startPage();
        List<Documents> list = iDocumentsService.selectAllList(documents);
        return getDataTable(list);
    }

    /**
     * 导出文档列表
     */
    @PreAuthorize("@ss.hasPermi('payment:documents:export')" )
    @Log(title = "文档" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(Documents documents) {
        List<Documents> list = iDocumentsService.queryList(documents);
        ExcelUtil<Documents> util = new ExcelUtil<Documents>(Documents.class);
        return util.exportExcel(list, "documents" );
    }

    /**
     * 获取文档详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:documents:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iDocumentsService.getById(id));
    }

    /**
     * 新增文档
     */
    @PreAuthorize("@ss.hasPermi('payment:documents:add')" )
    @Log(title = "文档" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Documents documents) {
        return toAjax(iDocumentsService.save(documents) ? 1 : 0);
    }

    /**
     * 修改文档
     */
    @PreAuthorize("@ss.hasPermi('payment:documents:edit')" )
    @Log(title = "文档" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Documents documents) {
        return toAjax(iDocumentsService.updateById(documents) ? 1 : 0);
    }

    /**
     * 删除文档
     */
    @PreAuthorize("@ss.hasPermi('payment:documents:remove')" )
    @Log(title = "文档" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iDocumentsService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
