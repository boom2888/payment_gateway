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
import org.apache.commons.lang3.StringUtils;
import com.games.common.annotation.Log;
import com.games.common.core.controller.BaseController;
import com.games.common.core.domain.AjaxResult;
import com.games.common.enums.BusinessType;
import com.games.payment.domain.IpInfo;
import com.games.payment.service.IIpInfoService;
import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * IP地址Controller
 * 
 * @author Ticker
 * @date 2025-09-24
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/ipinfo" )
public class IpInfoController extends BaseController {

    private final IIpInfoService iIpInfoService;

    /**
     * 查询IP地址列表
     */
    @PreAuthorize("@ss.hasPermi('payment:ipinfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(IpInfo ipInfo) {
        startPage();
        List<IpInfo> list = iIpInfoService.selectAllList(ipInfo);
        return getDataTable(list);
    }

    /**
     * 导出IP地址列表
     */
    @PreAuthorize("@ss.hasPermi('payment:ipinfo:export')" )
    @Log(title = "IP地址" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(IpInfo ipInfo) {
        List<IpInfo> list = iIpInfoService.queryList(ipInfo);
        ExcelUtil<IpInfo> util = new ExcelUtil<IpInfo>(IpInfo.class);
        return util.exportExcel(list, "ipinfo" );
    }

    /**
     * 获取IP地址详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:ipinfo:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iIpInfoService.getById(id));
    }

    /**
     * 新增IP地址
     */
    @PreAuthorize("@ss.hasPermi('payment:ipinfo:add')" )
    @Log(title = "IP地址" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody IpInfo ipInfo) {
        if (StringUtils.isNotBlank(ipInfo.getIp()) && iIpInfoService.existsByIp(ipInfo.getIp())) {
            return AjaxResult.error("IP地址已存在");
        }
        return toAjax(iIpInfoService.save(ipInfo) ? 1 : 0);
    }

    /**
     * 修改IP地址
     */
    @PreAuthorize("@ss.hasPermi('payment:ipinfo:edit')" )
    @Log(title = "IP地址" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody IpInfo ipInfo) {
        if (StringUtils.isNotBlank(ipInfo.getIp()) && iIpInfoService.existsByIpExceptId(ipInfo.getIp(), ipInfo.getId())) {
            return AjaxResult.error("IP地址已存在");
        }
        return toAjax(iIpInfoService.updateById(ipInfo) ? 1 : 0);
    }

    /**
     * 删除IP地址
     */
    @PreAuthorize("@ss.hasPermi('payment:ipinfo:remove')" )
    @Log(title = "IP地址" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iIpInfoService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
