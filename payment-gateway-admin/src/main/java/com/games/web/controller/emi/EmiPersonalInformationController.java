package com.games.web.controller.emi;

import java.util.List;
import java.util.Arrays;

import com.games.common.core.domain.entity.SysUser;
import com.games.common.core.domain.model.LoginUser;
import com.games.common.utils.MessageUtils;
import com.games.common.utils.ServletUtils;
import com.games.framework.web.service.TokenService;
import com.games.payment.domain.EmiPersonalInformation;
import com.games.payment.service.IEmiPersonalInformationService;
import com.games.system.mapper.SysUserMapper;
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

import com.games.common.utils.poi.ExcelUtil;
import com.games.common.core.page.TableDataInfo;

/**
 * 个人信息Controller
 * 
 * @author Ticker
 * @date 2025-10-16
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/information" )
public class EmiPersonalInformationController extends BaseController {

    private final IEmiPersonalInformationService iEmiPersonalInformationService;

    private final TokenService tokenService;

    private final SysUserMapper sysUserMapper;
    /**
     * 查询个人信息列表
     */

    @GetMapping("/list")
    public TableDataInfo list(EmiPersonalInformation emiPersonalInformation) {
        startPage();
        List<EmiPersonalInformation> list = iEmiPersonalInformationService.selectAllList(emiPersonalInformation);
        return getDataTable(list);
    }

    /**
     * 导出个人信息列表
     */

    @Log(title = "个人信息" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(EmiPersonalInformation emiPersonalInformation) {
        List<EmiPersonalInformation> list = iEmiPersonalInformationService.queryList(emiPersonalInformation);
        ExcelUtil<EmiPersonalInformation> util = new ExcelUtil<EmiPersonalInformation>(EmiPersonalInformation.class);
        return util.exportExcel(list, "information" );
    }

    /**
     * 获取个人信息详细信息
     */

    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iEmiPersonalInformationService.getById(id));
    }

    /**
     * 根据用户ID获取个人信息详细信息
     */
    @GetMapping("/user/{userId}")
    public AjaxResult getInfoByUserId(@PathVariable("userId") Long userId) {
        EmiPersonalInformation personalInfo = iEmiPersonalInformationService.getByUserId(userId);
        personalInfo.setAccount(sysUserMapper.selectUserById(userId).getUserName());
        if (personalInfo == null) {
            return AjaxResult.error(MessageUtils.message("emi.personal.info.not.found"));
        }
        return AjaxResult.success(personalInfo);
    }

    /**
     * 新增个人信息
     */

    @Log(title = "个人信息" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EmiPersonalInformation emiPersonalInformation) {

        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        Long userId = user.getUserId();
        emiPersonalInformation.setUserId(userId);
        // 查询是否已有记录（按当前用户）
        EmiPersonalInformation existing = iEmiPersonalInformationService.getByUserId(userId);
        if (existing == null) {
            // 首次提交：新增
            emiPersonalInformation.setStatus(1);
            emiPersonalInformation.setEnabledStatus(0);
            boolean save = iEmiPersonalInformationService.save(emiPersonalInformation);
            return toAjax(save ? 1 : 0);
        } else {
            // 二次及以后提交：修改（保持原记录主键）
            emiPersonalInformation.setId(existing.getId());
            boolean updated = iEmiPersonalInformationService.updateById(emiPersonalInformation);
            return toAjax(updated ? 1 : 0);
        }
    }

    /**
     * 修改个人信息
     */

    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EmiPersonalInformation emiPersonalInformation) {

        if (emiPersonalInformation != null && Integer.valueOf(3).equals(emiPersonalInformation.getStatus())) {
            // 随机生成卡号和开户银行
            emiPersonalInformation.setCardNumber(generateRandomCardNumber());
            emiPersonalInformation.setBank("Tallinn Bank");
        }
        return toAjax(iEmiPersonalInformationService.updateById(emiPersonalInformation) ? 1 : 0);
    }

    // 生成随机卡号的方法
    private String generateRandomCardNumber() {
        StringBuilder cardNumber = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            cardNumber.append((int) (Math.random() * 10));
        }
        return cardNumber.toString();
    }

    // 生成随机开户银行的方法（示例中使用常见的银行名称）
    private String generateRandomBank() {
        String[] bankKeys = {"emi.bank.icbc", "emi.bank.ccb", "emi.bank.boc", "emi.bank.abc", 
                             "emi.bank.cmb", "emi.bank.comm", "emi.bank.spdb", "emi.bank.cmbc"};
        int index = (int) (Math.random() * bankKeys.length);
        return MessageUtils.message(bankKeys[index]);
    }


    /**
     * 删除个人信息
     */

    @Log(title = "个人信息" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iEmiPersonalInformationService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
