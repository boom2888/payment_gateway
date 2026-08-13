package com.games.web.controller.emi;

import cn.hutool.core.util.ObjUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.games.common.annotation.Log;
import com.games.common.core.controller.BaseController;
import com.games.common.core.domain.AjaxResult;
import com.games.common.core.domain.entity.SysUser;
import com.games.common.core.domain.model.LoginUser;
import com.games.common.core.page.TableDataInfo;
import com.games.common.enums.BusinessType;
import com.games.common.enums.EmiProfileStatus;
import com.games.common.utils.MessageUtils;
import com.games.common.utils.ServletUtils;
import com.games.common.utils.http.HttpUtils;
import com.games.common.utils.poi.ExcelUtil;
import com.games.framework.web.service.TokenService;
import com.games.pay.vo.bindder.AccessTokenRes;
import com.games.pay.vo.bindder.AccessTokenVo;
import com.games.pay.vo.bindder.CreateWebhookVo;
import com.games.pay.vo.bindder.WebhookCreateResVo;
import com.games.payment.config.BinderrConfig;
import com.games.payment.domain.EmiEnterpriseInfo;
import com.games.payment.service.IEmiEnterpriseInfoService;
import com.games.payment.service.impl.EmiEnterpriseInfoServiceImpl;
import com.games.payment.vo.EmiAuditVo;
import com.games.system.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 企业基本信息及出口业务信息Controller
 * 
 * @author Ticker
 * @date 2025-10-16
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/info" )
public class EmiEnterpriseInfoController extends BaseController {

    private final IEmiEnterpriseInfoService iEmiEnterpriseInfoService;
    private final EmiEnterpriseInfoServiceImpl emiEnterpriseInfoServiceImpl;
    private final BinderrConfig binderrConfig;

    private final TokenService tokenService;

    @Autowired
    private SysUserMapper sysUserMapper;
    /**
     * 查询企业基本信息及出口业务信息列表
     */

    @GetMapping("/list")
    public TableDataInfo list(EmiEnterpriseInfo emiEnterpriseInfo) {
        startPage();
        List<EmiEnterpriseInfo> list = iEmiEnterpriseInfoService.selectAllList(emiEnterpriseInfo);
        return getDataTable(list);
    }

    /**
     * 导出企业基本信息及出口业务信息列表
     */

    @Log(title = "企业基本信息及出口业务信息" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(EmiEnterpriseInfo emiEnterpriseInfo) {
        List<EmiEnterpriseInfo> list = iEmiEnterpriseInfoService.queryList(emiEnterpriseInfo);
        ExcelUtil<EmiEnterpriseInfo> util = new ExcelUtil<EmiEnterpriseInfo>(EmiEnterpriseInfo.class);
        return util.exportExcel(list, "info" );
    }

    /**
     * 获取企业基本信息及出口业务信息详细信息
     */

    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iEmiEnterpriseInfoService.getById(id));
    }

    /**
     * 根据用户ID获取企业信息详细信息
     */
    @GetMapping("/user/{userId}")
    public AjaxResult getInfoByUserId(@PathVariable("userId") Long userId) {
        EmiEnterpriseInfo enterpriseInfo = iEmiEnterpriseInfoService.getByUserId(userId);

        enterpriseInfo.setAccount(sysUserMapper.selectUserById(userId).getUserName());
        if (enterpriseInfo == null) {
            return AjaxResult.error(MessageUtils.message("emi.enterprise.info.not.found"));
        }

        return AjaxResult.success(enterpriseInfo);
    }

    /**
     * 新增企业基本信息及出口业务信息
     */

    @Log(title = "企业基本信息及出口业务信息" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EmiEnterpriseInfo emiEnterpriseInfo) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        Long userId = user.getUserId();
        emiEnterpriseInfo.setUserId(userId);
        // 查询是否已有企业资料（按当前用户）
        EmiEnterpriseInfo existing = iEmiEnterpriseInfoService.getByUserId(userId);
        if (existing == null) {
            // 首次提交：新增
            emiEnterpriseInfo.setStatus(1);
            emiEnterpriseInfo.setEnabledStatus(0);
            return toAjax(iEmiEnterpriseInfoService.save(emiEnterpriseInfo) ? 1 : 0);
        } else {
            // 二次及以后提交：修改（保持原记录主键）
            emiEnterpriseInfo.setId(existing.getId());
            return toAjax(iEmiEnterpriseInfoService.updateById(emiEnterpriseInfo) ? 1 : 0);
        }
    }

    /**
     * 修改企业基本信息及出口业务信息
     */

    @Log(title = "企业基本信息及出口业务信息" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EmiEnterpriseInfo emiEnterpriseInfo) {

        if (emiEnterpriseInfo != null && emiEnterpriseInfo.getStatus() != null && emiEnterpriseInfo.getStatus() == 3) {
            // 随机生成卡号和开户银行
            emiEnterpriseInfo.setCardNumber(generateRandomCardNumber());
            emiEnterpriseInfo.setBank("Tallinn Bank");
        }
        return toAjax(iEmiEnterpriseInfoService.updateById(emiEnterpriseInfo) ? 1 : 0);
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
     * 删除企业基本信息及出口业务信息
     */

    @Log(title = "企业基本信息及出口业务信息" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iEmiEnterpriseInfoService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }

    /**
     * EMI企业审核
     */
    @PostMapping("/audit")
    @Log(title = "EMI企业审核", businessType = BusinessType.UPDATE)
    public AjaxResult audit(@Valid @RequestBody EmiAuditVo params) {
        // 验证审核参数
        // validateEmiAudit(params);
        
        EmiEnterpriseInfo emiInfo = iEmiEnterpriseInfoService.getById(params.getId());
        if (emiInfo == null) {
            return AjaxResult.error(MessageUtils.message("emi.enterprise.info.not.exist"));
        }
        
        try {
            if (params.getStatus() == 1) {  // 审核通过，创建 Binderr 实体
                // 获取 Binderr access token
                AccessTokenVo accessToken = new AccessTokenVo();
                accessToken.setClient_id(binderrConfig.getClientId())
                        .setClient_secret(binderrConfig.getClientSecret())
                        .setCode(params.getCode());
                        
                AccessTokenRes tokenRes = HttpUtils.sendPostJsonWithOkHttp(
                    binderrConfig.getTokenUrl(), accessToken, null, AccessTokenRes.class);
                    
                if (tokenRes == null || ObjUtil.isEmpty(tokenRes.getAccess_token())) {
                    throw new RuntimeException("Failed to obtain Binderr access token");
                }
                
                // 构建请求头
                String token = tokenRes.getAccess_token();
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                
                // 创建 Binderr 实体
                Long entityId = emiEnterpriseInfoServiceImpl.doCreateEntity(emiInfo, headers);
                
                // 缓存 token
                emiEnterpriseInfoServiceImpl.cacheBinderrToken(entityId, tokenRes);
                
                // 更新状态为等待 Binderr 审核 (2: 等待审核)
                emiInfo.setStatus(EmiProfileStatus.BINDERR_WAIT.getCode());
                emiInfo.setEntityId(entityId.intValue());
                
                // 注册 webhook
                CreateWebhookVo webhook = new CreateWebhookVo();
                webhook.setHookUrl(binderrConfig.getNotifyUrl());
                String url = binderrConfig.getSubscribeUrl();
                String webhookResponse = HttpUtils.sendPostJsonWithOkHttp(url, webhook, headers);
                
                // 解析 webhook 创建返回的 id
                ObjectMapper objectMapper = new ObjectMapper();
                WebhookCreateResVo webhookCreateRes = objectMapper.readValue(webhookResponse, WebhookCreateResVo.class);
                String webhookId = webhookCreateRes.getId();
                log.info("Webhook created for EMI enterprise with id: {}", webhookId);
                
                // 将 webhook id 保存到 Redis 中
                emiEnterpriseInfoServiceImpl.cacheWebhookId(entityId, webhookId);

            } else if (params.getStatus() == 2) {  // 审核拒绝
                emiInfo.setStatus(EmiProfileStatus.FAIL.getCode());
            } else {
                return AjaxResult.error(MessageUtils.message("emi.audit.status.unsupported"));
            }
            
            // 更新企业信息
            return toAjax(iEmiEnterpriseInfoService.updateById(emiInfo) ? 1 : 0);
            
        } catch (Exception e) {
            log.error("Failed to audit EMI enterprise", e);
            return AjaxResult.error(MessageUtils.message("emi.audit.failed", e.getMessage()));
        }
    }

    /**
     * 自定义验证逻辑
     */
    private void validateEmiAudit(EmiAuditVo params) {
        if (params.getStatus() == 1) {
            // 审核通过时，code 必填
            if (params.getCode() == null || params.getCode().trim().isEmpty()) {
                throw new IllegalArgumentException(MessageUtils.message("emi.audit.code.required"));
            }
        } else if (params.getStatus() == 2) {
            // 审核拒绝时，备注必填
            if (params.getRemark() == null || params.getRemark().trim().isEmpty()) {
                throw new IllegalArgumentException(MessageUtils.message("emi.audit.remark.required"));
            }
        }
    }
}
