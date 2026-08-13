package com.games.payment.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.games.common.core.redis.RedisCache;
import com.games.common.enums.EmiProfileStatus;
import com.games.common.utils.http.HttpUtils;
import com.games.pay.dto.CreateEntityDto;
import com.games.pay.dto.CreateEntityResDto;
import com.games.pay.enums.BinderrEntityStatusEnums;
import com.games.pay.enums.EntityRelationshipTypeEnums;
import com.games.pay.vo.bindder.*;
import com.games.payment.config.BinderrConfig;
import com.games.payment.domain.EmiEnterpriseInfo;
import com.games.payment.service.IEmiEnterpriseInfoService;
import com.games.payment.mapper.EmiEnterpriseInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 企业基本信息及出口业务信息Service业务层处理
 *
 * @author Ticker
 * @date 2025-10-16
 */
@Slf4j
@Service
public class EmiEnterpriseInfoServiceImpl extends ServiceImpl<EmiEnterpriseInfoMapper, EmiEnterpriseInfo> implements IEmiEnterpriseInfoService {

    @Autowired
    private BinderrConfig binderrConfig;
    
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ShopServiceImpl shopService;

    private static final long TOKEN_REFRESH_BUFFER_MILLIS = TimeUnit.MINUTES.toMillis(1);
    private static final String BINDERR_TOKEN_KEY_PREFIX = "binderr:emi:token:";
    private static final String BINDERR_WEBHOOK_KEY_PREFIX = "binderr:emi:webhook:";
    private static final String BINDERR_WEBHOOK_PROCESSED_KEY_PREFIX = "binderr:emi:webhook:processed:";

    /**
     * 查询企业基本信息及出口业务信息列表
     *
     * @param emiEnterpriseInfo 企业基本信息及出口业务信息
     * @return 企业基本信息及出口业务信息
     */
    @Override
    public List<EmiEnterpriseInfo> selectAllList(EmiEnterpriseInfo emiEnterpriseInfo)
    {
        return getBaseMapper().selectAllList(emiEnterpriseInfo);
    }


    @Override
    public List<EmiEnterpriseInfo> queryList(EmiEnterpriseInfo emiEnterpriseInfo) {
        LambdaQueryWrapper<EmiEnterpriseInfo> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(emiEnterpriseInfo.getCompanyNameChinese())){
            lqw.eq(EmiEnterpriseInfo::getCompanyNameChinese ,emiEnterpriseInfo.getCompanyNameChinese());
        }
        if (StringUtils.isNotBlank(emiEnterpriseInfo.getCompanyNameEnglish())){
            lqw.eq(EmiEnterpriseInfo::getCompanyNameEnglish ,emiEnterpriseInfo.getCompanyNameEnglish());
        }
        if (StringUtils.isNotBlank(emiEnterpriseInfo.getBusinessRegistrationNumber())){
            lqw.eq(EmiEnterpriseInfo::getBusinessRegistrationNumber ,emiEnterpriseInfo.getBusinessRegistrationNumber());
        }
        if (StringUtils.isNotBlank(emiEnterpriseInfo.getCompanyRegistrationCode())){
            lqw.eq(EmiEnterpriseInfo::getCompanyRegistrationCode ,emiEnterpriseInfo.getCompanyRegistrationCode());
        }
        if (StringUtils.isNotBlank(emiEnterpriseInfo.getRegisteredCountry())){
            lqw.eq(EmiEnterpriseInfo::getRegisteredCountry ,emiEnterpriseInfo.getRegisteredCountry());
        }
        if (StringUtils.isNotBlank(emiEnterpriseInfo.getRegisteredAddress())){
            lqw.eq(EmiEnterpriseInfo::getRegisteredAddress ,emiEnterpriseInfo.getRegisteredAddress());
        }
        if (StringUtils.isNotBlank(emiEnterpriseInfo.getActualOperationAddress())){
            lqw.eq(EmiEnterpriseInfo::getActualOperationAddress ,emiEnterpriseInfo.getActualOperationAddress());
        }
        if (emiEnterpriseInfo.getEmployeeCount() != null){
            lqw.eq(EmiEnterpriseInfo::getEmployeeCount ,emiEnterpriseInfo.getEmployeeCount());
        }
        if (emiEnterpriseInfo.getEstablishmentDate() != null){
            lqw.eq(EmiEnterpriseInfo::getEstablishmentDate ,emiEnterpriseInfo.getEstablishmentDate());
        }
        if (emiEnterpriseInfo.getExpirationDate() != null){
            lqw.eq(EmiEnterpriseInfo::getExpirationDate ,emiEnterpriseInfo.getExpirationDate());
        }
        if (StringUtils.isNotBlank(emiEnterpriseInfo.getIndustryType())){
            lqw.eq(EmiEnterpriseInfo::getIndustryType ,emiEnterpriseInfo.getIndustryType());
        }
        if (StringUtils.isNotBlank(emiEnterpriseInfo.getExportType())){
            lqw.eq(EmiEnterpriseInfo::getExportType ,emiEnterpriseInfo.getExportType());
        }
        if (StringUtils.isNotBlank(emiEnterpriseInfo.getExportCountries())){
            lqw.eq(EmiEnterpriseInfo::getExportCountries ,emiEnterpriseInfo.getExportCountries());
        }
        if (StringUtils.isNotBlank(emiEnterpriseInfo.getExportProductName())){
            lqw.like(EmiEnterpriseInfo::getExportProductName ,emiEnterpriseInfo.getExportProductName());
        }
        if (emiEnterpriseInfo.getHistoricalAnnualExport() != null){
            lqw.eq(EmiEnterpriseInfo::getHistoricalAnnualExport ,emiEnterpriseInfo.getHistoricalAnnualExport());
        }
        if (emiEnterpriseInfo.getEstimatedAnnualExport() != null){
            lqw.eq(EmiEnterpriseInfo::getEstimatedAnnualExport ,emiEnterpriseInfo.getEstimatedAnnualExport());
        }
        if (StringUtils.isNotBlank(emiEnterpriseInfo.getWebsite())){
            lqw.eq(EmiEnterpriseInfo::getWebsite ,emiEnterpriseInfo.getWebsite());
        }
        if (emiEnterpriseInfo.getCreatedAt() != null){
            lqw.eq(EmiEnterpriseInfo::getCreatedAt ,emiEnterpriseInfo.getCreatedAt());
        }
        if (emiEnterpriseInfo.getUpdatedAt() != null){
            lqw.eq(EmiEnterpriseInfo::getUpdatedAt ,emiEnterpriseInfo.getUpdatedAt());
        }
        if (emiEnterpriseInfo.getUserId() != null){
            lqw.eq(EmiEnterpriseInfo::getUserId ,emiEnterpriseInfo.getUserId());
        }
        return this.list(lqw);
    }

    /**
     * 根据 entityId 查询企业信息
     *
     * @param entityId Binderr 实体ID
     * @return 企业信息
     */
    @Override
    public EmiEnterpriseInfo getByEntityId(Long entityId) {
        if (entityId == null) {
            return null;
        }
        LambdaQueryWrapper<EmiEnterpriseInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(EmiEnterpriseInfo::getEntityId, entityId.intValue());
        return this.getOne(lqw);
    }

    /**
     * 根据用户ID查询企业信息
     *
     * @param userId 用户ID
     * @return 企业信息
     */
    @Override
    public EmiEnterpriseInfo getByUserId(Long userId) {
        if (userId == null) {
            return null;
        }
        LambdaQueryWrapper<EmiEnterpriseInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(EmiEnterpriseInfo::getUserId, userId);
        return this.getOne(lqw);
    }

    /**
     * 根据 entityId 更新企业状态
     *
     * @param entityId Binderr 实体ID
     * @param status 新状态
     */
    @Override
    public void updateStatusByEntityId(Long entityId, Integer status) {
        if (entityId == null || status == null) {
            return;
        }
        EmiEnterpriseInfo emiInfo = getByEntityId(entityId);
        if (emiInfo != null) {
            emiInfo.setStatus(status);
            this.updateById(emiInfo);
            log.info("Updated EMI enterprise status for entityId: {}, newStatus: {}", entityId, status);
        }
    }

    /**
     * 调用外部 API 创建 Binderr 实体
     *
     * @param emiInfo EMI企业信息
     * @param headers 请求头
     * @return 实体ID
     */
    public Long doCreateEntity(EmiEnterpriseInfo emiInfo, Map<String, String> headers) {
        try {
            CreateEntityDto dto = new CreateEntityDto();
            
            // 设置关系类型为客户
            RelationshipVo relationshipVo = new RelationshipVo();
            relationshipVo.setType(EntityRelationshipTypeEnums.CLIENT.name());
            StatusVo statusVo = new StatusVo();
            statusVo.setId(binderrConfig.getStatusId());
            relationshipVo.setStatus(statusVo);
            dto.setRelationship(relationshipVo);

            // 设置管理者信息
            List<ManagedByVo> managedByList = new ArrayList<>();
            ManagedByVo managedByVo = new ManagedByVo();
            managedByVo.setId(binderrConfig.getManagedById());
            managedByVo.setName(binderrConfig.getManagedByName());
            managedByList.add(managedByVo);
            dto.setManagedBy(managedByList);

            // 设置法人实体信息
            LegalEntityVo legalEntityVo = new LegalEntityVo();
            String country = emiInfo.getRegisteredCountry();
            String threeLetterCountryCode = shopService.convertToThreeLetterCountryCode(country);
            legalEntityVo.setIncorporationCountryCode(threeLetterCountryCode);
            TypeVo typeVo = new TypeVo();
            typeVo.setId(1);
            legalEntityVo.setType(typeVo);
            legalEntityVo.setRegistrationNumber(emiInfo.getBusinessRegistrationNumber());
            legalEntityVo.setName(emiInfo.getCompanyNameEnglish());
            dto.setLegalEntity(legalEntityVo);

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonBody = objectMapper.writeValueAsString(dto);
            String apiUrl = binderrConfig.getCreateEntityUrl();
            log.info("Calling create entity API: {}", apiUrl);
            log.info("Request parameters: {}", jsonBody);
            
            String response = HttpUtils.sendPostJsonWithOkHttp(apiUrl, jsonBody, headers);
            log.info("Response: {}", response);
            
            CreateEntityResDto resDto = objectMapper.readValue(response, CreateEntityResDto.class);
            Long entityId = resDto.getId();
            log.info("Entity created successfully with id: {}", entityId);
            
            return entityId;
        } catch (Exception e) {
            log.error("Failed to create Binderr entity", e);
            throw new RuntimeException("Failed to create Binderr entity: " + e.getMessage(), e);
        }
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
        log.info("Binderr token cached for entityId: {}", entityId);
        return tokenCache;
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
            
            EmiEnterpriseInfo existEmi = getByEntityId(entityId);
            if (existEmi == null) {
                log.warn("No EMI enterprise found by entityId: {}", entityId);
                return;
            }
            
            boolean isWait = Objects.equals(existEmi.getStatus(), EmiProfileStatus.BINDERR_WAIT.getCode()); // 0 表示等待审核
            boolean isApproved = Objects.equals(existEmi.getStatus(), EmiProfileStatus.OK.getCode()); // 1 表示审核通过
            boolean alreadyProcessed = isWebhookAlreadyProcessed(entityId);

            EmiEnterpriseInfo newEmi = new EmiEnterpriseInfo();
            if (BinderrEntityStatusEnums.VERIFIED.name().equals(status)) {
                if (isWait) {
                    // 审核通过
                    updateStatusByEntityId(entityId, EmiProfileStatus.OK.getCode());
                    newEmi.setId(existEmi.getId());
                    newEmi.setCardNumber(generateRandomCardNumber());
                    newEmi.setBank("Tallinn Bank");
                    updateById(newEmi);
                } else if (isApproved && alreadyProcessed) {
                    log.info("EntityId {} already approved and processed; skip re-processing", entityId);
                    return;
                } else {
                    log.info("EntityId {} not in waiting status; skip updating to approved. currentStatus={}", 
                            entityId, existEmi.getStatus());
                    return;
                }
                
                // 移除 webhook 订阅
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
                    // 审核失败
                    updateStatusByEntityId(entityId, EmiProfileStatus.FAIL.getCode());
                } else if (isApproved && alreadyProcessed) {
                    log.info("EntityId {} already approved and processed; skip downgrading", entityId);
                } else {
                    log.info("EntityId {} not in waiting status; skip updating to failed. currentStatus={}", 
                            entityId, existEmi.getStatus());
                }
            }
        } catch (Exception e) {
            log.error("Invalid webhook payload, raw= {}", webhookResponse, e);
            throw new RuntimeException("Invalid webhook payload: " + e.getMessage(), e);
        }
    }


    public String generateRandomCardNumber() {
        StringBuilder cardNumber = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            cardNumber.append((int) (Math.random() * 10));
        }
        return cardNumber.toString();
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

}
