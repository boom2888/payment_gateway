package com.games.payment.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.games.common.core.redis.RedisCache;
import com.games.common.enums.BusinessProfileStatus;
import com.games.common.enums.YesNo;
import com.games.common.utils.SignUtil;
import com.games.common.utils.http.HttpUtils;
import com.games.pay.dto.CreateEntityDto;
import com.games.pay.dto.CreateEntityResDto;
import com.games.pay.enums.BinderrEntityStatusEnums;
import com.games.pay.enums.EntityRelationshipTypeEnums;
import com.games.pay.vo.ShopEntity;
import com.games.pay.vo.bindder.*;
import com.games.payment.config.BinderrConfig;
import com.games.payment.domain.ConfigCountry;
import com.games.payment.domain.Order;
import com.games.payment.domain.Shop;
import com.games.payment.domain.WhitelistedCreditCards;
import com.games.payment.enums.WhiteListOnType;
import com.games.payment.mapper.OrderMapper;
import com.games.payment.mapper.ShopMapper;
import com.games.payment.mapper.UboMapper;
import com.games.payment.service.IConfigCountryService;
import com.games.payment.service.IShopService;
import com.games.payment.service.IWhitelistedCreditCardsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * 企业客户公司Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Slf4j
@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements IShopService {
    @Autowired
    private UboMapper uboMapper;
    @Resource
    private ShopMapper shopMapper;
    @Autowired
    private BinderrConfig binderrConfig;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private IConfigCountryService configCountryService;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private IWhitelistedCreditCardsService whitelistedCreditCardsService;
    @Resource
    private OrderMapper orderMapper;

    private static final long TOKEN_REFRESH_BUFFER_MILLIS = TimeUnit.MINUTES.toMillis(1);
    private static final String BINDERR_TOKEN_KEY_PREFIX = "binderr:token:";
    private static final String BINDERR_WEBHOOK_KEY_PREFIX = "binderr:webhook:";
    private static final String BINDERR_WEBHOOK_PROCESSED_KEY_PREFIX = "binderr:webhook:processed:";
    /**
     * 查询企业客户公司列表
     *
     * @param businessCustomerCorporation 企业客户公司
     * @return 企业客户公司
     */
    @Override
    public List<ShopEntity> selectAllList(Shop businessCustomerCorporation)
    {
        return getBaseMapper().selectAllList(businessCustomerCorporation);
    }

    /**
     * 获取正在跑的商户
     * @return
     */
    @Override
    public List<Shop> getRuningShops() {
        LambdaQueryWrapper<Shop> lqw = Wrappers.lambdaQuery();
        lqw.eq(Shop::getBusinessProfileStatus, BusinessProfileStatus.OK.getCode());
//        Shop shop = new Shop();
//        shop.setBusinessProfileStatus(BusinessProfileStatus.OK.getCode());
//        shop.setStatus(1);
//        return this.selectAllList(shop);
        return list(lqw);
    }


    @Override
    public List<Shop> queryList(Shop businessCustomerCorporation) {
        LambdaQueryWrapper<Shop> lqw = Wrappers.lambdaQuery();
        if (businessCustomerCorporation.getUserId() != null){
            lqw.eq(Shop::getUserId ,businessCustomerCorporation.getUserId());
        }
        if (StringUtils.isNotBlank(businessCustomerCorporation.getBusinessName())){
            lqw.like(Shop::getBusinessName ,businessCustomerCorporation.getBusinessName());
        }
        if (StringUtils.isNotBlank(businessCustomerCorporation.getBusinessNo())){
            lqw.eq(Shop::getBusinessNo ,businessCustomerCorporation.getBusinessNo());
        }
        if (businessCustomerCorporation.getBusinessAddress() != null){
            lqw.eq(Shop::getBusinessAddress ,businessCustomerCorporation.getBusinessAddress());
        }
        if (StringUtils.isNotBlank(businessCustomerCorporation.getCountry())){
            lqw.eq(Shop::getCountry ,businessCustomerCorporation.getCountry());
        }
        if (StringUtils.isNotBlank(businessCustomerCorporation.getIndustry())){
            lqw.eq(Shop::getIndustry ,businessCustomerCorporation.getIndustry());
        }
        if (StringUtils.isNotBlank(businessCustomerCorporation.getSideIndustry())){
            lqw.eq(Shop::getSideIndustry ,businessCustomerCorporation.getSideIndustry());
        }
        if (businessCustomerCorporation.getRegistrationDate() != null){
            lqw.eq(Shop::getRegistrationDate ,businessCustomerCorporation.getRegistrationDate());
        }
        if (businessCustomerCorporation.getRegistrationAddress() != null){
            lqw.eq(Shop::getRegistrationAddress ,businessCustomerCorporation.getRegistrationAddress());
        }
        if (StringUtils.isNotBlank(businessCustomerCorporation.getShareholdingStructure())){
            lqw.eq(Shop::getShareholdingStructure ,businessCustomerCorporation.getShareholdingStructure());
        }
        if (StringUtils.isNotBlank(businessCustomerCorporation.getTurnoverYear())){
            lqw.eq(Shop::getTurnoverYear ,businessCustomerCorporation.getTurnoverYear());
        }
        if (StringUtils.isNotBlank(businessCustomerCorporation.getTurnoverWeek())){
            lqw.eq(Shop::getTurnoverWeek ,businessCustomerCorporation.getTurnoverWeek());
        }
        if (StringUtils.isNotBlank(businessCustomerCorporation.getStation())){
            lqw.eq(Shop::getStation ,businessCustomerCorporation.getStation());
        }
        if (StringUtils.isNotBlank(businessCustomerCorporation.getWebsite())){
            lqw.eq(Shop::getWebsite ,businessCustomerCorporation.getWebsite());
        }
        if (StringUtils.isNotBlank(businessCustomerCorporation.getSupplierName())){
            lqw.like(Shop::getSupplierName ,businessCustomerCorporation.getSupplierName());
        }
        if (StringUtils.isNotBlank(businessCustomerCorporation.getSupplierContracts())){
            lqw.eq(Shop::getSupplierContracts ,businessCustomerCorporation.getSupplierContracts());
        }
        if (businessCustomerCorporation.getBusinessProfileStatus() != null){
            lqw.eq(Shop::getBusinessProfileStatus ,businessCustomerCorporation.getBusinessProfileStatus());
        }
        if (StringUtils.isNotBlank(businessCustomerCorporation.getDirectorProfileStatus())){
            lqw.eq(Shop::getDirectorProfileStatus ,businessCustomerCorporation.getDirectorProfileStatus());
        }
        if (StringUtils.isNotBlank(businessCustomerCorporation.getDirectorDocumentStatus())){
            lqw.eq(Shop::getDirectorDocumentStatus ,businessCustomerCorporation.getDirectorDocumentStatus());
        }
        if (businessCustomerCorporation.getSaasUserCorporationId() != null){
            lqw.eq(Shop::getSaasUserCorporationId ,businessCustomerCorporation.getSaasUserCorporationId());
        }
        if (businessCustomerCorporation.getCreatedAt() != null){
            lqw.eq(Shop::getCreatedAt ,businessCustomerCorporation.getCreatedAt());
        }
        if (businessCustomerCorporation.getCreatedBy() != null){
            lqw.eq(Shop::getCreatedBy ,businessCustomerCorporation.getCreatedBy());
        }
        if (businessCustomerCorporation.getDeletedAt() != null){
            lqw.eq(Shop::getDeletedAt ,businessCustomerCorporation.getDeletedAt());
        }
        if (businessCustomerCorporation.getDeletedBy() != null){
            lqw.eq(Shop::getDeletedBy ,businessCustomerCorporation.getDeletedBy());
        }
        if (businessCustomerCorporation.getDeleted() != null){
            lqw.eq(Shop::getDeleted ,businessCustomerCorporation.getDeleted());
        }
        if (StringUtils.isNotBlank(businessCustomerCorporation.getBusinessDesc())){
            lqw.eq(Shop::getBusinessDesc ,businessCustomerCorporation.getBusinessDesc());
        }
        if (StringUtils.isNotBlank(businessCustomerCorporation.getMid())){
            lqw.eq(Shop::getMid ,businessCustomerCorporation.getMid());
        }
        if (StringUtils.isNotBlank(businessCustomerCorporation.getRequestCryptoTransactionEndpoint())){
            lqw.eq(Shop::getRequestCryptoTransactionEndpoint ,businessCustomerCorporation.getRequestCryptoTransactionEndpoint());
        }
        if (StringUtils.isNotBlank(businessCustomerCorporation.getMerchantHashSecretKey())){
            lqw.eq(Shop::getMerchantHashSecretKey ,businessCustomerCorporation.getMerchantHashSecretKey());
        }
        if (StringUtils.isNotBlank(businessCustomerCorporation.getNotifyOrderStatusEndpoint())){
            lqw.eq(Shop::getNotifyOrderStatusEndpoint ,businessCustomerCorporation.getNotifyOrderStatusEndpoint());
        }
        if (businessCustomerCorporation.getIsSiftOn() != null){
            lqw.eq(Shop::getIsSiftOn ,businessCustomerCorporation.getIsSiftOn());
        }
        if (businessCustomerCorporation.getIs3dsOn() != null){
            lqw.eq(Shop::getIs3dsOn ,businessCustomerCorporation.getIs3dsOn());
        }
        if (businessCustomerCorporation.getCryptoDeliveryMethod() != null){
            lqw.eq(Shop::getCryptoDeliveryMethod ,businessCustomerCorporation.getCryptoDeliveryMethod());
        }
        if (businessCustomerCorporation.getIsHouseMerchant() != null){
            lqw.eq(Shop::getIsHouseMerchant ,businessCustomerCorporation.getIsHouseMerchant());
        }
        if (StringUtils.isNotBlank(businessCustomerCorporation.getNotifyOrderStatusEndpoint2())){
            lqw.eq(Shop::getNotifyOrderStatusEndpoint2 ,businessCustomerCorporation.getNotifyOrderStatusEndpoint2());
        }
        if (businessCustomerCorporation.getReservePercentage() != null){
            lqw.eq(Shop::getReservePercentage ,businessCustomerCorporation.getReservePercentage());
        }
        if (businessCustomerCorporation.getReserveHoldingPeriod() != null){
            lqw.eq(Shop::getReserveHoldingPeriod ,businessCustomerCorporation.getReserveHoldingPeriod());
        }
        if (businessCustomerCorporation.getSettlementCurrency() != null){
            lqw.eq(Shop::getSettlementCurrency ,businessCustomerCorporation.getSettlementCurrency());
        }
        if (StringUtils.isNotBlank(businessCustomerCorporation.getProfitSharing())){
            lqw.eq(Shop::getProfitSharing ,businessCustomerCorporation.getProfitSharing());
        }
        if (businessCustomerCorporation.getIsWhitelistOn() != null){
            lqw.eq(Shop::getIsWhitelistOn ,businessCustomerCorporation.getIsWhitelistOn());
        }
        if (StringUtils.isNotBlank(businessCustomerCorporation.getNotifyOrderStatusEndpoint3())){
            lqw.eq(Shop::getNotifyOrderStatusEndpoint3 ,businessCustomerCorporation.getNotifyOrderStatusEndpoint3());
        }
        if (businessCustomerCorporation.getAniCheck() != null){
            lqw.eq(Shop::getAniCheck ,businessCustomerCorporation.getAniCheck());
        }
        if (businessCustomerCorporation.getAcquirerId() != null){
            lqw.eq(Shop::getAcquirerId ,businessCustomerCorporation.getAcquirerId());
        }
        if (StringUtils.isNotBlank(businessCustomerCorporation.getCryptoDeliveryDelayTime())){
            lqw.eq(Shop::getCryptoDeliveryDelayTime ,businessCustomerCorporation.getCryptoDeliveryDelayTime());
        }
        if (StringUtils.isNotBlank(businessCustomerCorporation.getOrderStatusChangeNotification())){
            lqw.eq(Shop::getOrderStatusChangeNotification ,businessCustomerCorporation.getOrderStatusChangeNotification());
        }
        if (StringUtils.isNotBlank(businessCustomerCorporation.getThreeDSuccessUrl())){
            lqw.eq(Shop::getThreeDSuccessUrl ,businessCustomerCorporation.getThreeDSuccessUrl());
        }
        if (StringUtils.isNotBlank(businessCustomerCorporation.getThreeDFailureUrl())){
            lqw.eq(Shop::getThreeDFailureUrl ,businessCustomerCorporation.getThreeDFailureUrl());
        }
        if (StringUtils.isNotBlank(businessCustomerCorporation.getDefaultRedirectSuccessUrl())){
            lqw.eq(Shop::getDefaultRedirectSuccessUrl ,businessCustomerCorporation.getDefaultRedirectSuccessUrl());
        }
        if (StringUtils.isNotBlank(businessCustomerCorporation.getDefaultRedirectFailureUrl())){
            lqw.eq(Shop::getDefaultRedirectFailureUrl ,businessCustomerCorporation.getDefaultRedirectFailureUrl());
        }
        if (businessCustomerCorporation.getOnRampMin() != null){
            lqw.eq(Shop::getOnRampMin ,businessCustomerCorporation.getOnRampMin());
        }
        if (businessCustomerCorporation.getOnRampMax() != null){
            lqw.eq(Shop::getOnRampMax ,businessCustomerCorporation.getOnRampMax());
        }
        if (businessCustomerCorporation.getOffRampMin() != null){
            lqw.eq(Shop::getOffRampMin ,businessCustomerCorporation.getOffRampMin());
        }
        if (businessCustomerCorporation.getOffRampMax() != null){
            lqw.eq(Shop::getOffRampMax ,businessCustomerCorporation.getOffRampMax());
        }
        return this.list(lqw);
    }

    @Override
    public List<ShopEntity> selectAuthList(Shop reqShop) {
        List<ShopEntity> list = getBaseMapper().selectListWithUser(reqShop);
        if(CollUtil.isNotEmpty(list)){
            for (ShopEntity shop : list) {
                shop.setUbos(uboMapper.selectByShopId(shop.getId()));
            }
        }
        return list;
    }

    @Override
    public boolean authEdit(Shop shop) {
        this.baseMapper.updateById(shop);
        if(CollUtil.isNotEmpty(shop.getUbos())){
            uboMapper.deleteByShopId(shop.getId());
            uboMapper.batchInsert(shop.getId(), shop.getUbos());
        }

        return true;
    }

    @Override
    public List<Shop> getApiBackOpenShops() {
        LambdaQueryWrapper<Shop> lqw = Wrappers.lambdaQuery();
        lqw.eq(Shop::getBackOpen, YesNo.YES.name());
        lqw.eq(Shop::getBusinessProfileStatus, BusinessProfileStatus.OK.getCode());
        lqw.isNotNull(Shop::getNotifyOrderStatusEndpoint).ne(Shop::getNotifyOrderStatusEndpoint, ""); // 过滤掉空字符串
        lqw.orderByAsc(Shop::getUpdateAt);
        return list(lqw);
    }

    @Override
    public ShopEntity getInfoById(Long id) {
        return shopMapper.getInfoById(id);
    }

    @Override
    public Shop getByEntityId(Long entityId) {
        if (entityId == null) {
            return null;
        }
        LambdaQueryWrapper<Shop> lqw = Wrappers.<Shop>lambdaQuery()
                .eq(Shop::getEntityId, entityId)
                .last("limit 1");
        return this.getOne(lqw);
    }

    @Override
    public boolean updateBusinessProfileStatusByEntityId(Long entityId, Integer businessProfileStatus) {
        if (entityId == null) {
            return false;
        }
        LambdaQueryWrapper<Shop> lqw = Wrappers.<Shop>lambdaQuery()
                .eq(Shop::getEntityId, entityId)
                .last("limit 1");
        Shop shop = this.getOne(lqw);
        if (shop == null) {
            return false;
        }
        shop.setBusinessProfileStatus(businessProfileStatus);
        return this.updateById(shop);
    }

    @Override
    public List<Shop> listByBusinessProfileStatusThree() {
        LambdaQueryWrapper<Shop> lqw = Wrappers.lambdaQuery();
        lqw.eq(Shop::getBusinessProfileStatus, 1);
        return list(lqw);
    }


    /**
     * 调用外部 API 创建实体
     *
     * @param shop 商户信息
     * @param headers 请求头
     * @return 实体ID
     */
    public Long doCreateEntity(Shop shop, Map<String, String> headers) {
        try {
            CreateEntityDto dto = new CreateEntityDto();
            RelationshipVo relationshipVo = new RelationshipVo();
            relationshipVo.setType(EntityRelationshipTypeEnums.CLIENT.name());
            StatusVo statusVo = new StatusVo();
            statusVo.setId(binderrConfig.getStatusId());
            relationshipVo.setStatus(statusVo);
            dto.setRelationship(relationshipVo);

            List<ManagedByVo> managedByList = new ArrayList<>();
            ManagedByVo managedByVo = new ManagedByVo();
            managedByVo.setId(binderrConfig.getManagedById());
            managedByVo.setName(binderrConfig.getManagedByName());
            managedByList.add(managedByVo);
            dto.setManagedBy(managedByList);

            LegalEntityVo legalEntityVo = new LegalEntityVo();
            String country = shop.getCountry();
            String threeLetterCountryCode = convertToThreeLetterCountryCode(country);
            legalEntityVo.setIncorporationCountryCode(threeLetterCountryCode);
            TypeVo typeVo = new TypeVo();
            typeVo.setId(1);
            legalEntityVo.setType(typeVo);
            legalEntityVo.setRegistrationNumber(shop.getBusinessNo());
            legalEntityVo.setName(shop.getBusinessName());
            dto.setLegalEntity(legalEntityVo);

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonBody = objectMapper.writeValueAsString(dto);
            String apiUrl = binderrConfig.getCreateEntityUrl();
            log.info("Calling create entity API: {}", apiUrl);
            log.info("Creating Binderr entity");
            String response = HttpUtils.sendPostJsonWithOkHttp(apiUrl, jsonBody, headers);
            CreateEntityResDto resDto = objectMapper.readValue(response, CreateEntityResDto.class);
            log.info("API response: {}", response);
            return resDto.getId();
        } catch (Exception e) {
            log.error("Failed to call create entity API", e);
            throw new RuntimeException("Failed to call external API: " + e.getMessage(), e);
        }
    }

    /**
     * 将2位数国家代码转换为3位数国家代码
     *
     * @param twoLetterCode 2位数国家代码
     * @return 3位数国家代码
     */
    public String convertToThreeLetterCountryCode(String twoLetterCode) {
        if (twoLetterCode == null || twoLetterCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Country code cannot be null or empty");
        }
        String upper = twoLetterCode.trim().toUpperCase();
        ConfigCountry configCountry = configCountryService.getById(upper);
        if (configCountry != null && configCountry.getIso3() != null && !configCountry.getIso3().trim().isEmpty()) {
            String code = configCountry.getIso3().trim();
            log.info("Country code conversion (config): {} -> {}", upper, code);
            return code;
        }
        try {
            java.util.Locale locale = new java.util.Locale("", upper);
            String iso3 = locale.getISO3Country();
            if (!iso3.trim().isEmpty()) {
                log.info("Country code conversion (fallback): {} -> {}", upper, iso3);
                return iso3;
            }
        } catch (Exception ignore) {
        }
        throw new IllegalArgumentException("Country code not found: " + twoLetterCode);
    }

    /**
     * 处理 webhook 数据
     *
     * @param webhookResponse webhook 数据
     */
    public void processWebhookData(String webhookResponse) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            WebhookResVo payload = mapper.readValue(webhookResponse, WebhookResVo.class);
            Long entityId = payload.getEntityId();
            String status = payload.getStatus();
            log.info("Webhook parsed => entityId: {}, status: {}, ongoingMonitoring: {}, pending: {}, screeningHasRun: {}",
                    entityId, status, payload.getOngoingMonitoring(), payload.getPending(), payload.getScreeningHasRun());
            if (entityId == null) {
                log.warn("Webhook payload missing entityId, skipping processing");
                return;
            }
            Shop existShop = getByEntityId(entityId);
            if (existShop == null) {
                log.warn("No shop found by entityId: {}", entityId);
                try {
                    Object emiSvc = applicationContext.getBean("emiEnterpriseInfoServiceImpl");
                    java.lang.reflect.Method method = emiSvc.getClass().getMethod("processWebhookData", String.class);
                    method.invoke(emiSvc, webhookResponse);
                    log.info("Delegation to EMI completed for entityId: {}", entityId);
                } catch (Exception ex) {
                    log.error("Failed to delegate webhook to EMI service for entityId: {}", entityId, ex);
                }
                return;
            }
            boolean isWait = Objects.equals(existShop.getBusinessProfileStatus(), BusinessProfileStatus.BINDERR_WAIT.getCode());
            boolean isApproved = Objects.equals(existShop.getBusinessProfileStatus(), BusinessProfileStatus.OK.getCode());
            boolean alreadyProcessed = isWebhookAlreadyProcessed(entityId);
            if (BinderrEntityStatusEnums.VERIFIED.name().equals(status)) {
                if (isWait) {
                    updateBusinessProfileStatusByEntityId(entityId, BusinessProfileStatus.OK.getCode());
                } else if (isApproved && alreadyProcessed) {
                    log.info("EntityId {} already approved and processed; skip re-processing", entityId);
                    return;
                } else {
                    log.info("EntityId {} not in waiting status; skip updating to approved. currentStatus={}", entityId, existShop.getBusinessProfileStatus());
                    return;
                }
                String webhookId = getWebhookId(entityId);
                if (webhookId != null) {
                    String apiUrl = binderrConfig.getRemoveSubscribeUrl(webhookId);
                    Map<String, String> headers = buildBinderrAuthHeaders(entityId);
                    HttpUtils.sendDeleteWithOkHttp(apiUrl, headers);
                    log.info("Webhook removed with id: {}", webhookId);
                } else {
                    log.warn("No webhook id found in Redis for entityId: {}", entityId);
                }
                markWebhookProcessed(entityId);
            } else if (BinderrEntityStatusEnums.NEEDS_REVIEW.name().equals(status)) {
                if (isWait) {
                    updateBusinessProfileStatusByEntityId(entityId, BusinessProfileStatus.BINDERR_FAIL.getCode());
                } else if (isApproved && alreadyProcessed) {
                    log.info("EntityId {} already approved and processed; skip downgrading", entityId);
                } else {
                    log.info("EntityId {} not in waiting status; skip updating to binderr_fail. currentStatus={}", entityId, existShop.getBusinessProfileStatus());
                }
            }
        } catch (Exception e) {
            log.error("Invalid webhook payload", e);
            throw new RuntimeException("Invalid webhook payload: " + e.getMessage(), e);
        }
    }

    /**
     * 构建 Binderr 认证头
     *
     * @param entityId 实体ID
     * @return 认证头
     */
    public Map<String, String> buildBinderrAuthHeaders(Long entityId) {
        BinderrTokenCache tokenCache = ensureBinderrToken(entityId);
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + tokenCache.getAccessToken());
        return headers;
    }

    /**
     * 确保 Binderr token 有效
     *
     * @param entityId 实体ID
     * @return token 缓存
     */
    public BinderrTokenCache ensureBinderrToken(Long entityId) {
        if (entityId == null) {
            throw new RuntimeException("Binderr entityId is null");
        }
        BinderrTokenCache tokenCache = redisCache.getCacheObject(buildTokenKey(entityId));
        if (tokenCache == null) {
            throw new RuntimeException("Binderr token not found for entityId: " + entityId);
        }
        if (tokenCache.isExpired(TOKEN_REFRESH_BUFFER_MILLIS)) {
            tokenCache = refreshBinderrToken(entityId, tokenCache);
        }
        return tokenCache;
    }

    /**
     * 缓存 Binderr token
     *
     * @param entityId 实体ID
     * @param tokenRes token 响应
     * @return token 缓存
     */
    public BinderrTokenCache cacheBinderrToken(Long entityId, AccessTokenRes tokenRes) {
        if (entityId == null) {
            return null;
        }
        if (tokenRes == null || ObjUtil.isEmpty(tokenRes.getAccess_token())) {
            throw new RuntimeException("Failed to obtain Binderr access token");
        }
        long expiresInSeconds = 0L;
        try {
            expiresInSeconds = tokenRes.getExpires_in() == null ? 0L : Long.parseLong(tokenRes.getExpires_in());
        } catch (NumberFormatException ignored) {
        }
        if (expiresInSeconds <= 0L) {
            expiresInSeconds = TimeUnit.MINUTES.toSeconds(5);
        }
        long expireAt = System.currentTimeMillis() + Math.max(0L, expiresInSeconds * 1000);
        BinderrTokenCache tokenCache = new BinderrTokenCache()
                .setAccessToken(tokenRes.getAccess_token())
                .setRefreshToken(tokenRes.getRefresh_token())
                .setTokenType(tokenRes.getToken_type())
                .setExpireAt(expireAt);
        redisCache.setCacheObject(buildTokenKey(entityId), tokenCache);
        return tokenCache;
    }

    /**
     * 刷新 Binderr token
     *
     * @param entityId 实体ID
     * @param tokenCache 当前 token 缓存
     * @return 新的 token 缓存
     */
    public BinderrTokenCache refreshBinderrToken(Long entityId, BinderrTokenCache tokenCache) {
        if (tokenCache == null || ObjUtil.isEmpty(tokenCache.getRefreshToken())) {
            throw new RuntimeException("No refresh token available for entityId: " + entityId);
        }
        BinderrRefreshTokenRequest request = new BinderrRefreshTokenRequest()
                .setRefresh_token(tokenCache.getRefreshToken())
                .setGrant_type("refresh_token");
        AccessTokenRes tokenRes = HttpUtils.sendPostJsonWithOkHttp(binderrConfig.getRefreshUrl(), request, null, AccessTokenRes.class);
        return cacheBinderrToken(entityId, tokenRes);
    }

    /**
     * 构建 token key
     *
     * @param entityId 实体ID
     * @return token key
     */
    public String buildTokenKey(Long entityId) {
        return BINDERR_TOKEN_KEY_PREFIX + entityId;
    }

    /**
     * 构建 webhook key
     *
     * @param entityId 实体ID
     * @return webhook key
     */
    public String buildWebhookKey(Long entityId) {
        return BINDERR_WEBHOOK_KEY_PREFIX + entityId;
    }

    /**
     * 构建 webhook 已处理 key
     *
     * @param entityId 实体ID
     * @return webhook 已处理 key
     */
    public String buildWebhookProcessedKey(Long entityId) {
        return BINDERR_WEBHOOK_PROCESSED_KEY_PREFIX + entityId;
    }

    /**
     * 缓存 webhook id
     *
     * @param entityId 实体ID
     * @param webhookId webhook id
     */
    public void cacheWebhookId(Long entityId, String webhookId) {
        if (entityId == null || webhookId == null) {
            return;
        }
        redisCache.setCacheObject(buildWebhookKey(entityId), webhookId);
        log.info("Webhook id cached for entityId: {}, webhookId: {}", entityId, webhookId);
    }

    /**
     * 获取 webhook id
     *
     * @param entityId 实体ID
     * @return webhook id
     */
    public String getWebhookId(Long entityId) {
        if (entityId == null) {
            return null;
        }
        return redisCache.getCacheObject(buildWebhookKey(entityId));
    }

    /**
     * 判断 webhook 是否已处理
     *
     * @param entityId 实体ID
     * @return 是否已处理
     */
    public boolean isWebhookAlreadyProcessed(Long entityId) {
        if (entityId == null) {
            return false;
        }
        Boolean processed = redisCache.getCacheObject(buildWebhookProcessedKey(entityId));
        return Boolean.TRUE.equals(processed);
    }

    /**
     * 标记 webhook 已处理
     *
     * @param entityId 实体ID
     */
    public void markWebhookProcessed(Long entityId) {
        if (entityId == null) {
            return;
        }
        redisCache.setCacheObject(buildWebhookProcessedKey(entityId), Boolean.TRUE);
        log.info("Webhook processing flag cached for entityId: {}", entityId);
    }

    @Override
    public Shop replaceShopToSecondaryMerchant(Shop shop, String cardNumber, Order dbOrder) {
        if (shop == null || shop.getId() == null) {
            return shop;
        }
        String normalizedCardNumber = normalizeCardNumber(cardNumber);
        if (StringUtils.isBlank(normalizedCardNumber)) {
            return shop;
        }
        String cardFinger = null;
        try {
            cardFinger = SignUtil.cardFingerprint(normalizedCardNumber);
        } catch (Exception ignored) {
        }

        // 主商户白名单优先
        if (shop.getIsWhitelistOn() == WhiteListOnType.ON.getValue()) {
            List<WhitelistedCreditCards> parentMatches = Collections.emptyList();
            if (StringUtils.isNotBlank(cardFinger)) {
                parentMatches = whitelistedCreditCardsService.listMatchesByCardFinger(Collections.singleton(shop.getId()), cardFinger);
            }
            if (CollUtil.isEmpty(parentMatches)) {
                parentMatches = whitelistedCreditCardsService.listMatchesByCardNumber(Collections.singleton(shop.getId()), normalizedCardNumber);
            }
            if (CollUtil.isNotEmpty(parentMatches)) {
	                log.info("主商户白名单命中，商户ID:{}，卡号:{}，无需切换二级商户",
	                        shop.getId(), SignUtil.maskPan(normalizedCardNumber));
	                return shop;
	            }
	        } else {
            log.info("主商户白名单处于关闭状态, 商户ID:{}", shop.getId());
        }

        List<ShopEntity> secondaryShops = shopMapper.listWhitelistSecondaryMerchantDetails(
                shop.getId(), WhiteListOnType.ON.getValue());
        if (CollUtil.isEmpty(secondaryShops)) {
            return shop;
        }
        Map<Long, Shop> secondaryShopMap = secondaryShops.stream()
                .filter(child -> child.getId() != null)
                .collect(Collectors.toMap(Shop::getId, s -> s, (s1, s2) -> s1));
        if (secondaryShopMap.isEmpty()) {
            return shop;
        }

        List<WhitelistedCreditCards> matchedCards = Collections.emptyList();
        if (StringUtils.isNotBlank(cardFinger)) {
            matchedCards = whitelistedCreditCardsService.listMatchesByCardFinger(secondaryShopMap.keySet(), cardFinger);
        }
        if (CollUtil.isEmpty(matchedCards)) {
            matchedCards = whitelistedCreditCardsService.listMatchesByCardNumber(secondaryShopMap.keySet(), normalizedCardNumber);
        }
        if (CollUtil.isEmpty(matchedCards)) {
            return shop;
        }

        List<Long> candidateShopIds = matchedCards.stream()
                .map(WhitelistedCreditCards::getShopId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        if (CollUtil.isEmpty(candidateShopIds)) {
            return shop;
        }
        Long targetShopId = candidateShopIds.size() == 1
                ? candidateShopIds.get(0)
                : candidateShopIds.get(ThreadLocalRandom.current().nextInt(candidateShopIds.size()));
        Shop targetShop = secondaryShopMap.get(targetShopId);
        if (targetShop == null) {
            return shop;
        }
	        log.info("二级商户匹配成功，父商户ID:{} 切换为二级商户ID:{}，候选数量:{}，候选商户:{},卡号:{}", shop.getId(),
	                targetShop.getId(), candidateShopIds.size(), candidateShopIds, SignUtil.maskPan(normalizedCardNumber));
        // 商户启用开关
        boolean shopSwitch = targetShop.getStatus() != null && targetShop.getStatus() == 0;
        if (!shopSwitch) {
            throw new RuntimeException("渠道授权暂未开启，请联系管理处理");
        }
        dbOrder.setMerchantId(targetShop.getId());
        orderMapper.updateById(dbOrder);
        return targetShop;
    }

    private String normalizeCardNumber(String cardNumber) {
        if (StringUtils.isBlank(cardNumber)) {
            return null;
        }
        return cardNumber.replaceAll("\\D", "");
    }

	}
