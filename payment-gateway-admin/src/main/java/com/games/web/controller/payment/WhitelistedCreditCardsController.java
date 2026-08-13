package com.games.web.controller.payment;

import com.games.common.annotation.Log;
import com.games.common.core.controller.BaseController;
import com.games.common.core.domain.AjaxResult;
import com.games.common.core.page.TableDataInfo;
import com.games.common.enums.BusinessType;
import com.games.common.utils.MessageUtils;
import com.games.common.utils.poi.ExcelUtil;
import com.games.payment.domain.WhitelistedCreditCards;
import com.games.payment.service.IWhitelistedCreditCardsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 白名单信用卡Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/whitelistedCreditCards")
public class WhitelistedCreditCardsController extends BaseController {

    private final IWhitelistedCreditCardsService iWhitelistedCreditCardsService;

    /**
     * 查询白名单信用卡列表
     */
    @PreAuthorize("@ss.hasPermi('payment:whitelistedCreditCards:list')")
    @GetMapping("/list")
    public TableDataInfo list(WhitelistedCreditCards whitelistedCreditCards) {
        startPage();
        List<WhitelistedCreditCards> list = iWhitelistedCreditCardsService.selectAllList(whitelistedCreditCards);
        return getDataTable(list);
    }

    /**
     * 导出白名单信用卡列表
     */
    @PreAuthorize("@ss.hasPermi('payment:whitelistedCreditCards:export')")
    @Log(title = "白名单信用卡", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(WhitelistedCreditCards whitelistedCreditCards) {
        List<WhitelistedCreditCards> list = iWhitelistedCreditCardsService.queryList(whitelistedCreditCards);
        ExcelUtil<WhitelistedCreditCards> util = new ExcelUtil<>(WhitelistedCreditCards.class);
        return util.exportExcel(list, "whitelistedCreditCards");
    }

    /**
     * 获取白名单信用卡详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:whitelistedCreditCards:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(iWhitelistedCreditCardsService.getById(id));
    }

    /**
     * 新增白名单信用卡
     */
    @PreAuthorize("@ss.hasPermi('payment:whitelistedCreditCards:add')")
    @Log(title = "白名单信用卡", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WhitelistedCreditCards whitelistedCreditCards) {
        return toAjax(iWhitelistedCreditCardsService.save(whitelistedCreditCards) ? 1 : 0);
    }

    /**
     * 修改白名单信用卡
     */
    @PreAuthorize("@ss.hasPermi('payment:whitelistedCreditCards:edit')")
    @Log(title = "白名单信用卡", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WhitelistedCreditCards whitelistedCreditCards) {
        return iWhitelistedCreditCardsService.updateWhiteList(whitelistedCreditCards);
    }

    /**
     * 删除白名单信用卡
     */
    @PreAuthorize("@ss.hasPermi('payment:whitelistedCreditCards:remove')")
    @Log(title = "白名单信用卡", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iWhitelistedCreditCardsService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }

    @GetMapping("/importTemplate")
    public AjaxResult importTemplate(@RequestParam(required = false) String lang) {
        // 转换语言代码：en_US -> en, zh_CN -> zh
        String langCode = "zh"; // 默认中文
        if (lang != null) {
            if (lang.startsWith("en")) {
                langCode = "en";
            } else if (lang.startsWith("zh")) {
                langCode = "zh";
            }
        }

        ExcelUtil<WhitelistedCreditCards> util = new ExcelUtil<>(WhitelistedCreditCards.class);
        util.init(null, MessageUtils.message("system.import.whitelist.template"),
                com.games.common.annotation.Excel.Type.IMPORT, langCode);
        return util.exportExcel();
    }
}
