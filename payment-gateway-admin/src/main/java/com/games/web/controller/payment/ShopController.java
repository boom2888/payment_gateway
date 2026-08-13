package com.games.web.controller.payment;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.games.common.annotation.Log;
import com.games.common.constant.Constants;
import com.games.common.constant.UserConstants;
import com.games.common.core.domain.AjaxResult;
import com.games.common.core.domain.entity.SysUser;
import com.games.common.core.domain.model.LoginUser;
import com.games.common.core.page.TableDataInfo;
import com.games.common.core.redis.RedisCache;
import com.games.common.enums.BusinessProfileStatus;
import com.games.common.enums.BusinessType;
import com.games.common.utils.SecurityUtils;
import com.games.common.utils.StringUtils;
import com.games.common.utils.MessageUtils;
import com.games.common.utils.ServletUtils;
import com.games.common.utils.http.HttpUtils;
import com.games.common.utils.poi.ExcelUtil;
import com.games.framework.web.service.TokenService;
import com.games.pay.dto.ShopSelfEditDto;
import com.games.pay.vo.ShopAuditVo;
import com.games.pay.vo.ShopEntity;
import com.games.pay.vo.bindder.*;
import com.games.payment.config.BinderrConfig;
import com.games.payment.domain.EmiEnterpriseInfo;
import com.games.payment.domain.EmiPersonalInformation;
import com.games.payment.domain.Shop;
import com.games.payment.domain.Ubo;
import com.games.payment.mapper.UboMapper;
import com.games.payment.service.IShopService;
import com.games.payment.service.impl.EmiEnterpriseInfoServiceImpl;
import com.games.payment.service.impl.EmiPersonalInformationServiceImpl;
import com.games.payment.service.impl.ShopServiceImpl;
import com.games.system.service.ISysUserService;
import com.games.system.mapper.SysUserRoleMapper;
import com.games.system.domain.SysUserRole;
import com.google.gson.Gson;
import com.stripe.exception.StripeException;
import com.stripe.model.v2.core.Account;
import com.stripe.param.v2.core.AccountCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import org.springframework.web.context.ContextLoader;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.*;
import java.util.Collections;

/**
 * 企业客户公司Controller
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/payment/businessCustomerCorporation")
public class ShopController extends BasePayController {

    private final IShopService shopService;
    private final ShopServiceImpl shopServiceImpl;
    private final TokenService tokenService;
    private final ISysUserService sysUserService;
    private final BinderrConfig binderrConfig;
    private final RedisCache redisCache;
    private final SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private UboMapper uboMapper;
    @Autowired
    private EmiPersonalInformationServiceImpl emiPersonalInformationService;

    @Autowired
    private EmiEnterpriseInfoServiceImpl emiEnterpriseInfoService;

    /**
     * 查询企业客户公司列表
     */
    @PreAuthorize("@ss.hasPermi('payment:businessCustomerCorporation:list')")
    @GetMapping("/list")
    public TableDataInfo list(Shop businessCustomerCorporation) {
        if (isShopRole()) {
            BusinessProfileStatus shopStatus = getShopStatus();
            if (!Objects.equals(shopStatus.getCode(), BusinessProfileStatus.OK.getCode())) {
                throw new RuntimeException("商户状态:" + shopStatus.getDesc() + ",暂无权限查询数据");
            }
            businessCustomerCorporation.setId(getSysLoginUser().getUser().getShopId());
        }
        startPage();
        List<ShopEntity> list = shopService.selectAllList(businessCustomerCorporation);
        return getDataTable(list);
    }

    /**
     * 查询下游商户列表
     */
    // todo
    // @PreAuthorize("@ss.hasPermi('payment:businessCustomerCorporation:list')")
    @GetMapping("/authList")
    public TableDataInfo authList(Shop shop) {
        startPage();
        shop.setId(getSysLoginUser().getUser().getShopId());
        List<ShopEntity> list = this.shopService.selectAuthList(shop);
        for (ShopEntity shopEntity : list) {
            // 根据用户ID查询 EMI 表，设置 userType
            try {
                // 先查询企业信息表
                EmiEnterpriseInfo enterpriseInfo = emiEnterpriseInfoService.getByUserId(shopEntity.getUserId());
                if (enterpriseInfo != null) {
                    // 如果在企业表中找到数据，设置 userType 为 2
                    shopEntity.setUserType(2);
                    shopEntity.setUserStatus(enterpriseInfo.getStatus());
                } else {
                    // 如果企业表没有，查询个人信息表
                    EmiPersonalInformation personalInfo = emiPersonalInformationService
                            .getByUserId(shopEntity.getUserId());
                    if (personalInfo != null) {
                        // 如果在个人表中找到数据，设置 userType 为 1
                        shopEntity.setUserType(1);
                        shopEntity.setUserStatus(personalInfo.getStatus());
                    } else {
                        // 如果两个表都没有数据，设置为 null 或 0
                        shopEntity.setUserType(null);
                        shopEntity.setUserStatus(null);

                    }
                }
            } catch (Exception e) {
                log.error("查询 EMI 信息失败: userId={}", shopEntity.getUserId(), e);
                // 查询失败时，userType 设置为 null
                shopEntity.setUserType(null);
            }
        }
        TableDataInfo dataTable = getDataTable(list);
        return dataTable;
    }

    /**
     * 商户认证编辑
     */
    @Log(title = "商户认证编辑", businessType = BusinessType.UPDATE)
    @PutMapping("/authEdit")
    public AjaxResult authEdit(@RequestBody Shop shop) {
        Shop dbShop = this.shopService.getById(shop.getId());
        BeanUtil.copyProperties(shop, dbShop);
        if (isShopRole()) {
            dbShop.setBusinessProfileStatus(BusinessProfileStatus.WAIT.getCode());
        }
        return toAjax(this.shopService.authEdit(dbShop) ? 1 : 0);
    }

    /**
     * 导出企业客户公司列表
     */
    @PreAuthorize("@ss.hasPermi('payment:businessCustomerCorporation:export')")
    @Log(title = "企业客户公司", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(Shop businessCustomerCorporation) {
        List<Shop> list = shopService.queryList(businessCustomerCorporation);
        ExcelUtil<Shop> util = new ExcelUtil<Shop>(Shop.class);
        return util.exportExcel(list, "businessCustomerCorporation");
    }

    /**
     * 获取企业客户公司详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:businessCustomerCorporation:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(shopService.getInfoById(id));
    }

    /**
     * 新增企业客户公司
     */
    @PreAuthorize("@ss.hasPermi('payment:businessCustomerCorporation:add')")
    @Log(title = "企业客户公司", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Shop businessCustomerCorporation) {
        SysUser sysUser = null; // 在外部声明，用于后续更新 shopId

        // 如果提供了账号信息，创建用户账号（用于二级商户）
        if (businessCustomerCorporation.getAccountName() != null
                && businessCustomerCorporation.getAccountPassword() != null) {
            // 检查用户名是否已存在
            if (UserConstants.NOT_UNIQUE
                    .equals(sysUserService.checkUserNameUnique(businessCustomerCorporation.getAccountName()))) {
                return AjaxResult.error("用户名已存在");
            }

            // 创建用户
            sysUser = new SysUser();
            sysUser.setUserName(businessCustomerCorporation.getAccountName());
            sysUser.setPassword(SecurityUtils.encryptPassword(businessCustomerCorporation.getAccountPassword()));
            sysUser.setNickName(businessCustomerCorporation.getAccountName());
            sysUser.setStatus("0"); // 正常状态
            sysUser.setDelFlag("0");
            sysUser.setType(1);

            // 插入用户
            int result = sysUserService.insertUser(sysUser);
            if (result > 0) {
                // 将用户ID设置到 shop
                businessCustomerCorporation.setUserId(sysUser.getUserId());

                // 为新建用户分配默认角色ID=5
                SysUserRole userRole = new SysUserRole();
                userRole.setUserId(sysUser.getUserId());
                userRole.setRoleId(5L);
                sysUserRoleMapper.batchUserRole(Collections.singletonList(userRole));

                // 如果是二级商户，设置父ID
                if (businessCustomerCorporation.getParentId() != null) {
                    // 验证父商户是否存在
                    Shop parentShop = shopService.getById(businessCustomerCorporation.getParentId());
                    if (parentShop == null) {
                        return AjaxResult.error("父商户不存在");
                    }
                    // 基于父商户复制生成新商户，排除指定字段与标识字段
                    String[] ignoreProps = new String[] {
                            // 明确不复制的字段
                            "businessName", "station", "directorProfileStatus", "directorDocumentStatus", "is3dsOn",
                            "settlementCurrency", "isWhitelistOn", "descriptor", "onRampMin", "onRampMax", "offRampMin",
                            "offRampMax", "parentId",
                            // 标识/系统字段不复制
                            "id", "userId", "entityId", "createdAt", "createdBy", "deletedAt", "deletedBy", "updateAt"
                    };
                    Shop newShop = new Shop();
                    // 先拷贝父商户到 newShop（忽略上述字段）
                    BeanUtil.copyProperties(parentShop, newShop,
                            CopyOptions.create().ignoreNullValue().setIgnoreProperties(ignoreProps));
                    // 再将前端传入字段覆盖到 newShop，确保请求优先生效
                    BeanUtil.copyProperties(businessCustomerCorporation, newShop,
                            CopyOptions.create().ignoreNullValue());
                    // 覆盖关键字段：父ID、用户ID（若已创建）、初始化资料状态
                    newShop.setParentId(businessCustomerCorporation.getParentId());
                    newShop.setUserId(sysUser.getUserId());
                    newShop.setBusinessProfileStatus(1);
                    // 用 newShop 替换原对象，后续统一保存该对象
                    businessCustomerCorporation = newShop;
                }
            } else {
                return AjaxResult.error("创建用户失败");
            }
        }

        // 保存 shop，MyBatis-Plus 会通过 useGeneratedKeys 自动将生成的 ID 填充到
        // businessCustomerCorporation.getId()
        boolean saveResult = shopService.save(businessCustomerCorporation);
        if (saveResult && sysUser != null) {
            // 将新创建的 shop 的 ID 设置到用户的 shopId 字段
            sysUser.setShopId(businessCustomerCorporation.getId());
            sysUserService.updateUserProfile(sysUser);
        }

        // 新增逻辑：如为二级商户（复制新对象），复制父商户对应的 credit_ubo 数据
        if (saveResult && businessCustomerCorporation.getParentId() != null) {
            // 直接注入 mapper

            List<Ubo> parentUbos = uboMapper.selectByShopId(businessCustomerCorporation.getParentId());
            if (parentUbos != null && !parentUbos.isEmpty()) {
                List<Ubo> copyUbos = new ArrayList<>();
                for (Ubo parentUbo : parentUbos) {
                    Ubo newUbo = new Ubo();
                    BeanUtils.copyProperties(parentUbo, newUbo);
                    newUbo.setShopId(businessCustomerCorporation.getId()); // 设为新二级商户ID
                    copyUbos.add(newUbo);
                }
                uboMapper.batchInsert(businessCustomerCorporation.getId(), copyUbos);
            }
        }

        return toAjax(saveResult ? 1 : 0);
    }

    /**
     * 修改企业客户公司
     */
    @PreAuthorize("@ss.hasPermi('payment:businessCustomerCorporation:edit')")
    @Log(title = "企业客户公司", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Shop shop) {
        // 只有当 userId 和 status 都不为空时，才需要更新用户状态和缓存
        if (shop.getUserId() != null && shop.getStatus() != null) {
            SysUser sysUser = sysUserService.selectUserById(shop.getUserId());
            if (sysUser != null) {
                String newStatus = String.valueOf(shop.getStatus());
                // 只有当状态发生变化时，才更新数据库和缓存
                if (!newStatus.equals(sysUser.getStatus())) {
                    sysUser.setStatus(newStatus);
                    sysUserService.updateUserStatus(sysUser);

                    // 更新缓存中的用户状态 - 遍历所有登录用户查找需要更新的用户
                    Collection<String> keys = redisCache.keys(Constants.LOGIN_TOKEN_KEY + "*");
                    for (String key : keys) {
                        LoginUser loginUser = redisCache.getCacheObject(key);
                        if (loginUser != null && loginUser.getUser() != null &&
                                loginUser.getUser().getUserId().equals(sysUser.getUserId())) {
                            loginUser.getUser().setStatus(sysUser.getStatus());
                            // 使用TokenService的refreshToken方法确保正确更新缓存
                            tokenService.refreshToken(loginUser);
                            break; // 找到后退出循环，一个用户通常只有一个活跃 token
                        }
                    }
                }
            }
        }

        return toAjax(shopService.updateById(shop) ? 1 : 0);
    }

    /**
     * 删除企业客户公司
     */
    @PreAuthorize("@ss.hasPermi('payment:businessCustomerCorporation:remove')")
    @Log(title = "企业客户公司", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        // 删除前，按 shopId 查询并删除关联用户数据
        List<Long> userIdList = new ArrayList<>();
        if (ids != null && ids.length > 0) {
            for (Long shopId : ids) {
                if (shopId == null) {
                    continue;
                }
                SysUser query = new SysUser();
                query.setShopId(shopId);
                List<SysUser> users = sysUserService.selectUserList(query);
                if (users != null && !users.isEmpty()) {
                    for (SysUser u : users) {
                        if (u != null && u.getUserId() != null) {
                            userIdList.add(u.getUserId());
                        }
                    }
                }
            }
        }
        if (!userIdList.isEmpty()) {
            sysUserService.deleteUserByIds(userIdList.toArray(new Long[0]));
        }
        return toAjax(shopService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }

    @PreAuthorize("@ss.hasPermi('payment:shopConfig:list')")
    @GetMapping("/shopConfig")
    public AjaxResult shopConfig() {
        // 商户查看自己的 admin查看商户id=1的
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();

        Long shopId = null;
        if (SysUser.isAdmin(user.getUserId())) { // 管理员 查看商户id=1的信息
            shopId = 1L;
        } else {
            // 当前用户的商户id不为空 返回该商户信息
            if (ObjUtil.isNotEmpty(user.getShopId())) {
                shopId = user.getShopId();
            }
        }

        startPage();
        Shop shop = this.shopService.getById(shopId);
        if (shop != null) {
            shop.setUserName(user.getUserName());
        }
        return AjaxResult.success(shop);
    }

    @PreAuthorize("@ss.hasPermi('payment:shopConfig:selfEdit')")
    @PutMapping("/selfEdit")
    public AjaxResult updateShopConfig(@RequestBody ShopSelfEditDto params) {
        Shop shop = BeanUtil.copyProperties(params, Shop.class);
        return this.shopService.updateById(shop) ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 获取国家列表
     */
    @GetMapping("/countryList")
    public AjaxResult getCountryList() {
        List<Map<String, String>> countryList = new ArrayList<>();

        // 获取所有可用的国家代码
        String[] countryCodes = Locale.getISOCountries();

        for (String countryCode : countryCodes) {
            Locale locale = new Locale("", countryCode);
            String countryName = locale.getDisplayCountry(Locale.ENGLISH);

            Map<String, String> country = new HashMap<>();
            country.put("code", countryCode);
            country.put("name", countryName);

            countryList.add(country);
        }

        // 按英文名称排序
        countryList.sort(Comparator.comparing(a -> a.get("name")));

        return AjaxResult.success(countryList);
    }

    /**
     * 商户审核
     */
    @PostMapping("/audit")
    public AjaxResult audit(@Valid @RequestBody ShopAuditVo params) {
        // 自定义业务验证逻辑
        validateShopAudit(params);
        Shop shop = BeanUtil.copyProperties(params, Shop.class);
        try {
            if (Objects.equals(BusinessProfileStatus.OK.getCode(), params.getBusinessProfileStatus())) {
                // 创建 binderr 实体
                Shop newShop = shopService.getInfoById(shop.getId());
                // 将传入的 Bearer Token 透传给 doCreateEntity 使用
                AccessTokenVo accessToken = new AccessTokenVo();
                accessToken.setClient_id(binderrConfig.getClientId())
                        .setClient_secret(binderrConfig.getClientSecret())
                        .setCode(params.getCode());
                AccessTokenRes tokenRes = HttpUtils.sendPostJsonWithOkHttp(binderrConfig.getTokenUrl(), accessToken,
                        null, AccessTokenRes.class);
                if (tokenRes == null || ObjUtil.isEmpty(tokenRes.getAccess_token())) {
                    throw new RuntimeException("Failed to obtain Binderr access token");
                }
                String token = tokenRes.getAccess_token();
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                Long entityId = shopServiceImpl.doCreateEntity(newShop, headers);
                shopServiceImpl.cacheBinderrToken(entityId, tokenRes);
                // 修改为待 Binderr 审核
                shop.setBusinessProfileStatus(BusinessProfileStatus.BINDERR_WAIT.getCode());
                shop.setEntityId(entityId);
                // 注册 webhook
                CreateWebhookVo webhook = new CreateWebhookVo();
                webhook.setHookUrl(binderrConfig.getNotifyUrl());
                String url = binderrConfig.getSubscribeUrl();
                String webhookResponse = HttpUtils.sendPostJsonWithOkHttp(url, webhook, headers);
                // 解析 webhook 创建返回的 id
                ObjectMapper objectMapper = new ObjectMapper();
                WebhookCreateResVo webhookCreateRes = objectMapper.readValue(webhookResponse, WebhookCreateResVo.class);
                String webhookId = webhookCreateRes.getId();
                log.info("Webhook created with id: {}", webhookId);
                // 将 webhook id 保存到 Redis 中
                shopServiceImpl.cacheWebhookId(entityId, webhookId);
                // 执行 screening TODO 暂时人工进行审核
                // RunScreeningVo runScreening = new RunScreeningVo();
                // runScreening.setOngoingMonitoring(true);
                // HttpUtils.sendPostJsonWithOkHttp(StrUtil.format(binderrConfig.getScreeningUrl()
                // + entityId), runScreening, headers);
            } else if (!Objects.equals(BusinessProfileStatus.FAIL.getCode(), params.getBusinessProfileStatus())) {
                return AjaxResult.error("Review is not supported in the current status.");
            }
        } catch (Exception e) {
            log.error("Failed to create entity", e);
            return AjaxResult.error();
        }
        return shopService.updateById(shop) ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 自定义验证逻辑
     */
    private void validateShopAudit(ShopAuditVo params) {
        if (params.getBusinessProfileStatus() == 1) {
            // businessProfileStatus为1时，除了remark其他字段都是必填
            if (params.getOnRampMin() == null) {
                throw new IllegalArgumentException(MessageUtils.message("shop.deposit.min.amount.empty"));
            }
            if (params.getOnRampMin().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("入金最小金额必须大于0");
            }
            if (params.getOnRampMax() == null) {
                throw new IllegalArgumentException(MessageUtils.message("shop.deposit.max.amount.empty"));
            }
            if (params.getOnRampMax().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("入金最大金额必须大于0");
            }
            if (params.getOffRampMin() == null) {
                throw new IllegalArgumentException(MessageUtils.message("shop.withdraw.min.amount.empty"));
            }
            if (params.getOffRampMin().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("出金最小金额必须大于0");
            }
            if (params.getOffRampMax() == null) {
                throw new IllegalArgumentException(MessageUtils.message("shop.withdraw.max.amount.empty"));
            }
            if (params.getOffRampMax().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("出金最大金额必须大于0");
            }
            if (params.getMerchantFeeModelId() == null) {
                throw new IllegalArgumentException(MessageUtils.message("shop.fee.model.empty"));
            }
            if (params.getSettlementCurrency() == null) {
                throw new IllegalArgumentException(MessageUtils.message("shop.settlement.currency.empty"));
            }
            if (params.getIs3dsOn() == null) {
                throw new IllegalArgumentException("3ds开关不能为空");
            }
        } else if (params.getBusinessProfileStatus() == 2) {
            // businessProfileStatus为2时，只有id和remark是必填
            if (params.getRemark() == null || params.getRemark().trim().isEmpty()) {
                throw new IllegalArgumentException("审核拒绝时，原因不能为空");
            }
        }
    }

    @PreAuthorize("@ss.hasPermi('payment:shop:resetPassword')")
    @PostMapping("/resetPassword/{userId}")
    public AjaxResult resetPassword(@PathVariable Long userId) {
        SysUser sysUser = new SysUser(userId);
        sysUser.setPassword(SecurityUtils.encryptPassword(Constants.DEFAULT_SHOP_PASSWORD));
        return this.sysUserService.updateUserProfile(sysUser) > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 接收 webhook 推送结果
     *
     * @param res webhook 推送的数据
     * @return 处理结果
     */
    @PostMapping("/webhook")
    public AjaxResult receiveWebhook(@RequestHeader(value = "X-Webhook-Secret", required = false) String webhookSecret,
                                     @RequestBody String res) {
        try {
            String expectedSecret = binderrConfig.getWebhookSecret();
            if (StringUtils.isBlank(expectedSecret) || StringUtils.isBlank(webhookSecret)
                    || !MessageDigest.isEqual(expectedSecret.getBytes(StandardCharsets.UTF_8),
                    webhookSecret.getBytes(StandardCharsets.UTF_8))) {
                return AjaxResult.error("Invalid webhook authentication");
            }

            // 处理 webhook 数据
            shopServiceImpl.processWebhookData(res);

            log.info("Webhook data processed successfully");
            return AjaxResult.success("Webhook received and processed successfully");

        } catch (Exception e) {
            log.error("Failed to process webhook data", e);
            return AjaxResult.error("Failed to process webhook: " + e.getMessage());
        }
    }

    /**
     * 查询 businessProfileStatus 为 3 的商户列表
     */
    @GetMapping("/listByBusinessProfileStatusThree")
    public TableDataInfo listByBusinessProfileStatusThree() {
        List<Shop> list = shopService.listByBusinessProfileStatusThree();
        return getDataTable(list);
    }


//    @PreAuthorize("@ss.hasPermi('payment:shopConfig:commitStripe')")
//    @PutMapping("/commitStripe")
//    public AjaxResult commitStripe() {
//        try {
//            // Create v2 Account
//            AccountCreateParams params =
//                    AccountCreateParams.builder()
//                            .setContactEmail(email)
//                            .setDisplayName(email)
//                            .setIdentity(
//                                    AccountCreateParams.Identity.builder()
//                                            .setCountry("US")
//                                            .setEntityType(AccountCreateParams.Identity.EntityType.COMPANY)
//                                            .build()
//                            )
//                            .setConfiguration(
//                                    AccountCreateParams.Configuration.builder()
//                                            .setMerchant(
//                                                    AccountCreateParams.Configuration.Merchant.builder()
//                                                            .setCapabilities(
//                                                                    AccountCreateParams.Configuration.Merchant.Capabilities.builder()
//                                                                            .setCardPayments(
//                                                                                    AccountCreateParams.Configuration.Merchant.Capabilities.CardPayments.builder()
//                                                                                            .setRequested(true)
//                                                                                            .build()
//                                                                            )
//                                                                            .build()
//                                                            )
//                                                            .build()
//                                            )
//                                            .build()
//                            )
//                            .setDefaults(
//                                    AccountCreateParams.Defaults.builder()
//                                            .setResponsibilities(
//                                                    AccountCreateParams.Defaults.Responsibilities.builder()
//                                                            .setFeesCollector(AccountCreateParams.Defaults.Responsibilities.FeesCollector.STRIPE)
//                                                            .setLossesCollector(AccountCreateParams.Defaults.Responsibilities.LossesCollector.STRIPE)
//                                                            .build()
//                                            )
//                                            .build()
//                            )
//                            .setDashboard(AccountCreateParams.Dashboard.FULL)
//                            .build();
//
//            Account account = v2Client.v2().core().accounts().create(params);
//            return new Gson().toJson(Map.of("accountId", account.getId()));
//        } catch (StripeException e) {
//            response.status(500);
//            return new Gson().toJson(Map.of("error", e.getMessage()));
//        }
//
//    }

}
