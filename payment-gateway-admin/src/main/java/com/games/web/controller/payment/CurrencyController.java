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
import com.games.payment.domain.Currency;
import com.games.payment.service.ICurrencyService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 货币Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/currency" )
public class CurrencyController extends BaseController {

    private final ICurrencyService iCurrencyService;

    /**
     * 查询货币列表
     */
    @PreAuthorize("@ss.hasPermi('payment:currency:list')")
    @GetMapping("/list")
    public TableDataInfo list(Currency currency) {
        startPage();
        List<Currency> list = iCurrencyService.selectAllList(currency);
        return getDataTable(list);
    }

    /**
     * 导出货币列表
     */
    @PreAuthorize("@ss.hasPermi('payment:currency:export')" )
    @Log(title = "货币" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(Currency currency) {
        List<Currency> list = iCurrencyService.queryList(currency);
        ExcelUtil<Currency> util = new ExcelUtil<Currency>(Currency.class);
        return util.exportExcel(list, "currency" );
    }

    /**
     * 获取货币详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:currency:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iCurrencyService.getById(id));
    }

    /**
     * 新增货币
     */
    @PreAuthorize("@ss.hasPermi('payment:currency:add')" )
    @Log(title = "货币" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Currency currency) {
        return toAjax(iCurrencyService.save(currency) ? 1 : 0);
    }

    /**
     * 修改货币
     */
    @PreAuthorize("@ss.hasPermi('payment:currency:edit')" )
    @Log(title = "货币" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Currency currency) {
        return toAjax(iCurrencyService.updateById(currency) ? 1 : 0);
    }

    /**
     * 删除货币
     */
    @PreAuthorize("@ss.hasPermi('payment:currency:remove')" )
    @Log(title = "货币" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iCurrencyService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
