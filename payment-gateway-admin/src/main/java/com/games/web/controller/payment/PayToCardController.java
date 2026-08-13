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
import com.games.payment.domain.PayToCard;
import com.games.payment.service.IPayToCardService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 支付到卡Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/payToCard" )
public class PayToCardController extends BaseController {

    private final IPayToCardService iPayToCardService;

    /**
     * 查询支付到卡列表
     */
    @PreAuthorize("@ss.hasPermi('payment:payToCard:list')")
    @GetMapping("/list")
    public TableDataInfo list(PayToCard payToCard) {
        startPage();
        List<PayToCard> list = iPayToCardService.selectAllList(payToCard);
        return getDataTable(list);
    }

    /**
     * 导出支付到卡列表
     */
    @PreAuthorize("@ss.hasPermi('payment:payToCard:export')" )
    @Log(title = "支付到卡" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(PayToCard payToCard) {
        List<PayToCard> list = iPayToCardService.queryList(payToCard);
        ExcelUtil<PayToCard> util = new ExcelUtil<PayToCard>(PayToCard.class);
        return util.exportExcel(list, "payToCard" );
    }

    /**
     * 获取支付到卡详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:payToCard:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iPayToCardService.getById(id));
    }

    /**
     * 新增支付到卡
     */
    @PreAuthorize("@ss.hasPermi('payment:payToCard:add')" )
    @Log(title = "支付到卡" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PayToCard payToCard) {
        return toAjax(iPayToCardService.save(payToCard) ? 1 : 0);
    }

    /**
     * 修改支付到卡
     */
    @PreAuthorize("@ss.hasPermi('payment:payToCard:edit')" )
    @Log(title = "支付到卡" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PayToCard payToCard) {
        return toAjax(iPayToCardService.updateById(payToCard) ? 1 : 0);
    }

    /**
     * 删除支付到卡
     */
    @PreAuthorize("@ss.hasPermi('payment:payToCard:remove')" )
    @Log(title = "支付到卡" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iPayToCardService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
