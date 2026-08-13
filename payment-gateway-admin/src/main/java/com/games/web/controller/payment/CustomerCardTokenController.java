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
import com.games.payment.domain.Card;
import com.games.payment.service.ICardService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 客户卡令牌Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/customerCardToken" )
public class CustomerCardTokenController extends BaseController {

    private final ICardService iCustomerCardTokenService;

    /**
     * 查询客户卡令牌列表
     */
    @PreAuthorize("@ss.hasPermi('payment:customerCardToken:list')")
    @GetMapping("/list")
    public TableDataInfo list(Card customerCardToken) {
        startPage();
        List<Card> list = iCustomerCardTokenService.selectAllList(customerCardToken);
        return getDataTable(list);
    }

    /**
     * 导出客户卡令牌列表
     */
    @PreAuthorize("@ss.hasPermi('payment:customerCardToken:export')" )
    @Log(title = "客户卡令牌" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(Card customerCardToken) {
        List<Card> list = iCustomerCardTokenService.queryList(customerCardToken);
        ExcelUtil<Card> util = new ExcelUtil<Card>(Card.class);
        return util.exportExcel(list, "customerCardToken" );
    }

    /**
     * 获取客户卡令牌详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:customerCardToken:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iCustomerCardTokenService.getById(id));
    }

    /**
     * 新增客户卡令牌
     */
    @PreAuthorize("@ss.hasPermi('payment:customerCardToken:add')" )
    @Log(title = "客户卡令牌" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Card customerCardToken) {
        return toAjax(iCustomerCardTokenService.save(customerCardToken) ? 1 : 0);
    }

    /**
     * 修改客户卡令牌
     */
    @PreAuthorize("@ss.hasPermi('payment:customerCardToken:edit')" )
    @Log(title = "客户卡令牌" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Card customerCardToken) {
        return toAjax(iCustomerCardTokenService.updateById(customerCardToken) ? 1 : 0);
    }

    /**
     * 删除客户卡令牌
     */
    @PreAuthorize("@ss.hasPermi('payment:customerCardToken:remove')" )
    @Log(title = "客户卡令牌" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iCustomerCardTokenService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
