package com.games.web.controller.emi;

import java.util.List;
import java.util.Arrays;

import com.games.common.core.domain.model.LoginUser;
import com.games.common.utils.ServletUtils;
import com.games.framework.web.service.TokenService;
import com.games.payment.domain.EmiOrderManagement;
import com.games.payment.domain.EmiEnterpriseInfo;
import com.games.payment.domain.EmiPersonalInformation;
import com.games.payment.service.IEmiOrderManagementService;
import com.games.payment.service.IEmiEnterpriseInfoService;
import com.games.payment.service.IEmiPersonalInformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import com.games.common.utils.SecurityUtils;
import java.util.ArrayList;
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

import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 订单管理Controller
 * 
 * @author Ticker
 * @date 2025-10-16
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/management" )
public class EmiOrderManagementController extends BaseController {

    private final IEmiOrderManagementService iEmiOrderManagementService;
    private final IEmiEnterpriseInfoService iEmiEnterpriseInfoService;
    private final IEmiPersonalInformationService iEmiPersonalInformationService;

    private final TokenService tokenService;

    /**
     * 查询订单管理列表
     */
    @PreAuthorize("@ss.hasPermi('payment:management:list')")
    @GetMapping("/list")
    public TableDataInfo list(EmiOrderManagement emiOrderManagement) {
        startPage();
        
        // 获取当前用户ID
        // 获取当前登录用户信息
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long currentUserId = loginUser.getUserId();
        
        // 如果是管理员（ID=1），可以查看全部数据
        if (currentUserId != null && currentUserId == 138) {
            List<EmiOrderManagement> list = iEmiOrderManagementService.selectAllList(emiOrderManagement);
            return getDataTable(list);
        }
        
        // 普通用户只能查看与自己银行卡号相关的订单数据
        List<String> userCardNumbers = getUserCardNumbers(currentUserId);
        
        if (userCardNumbers.isEmpty()) {
            // 如果用户没有银行卡号，返回空列表
            return getDataTable(new ArrayList<>());
        }
        
        // 设置卡号过滤条件
        emiOrderManagement.setUserCardNumbers(userCardNumbers);
        List<EmiOrderManagement> list = iEmiOrderManagementService.selectAllList(emiOrderManagement);
        return getDataTable(list);
    }

    /**
     * 导出订单管理列表
     */
    @PreAuthorize("@ss.hasPermi('payment:management:export')" )
    @Log(title = "订单管理" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(EmiOrderManagement emiOrderManagement) {
        // 获取当前用户ID
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long currentUserId = loginUser.getUserId();
        
        // 如果是管理员（ID=1），可以导出全部数据
        if (currentUserId != null && currentUserId == 1L) {
            List<EmiOrderManagement> list = iEmiOrderManagementService.queryList(emiOrderManagement);
            ExcelUtil<EmiOrderManagement> util = new ExcelUtil<EmiOrderManagement>(EmiOrderManagement.class);
            return util.exportExcel(list, "management" );
        }
        
        // 普通用户只能导出与自己银行卡号相关的订单数据
        List<String> userCardNumbers = getUserCardNumbers(currentUserId);
        
        if (userCardNumbers.isEmpty()) {
            // 如果用户没有银行卡号，返回空列表
            ExcelUtil<EmiOrderManagement> util = new ExcelUtil<EmiOrderManagement>(EmiOrderManagement.class);
            return util.exportExcel(new ArrayList<>(), "management" );
        }
        
        // 设置卡号过滤条件
        emiOrderManagement.setUserCardNumbers(userCardNumbers);
        List<EmiOrderManagement> list = iEmiOrderManagementService.queryList(emiOrderManagement);
        ExcelUtil<EmiOrderManagement> util = new ExcelUtil<EmiOrderManagement>(EmiOrderManagement.class);
        return util.exportExcel(list, "management" );
    }

    /**
     * 获取订单管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:management:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iEmiOrderManagementService.getById(id));
    }

    /**
     * 新增订单管理
     */
    @PreAuthorize("@ss.hasPermi('payment:management:add')" )
    @Log(title = "订单管理" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EmiOrderManagement emiOrderManagement) {
        return toAjax(iEmiOrderManagementService.save(emiOrderManagement) ? 1 : 0);
    }

    /**
     * 修改订单管理
     */
    @PreAuthorize("@ss.hasPermi('payment:management:edit')" )
    @Log(title = "订单管理" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EmiOrderManagement emiOrderManagement) {
        return toAjax(iEmiOrderManagementService.updateById(emiOrderManagement) ? 1 : 0);
    }

    /**
     * 删除订单管理
     */
    @PreAuthorize("@ss.hasPermi('payment:management:remove')" )
    @Log(title = "订单管理" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iEmiOrderManagementService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
    
    /**
     * 获取用户的银行卡号列表
     * 从企业信息表和个人信息表中获取当前用户的所有银行卡号
     * 
     * @param userId 用户ID
     * @return 银行卡号列表
     */
    private List<String> getUserCardNumbers(Long userId) {
        List<String> cardNumbers = new ArrayList<>();
        
        try {
            // 查询企业信息中的银行卡号
            EmiEnterpriseInfo enterpriseInfo = iEmiEnterpriseInfoService.getByUserId(userId);
            if (enterpriseInfo != null && enterpriseInfo.getCardNumber() != null && !enterpriseInfo.getCardNumber().trim().isEmpty()) {
                cardNumbers.add(enterpriseInfo.getCardNumber().trim());
            }
            
            // 查询个人信息中的银行卡号
            EmiPersonalInformation personalInfo = iEmiPersonalInformationService.getByUserId(userId);
            if (personalInfo != null && personalInfo.getCardNumber() != null && !personalInfo.getCardNumber().trim().isEmpty()) {
                cardNumbers.add(personalInfo.getCardNumber().trim());
            }
        } catch (Exception e) {
            // 记录日志但不抛出异常，返回空列表
            System.err.println("获取用户银行卡号时发生错误: " + e.getMessage());
        }
        
        return cardNumbers;
    }
}
