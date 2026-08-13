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
import com.games.payment.domain.CryptoTransaction;
import com.games.payment.service.ICryptoTransactionService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 加密货币交易Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/cryptoTransaction" )
public class CryptoTransactionController extends BaseController {

    private final ICryptoTransactionService iCryptoTransactionService;

    /**
     * 查询加密货币交易列表
     */
    @PreAuthorize("@ss.hasPermi('payment:cryptoTransaction:list')")
    @GetMapping("/list")
    public TableDataInfo list(CryptoTransaction cryptoTransaction) {
        startPage();
        List<CryptoTransaction> list = iCryptoTransactionService.selectAllList(cryptoTransaction);
        return getDataTable(list);
    }

    /**
     * 导出加密货币交易列表
     */
    @PreAuthorize("@ss.hasPermi('payment:cryptoTransaction:export')" )
    @Log(title = "加密货币交易" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(CryptoTransaction cryptoTransaction) {
        List<CryptoTransaction> list = iCryptoTransactionService.queryList(cryptoTransaction);
        ExcelUtil<CryptoTransaction> util = new ExcelUtil<CryptoTransaction>(CryptoTransaction.class);
        return util.exportExcel(list, "cryptoTransaction" );
    }

    /**
     * 获取加密货币交易详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:cryptoTransaction:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iCryptoTransactionService.getById(id));
    }

    /**
     * 新增加密货币交易
     */
    @PreAuthorize("@ss.hasPermi('payment:cryptoTransaction:add')" )
    @Log(title = "加密货币交易" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CryptoTransaction cryptoTransaction) {
        return toAjax(iCryptoTransactionService.save(cryptoTransaction) ? 1 : 0);
    }

    /**
     * 修改加密货币交易
     */
    @PreAuthorize("@ss.hasPermi('payment:cryptoTransaction:edit')" )
    @Log(title = "加密货币交易" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CryptoTransaction cryptoTransaction) {
        return toAjax(iCryptoTransactionService.updateById(cryptoTransaction) ? 1 : 0);
    }

    /**
     * 删除加密货币交易
     */
    @PreAuthorize("@ss.hasPermi('payment:cryptoTransaction:remove')" )
    @Log(title = "加密货币交易" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iCryptoTransactionService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
