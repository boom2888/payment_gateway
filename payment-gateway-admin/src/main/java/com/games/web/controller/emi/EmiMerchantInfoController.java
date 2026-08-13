package com.games.web.controller.emi;

import com.games.common.annotation.Log;
import com.games.common.core.controller.BaseController;
import com.games.common.core.domain.AjaxResult;
import com.games.common.core.page.TableDataInfo;
import com.games.common.enums.BusinessType;
import com.games.payment.service.IEmiMerchantInfoService;
import com.games.payment.vo.EmiMerchantInfoVo;
import com.games.system.mapper.SysUserMapper;
import com.games.system.service.impl.SysUserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * EMI 商户信息（个人+企业合并）Controller
 *
 * @author Ticker
 * @date 2025-10-17
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/merchant")
public class EmiMerchantInfoController extends BaseController {

    private final IEmiMerchantInfoService emiMerchantInfoService;

    @Autowired
    private SysUserServiceImpl sysUserService;
    /**
     * 查询商户信息列表（个人+企业合并）
     * 支持分页查询
     *
     * @param name 名称（模糊查询）
     * @param merchantType 商户类型（1：个人，2：企业）
     * @param status 状态
     * @param enabledStatus 启用状态
     * @return 商户信息列表
     */
    @GetMapping("/list")
    @Log(title = "商户信息列表", businessType = BusinessType.OTHER)
    public TableDataInfo list(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer merchantType,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer enabledStatus
    ) {
        startPage();
        List<EmiMerchantInfoVo> list = emiMerchantInfoService.selectMerchantList(name, merchantType, status, enabledStatus);
        for (EmiMerchantInfoVo emiMerchantInfoVo : list) {
            emiMerchantInfoVo.setAccount(sysUserService.selectUserById(emiMerchantInfoVo.getUserId()).getUserName());
        }
        return getDataTable(list);
    }

    /**
     * 导出商户信息列表
     *
     * @param name 名称（模糊查询）
     * @param merchantType 商户类型（1：个人，2：企业）
     * @param status 状态
     * @param enabledStatus 启用状态
     * @return 导出结果
     */
    @GetMapping("/export")
    @Log(title = "商户信息导出", businessType = BusinessType.EXPORT)
    public AjaxResult export(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer merchantType,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer enabledStatus
    ) {
        List<EmiMerchantInfoVo> list = emiMerchantInfoService.selectMerchantList(name, merchantType, status, enabledStatus);
        // 这里可以使用 ExcelUtil 进行导出，暂时返回数据
        return AjaxResult.success(list);
    }
}

