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
import com.games.payment.domain.HouseWallet;
import com.games.payment.service.IHouseWalletService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 平台钱包Controller
 * 
 * @author Ticker
 * @date 2025-07-09
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/houseWallet" )
public class HouseWalletController extends BaseController {

    private final IHouseWalletService iHouseWalletService;

    /**
     * 查询平台钱包列表
     */
    @PreAuthorize("@ss.hasPermi('payment:houseWallet:list')")
    @GetMapping("/list")
    public TableDataInfo list(HouseWallet houseWallet) {
        startPage();
        List<HouseWallet> list = iHouseWalletService.selectAllList(houseWallet);
        return getDataTable(list);
    }

    /**
     * 导出平台钱包列表
     */
    @PreAuthorize("@ss.hasPermi('payment:houseWallet:export')" )
    @Log(title = "平台钱包" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(HouseWallet houseWallet) {
        List<HouseWallet> list = iHouseWalletService.queryList(houseWallet);
        ExcelUtil<HouseWallet> util = new ExcelUtil<HouseWallet>(HouseWallet.class);
        return util.exportExcel(list, "houseWallet" );
    }

    /**
     * 获取平台钱包详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:houseWallet:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iHouseWalletService.getById(id));
    }

    /**
     * 新增平台钱包
     */
    @PreAuthorize("@ss.hasPermi('payment:houseWallet:add')" )
    @Log(title = "平台钱包" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody HouseWallet houseWallet) {
        return toAjax(iHouseWalletService.save(houseWallet) ? 1 : 0);
    }

    /**
     * 修改平台钱包
     */
    @PreAuthorize("@ss.hasPermi('payment:houseWallet:edit')" )
    @Log(title = "平台钱包" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HouseWallet houseWallet) {
        return toAjax(iHouseWalletService.updateById(houseWallet) ? 1 : 0);
    }

    /**
     * 删除平台钱包
     */
    @PreAuthorize("@ss.hasPermi('payment:houseWallet:remove')" )
    @Log(title = "平台钱包" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iHouseWalletService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
