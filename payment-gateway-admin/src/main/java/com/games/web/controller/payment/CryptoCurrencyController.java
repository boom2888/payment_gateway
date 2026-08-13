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
import com.games.payment.domain.Crypto;
import com.games.payment.service.ICryptoService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 加密货币Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/cryptoCurrency" )
public class CryptoCurrencyController extends BaseController {

    private final ICryptoService iCryptoCurrencyService;

    /**
     * 查询加密货币列表
     */
    @PreAuthorize("@ss.hasPermi('payment:cryptoCurrency:list')")
    @GetMapping("/list")
    public TableDataInfo list(Crypto cryptoCurrency) {
        startPage();
        List<Crypto> list = iCryptoCurrencyService.selectAllList(cryptoCurrency);
        return getDataTable(list);
    }

    /**
     * 导出加密货币列表
     */
    @PreAuthorize("@ss.hasPermi('payment:cryptoCurrency:export')" )
    @Log(title = "加密货币" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(Crypto cryptoCurrency) {
        List<Crypto> list = iCryptoCurrencyService.queryList(cryptoCurrency);
        ExcelUtil<Crypto> util = new ExcelUtil<Crypto>(Crypto.class);
        return util.exportExcel(list, "cryptoCurrency" );
    }

    /**
     * 获取加密货币详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:cryptoCurrency:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iCryptoCurrencyService.getById(id));
    }

    /**
     * 新增加密货币
     */
    @PreAuthorize("@ss.hasPermi('payment:cryptoCurrency:add')" )
    @Log(title = "加密货币" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Crypto cryptoCurrency) {
        return toAjax(iCryptoCurrencyService.save(cryptoCurrency) ? 1 : 0);
    }

    /**
     * 修改加密货币
     */
    @PreAuthorize("@ss.hasPermi('payment:cryptoCurrency:edit')" )
    @Log(title = "加密货币" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Crypto cryptoCurrency) {
        return toAjax(iCryptoCurrencyService.updateById(cryptoCurrency) ? 1 : 0);
    }

    /**
     * 删除加密货币
     */
    @PreAuthorize("@ss.hasPermi('payment:cryptoCurrency:remove')" )
    @Log(title = "加密货币" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iCryptoCurrencyService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
