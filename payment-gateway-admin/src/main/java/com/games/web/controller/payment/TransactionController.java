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
import com.games.payment.domain.Transaction;
import com.games.payment.service.ITransactionService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 交易Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/transaction" )
public class TransactionController extends BaseController {

    private final ITransactionService iTransactionService;

    /**
     * 查询交易列表
     */
    @PreAuthorize("@ss.hasPermi('payment:transaction:list')")
    @GetMapping("/list")
    public TableDataInfo list(Transaction transaction) {
        startPage();
        List<Transaction> list = iTransactionService.selectAllList(transaction);
        return getDataTable(list);
    }

    /**
     * 导出交易列表
     */
    @PreAuthorize("@ss.hasPermi('payment:transaction:export')" )
    @Log(title = "交易" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(Transaction transaction) {
        List<Transaction> list = iTransactionService.queryList(transaction);
        ExcelUtil<Transaction> util = new ExcelUtil<Transaction>(Transaction.class);
        return util.exportExcel(list, "transaction" );
    }

    /**
     * 获取交易详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:transaction:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iTransactionService.getById(id));
    }

    /**
     * 新增交易
     */
    @PreAuthorize("@ss.hasPermi('payment:transaction:add')" )
    @Log(title = "交易" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Transaction transaction) {
        return toAjax(iTransactionService.save(transaction) ? 1 : 0);
    }

    /**
     * 修改交易
     */
    @PreAuthorize("@ss.hasPermi('payment:transaction:edit')" )
    @Log(title = "交易" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Transaction transaction) {
        return toAjax(iTransactionService.updateById(transaction) ? 1 : 0);
    }

    /**
     * 删除交易
     */
    @PreAuthorize("@ss.hasPermi('payment:transaction:remove')" )
    @Log(title = "交易" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iTransactionService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
