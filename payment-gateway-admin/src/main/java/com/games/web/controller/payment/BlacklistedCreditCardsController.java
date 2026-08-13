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
import com.games.payment.domain.BlacklistedCreditCards;
import com.games.payment.service.IBlacklistedCreditCardsService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 黑名单信用卡Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/blacklistedCreditCards" )
public class BlacklistedCreditCardsController extends BaseController {

    private final IBlacklistedCreditCardsService iBlacklistedCreditCardsService;

    /**
     * 查询黑名单信用卡列表
     */
    @PreAuthorize("@ss.hasPermi('payment:blacklistedCreditCards:list')")
    @GetMapping("/list")
    public TableDataInfo list(BlacklistedCreditCards blacklistedCreditCards) {
        startPage();
        List<BlacklistedCreditCards> list = iBlacklistedCreditCardsService.selectAllList(blacklistedCreditCards);
        return getDataTable(list);
    }

    /**
     * 导出黑名单信用卡列表
     */
    @PreAuthorize("@ss.hasPermi('payment:blacklistedCreditCards:export')" )
    @Log(title = "黑名单信用卡" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(BlacklistedCreditCards blacklistedCreditCards) {
        List<BlacklistedCreditCards> list = iBlacklistedCreditCardsService.queryList(blacklistedCreditCards);
        ExcelUtil<BlacklistedCreditCards> util = new ExcelUtil<BlacklistedCreditCards>(BlacklistedCreditCards.class);
        return util.exportExcel(list, "cards" );
    }

    /**
     * 获取黑名单信用卡详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:blacklistedCreditCards:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iBlacklistedCreditCardsService.getById(id));
    }

    /**
     * 新增黑名单信用卡
     */
    @PreAuthorize("@ss.hasPermi('payment:blacklistedCreditCards:add')" )
    @Log(title = "黑名单信用卡" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BlacklistedCreditCards blacklistedCreditCards) {
        return toAjax(iBlacklistedCreditCardsService.save(blacklistedCreditCards) ? 1 : 0);
    }

    /**
     * 修改黑名单信用卡
     */
    @PreAuthorize("@ss.hasPermi('payment:blacklistedCreditCards:edit')" )
    @Log(title = "黑名单信用卡" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BlacklistedCreditCards blacklistedCreditCards) {
        return toAjax(iBlacklistedCreditCardsService.updateById(blacklistedCreditCards) ? 1 : 0);
    }

    /**
     * 删除黑名单信用卡
     */
    @PreAuthorize("@ss.hasPermi('payment:blacklistedCreditCards:remove')" )
    @Log(title = "黑名单信用卡" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iBlacklistedCreditCardsService.deleteByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}