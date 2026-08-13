package com.games.payment.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.games.common.core.domain.AjaxResult;
import com.games.common.enums.WhiteImportStatus;
import com.games.common.enums.WhiteImportType;
import com.games.common.utils.CardNumberUtils;
import com.games.common.utils.MessageUtils;
import com.games.common.utils.SignUtil;
import com.games.common.exception.BusinessException;
import com.games.payment.domain.Shop;
import com.games.payment.domain.WhiteUploadRecord;
import com.games.payment.domain.WhitelistedCreditCards;
import com.games.payment.mapper.ShopMapper;
import com.games.payment.mapper.WhiteUploadRecordMapper;
import com.games.payment.mapper.WhitelistedCreditCardsMapper;
import com.games.payment.service.IWhitelistedCreditCardsService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 白名单信用卡Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
@Slf4j
public class WhitelistedCreditCardsServiceImpl extends ServiceImpl<WhitelistedCreditCardsMapper, WhitelistedCreditCards> implements IWhitelistedCreditCardsService {

    @Resource
    private WhiteUploadRecordMapper whiteUploadRecordMapper;
    
    @Resource
    private ShopMapper shopMapper;

    private static final int DEFAULT_BATCH_SIZE = 5000;
    private static final int DELETE_BATCH_SIZE = 3000; // 删除操作使用更小的批次
    private static final Pattern MASKED_CARD_PATTERN = Pattern.compile("^(\\d{6})([\\d\\*]{6,9})(\\d{4})$"); // 保留用于 extractCardSegments 方法
    private final ConcurrentMap<Long, Boolean> shopExistCache = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, List<Long>> shopIdsParseCache = new ConcurrentHashMap<>();

    @Override
    public boolean save(WhitelistedCreditCards entity) {
        fillCardFieldsForCompliance(entity);
        return super.save(entity);
    }

    @Override
    public boolean updateById(WhitelistedCreditCards entity) {
        fillCardFieldsForCompliance(entity);
        return super.updateById(entity);
    }

    /**
     * 使用批量INSERT ON DUPLICATE KEY UPDATE保存数据，处理逻辑删除
     *
     * @param entityList 实体列表
     * @param batchSize  批次大小
     */
    private void saveBatchOrUpdate(List<WhitelistedCreditCards> entityList, int batchSize) {
        if (entityList == null || entityList.isEmpty()) {
            return;
        }
        
        // 分批处理，每批1000条
        for (int i = 0; i < entityList.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, entityList.size());
            List<WhitelistedCreditCards> batch = entityList.subList(i, endIndex);
            
            // 使用批量INSERT ON DUPLICATE KEY UPDATE
            getBaseMapper().batchInsertOrUpdate(batch);
        }
    }

    /**
     * 查询白名单信用卡列表
     *
     * @param whitelistedCreditCards 白名单信用卡
     * @return 白名单信用卡
     */
    @Override
    public List<WhitelistedCreditCards> selectAllList(WhitelistedCreditCards whitelistedCreditCards)
    {
        normalizeQueryFullNumberCondition(whitelistedCreditCards);
        return getBaseMapper().selectAllList(whitelistedCreditCards);
    }

    /**
     * 批量验证shopId是否存在，并返回有效的shopId集合
     * @param shopIds 需要验证的shopId集合
     * @return 有效的shopId集合
     */
    private Set<Long> validateShopIds(Set<Long> shopIds) {
        if (shopIds == null || shopIds.isEmpty()) {
            return new HashSet<>();
        }
        
        Set<Long> validShopIds = new HashSet<>();
        Set<Long> toQueryShopIds = new HashSet<>();
        for (Long shopId : shopIds) {
            if (shopId == null) {
                continue;
            }
            Boolean cached = shopExistCache.get(shopId);
            if (cached != null) {
                if (cached) {
                    validShopIds.add(shopId);
                }
                continue;
            }
            toQueryShopIds.add(shopId);
        }

        if (!toQueryShopIds.isEmpty()) {
            LambdaQueryWrapper<Shop> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(Shop::getId, toQueryShopIds);
            queryWrapper.select(Shop::getId);

            List<Shop> existingShops = shopMapper.selectList(queryWrapper);
            Set<Long> foundIds = existingShops.stream()
                    .map(Shop::getId)
                    .collect(Collectors.toSet());
            for (Long id : foundIds) {
                shopExistCache.put(id, Boolean.TRUE);
            }
            validShopIds.addAll(foundIds);
            toQueryShopIds.removeAll(foundIds);
            for (Long missingId : toQueryShopIds) {
                shopExistCache.put(missingId, Boolean.FALSE);
            }
        }
        return validShopIds;
    }

    /**
     * 从shopIds字符串中提取shopId（不验证）
     * @param shopIdsStr shopIds字符串，格式如 "1;2;3"
     * @return shopId集合
     */
    private Set<Long> extractShopIds(String shopIdsStr) {
        if (StringUtils.isBlank(shopIdsStr)) {
            return new HashSet<>();
        }

        List<Long> cached = shopIdsParseCache.get(shopIdsStr);
        if (cached != null) {
            return new HashSet<>(cached);
        }

        Set<Long> shopIds = new LinkedHashSet<>();
        String[] shopIdArray = shopIdsStr.split(";");
        for (String shopIdStr : shopIdArray) {
            if (StringUtils.isBlank(shopIdStr)) {
                continue;
            }
            try {
                Long shopId = Long.parseLong(shopIdStr.trim());
                shopIds.add(shopId);
            } catch (NumberFormatException e) {
                log.warn("无效的shopId格式: {}", shopIdStr);
            }
        }
        shopIdsParseCache.put(shopIdsStr, new ArrayList<>(shopIds));
        return shopIds;
    }

    /**
     * 从数据列表中收集所有唯一的shopId并批量验证
     * @param dataList 数据列表
     * @return 有效的shopId集合
     */
    private Set<Long> collectAndValidateAllShopIds(List<WhitelistedCreditCards> dataList) {
        // 收集所有唯一的shopId
        Set<Long> allShopIds = new HashSet<>();
        for (WhitelistedCreditCards data : dataList) {
            String shopIds = data.getShopIds();
            if (StringUtils.isNotBlank(shopIds)) {
                allShopIds.addAll(extractShopIds(shopIds));
            }
        }
        
        // 批量验证所有shopId
        return validateShopIds(allShopIds);
    }


    @Override
    public List<WhitelistedCreditCards> queryList(WhitelistedCreditCards whitelistedCreditCards) {
        normalizeQueryFullNumberCondition(whitelistedCreditCards);
        LambdaQueryWrapper<WhitelistedCreditCards> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(whitelistedCreditCards.getFullNumber())){
            lqw.eq(WhitelistedCreditCards::getFullNumber ,whitelistedCreditCards.getFullNumber());
        }
        if (StringUtils.isNotBlank(whitelistedCreditCards.getCardFinger())) {
            lqw.eq(WhitelistedCreditCards::getCardFinger, whitelistedCreditCards.getCardFinger());
        }
        if (StringUtils.isNotBlank(whitelistedCreditCards.getBinNumber())){
            lqw.eq(WhitelistedCreditCards::getBinNumber ,whitelistedCreditCards.getBinNumber());
        }
        if (StringUtils.isNotBlank(whitelistedCreditCards.getLastFourNumber())){
            lqw.eq(WhitelistedCreditCards::getLastFourNumber ,whitelistedCreditCards.getLastFourNumber());
        }
        if (StringUtils.isNotBlank(whitelistedCreditCards.getEmail())){
            lqw.eq(WhitelistedCreditCards::getEmail ,whitelistedCreditCards.getEmail());
        }
        if (whitelistedCreditCards.getCreatedAt() != null){
            lqw.eq(WhitelistedCreditCards::getCreatedAt ,whitelistedCreditCards.getCreatedAt());
        }
        if (whitelistedCreditCards.getCreatedBy() != null){
            lqw.eq(WhitelistedCreditCards::getCreatedBy ,whitelistedCreditCards.getCreatedBy());
        }
        if (whitelistedCreditCards.getIds() != null && whitelistedCreditCards.getIds().length > 0){
            lqw.in(WhitelistedCreditCards::getId, (Object[]) whitelistedCreditCards.getIds());
        }
        return this.list(lqw);
    }

    /**
     * 列表/导出查询时，允许前端/用户输入完整 PAN，但实际查询优先走 card_finger，
     * 回退为查询“前六****后四”格式的 full_number（用于兼容既有查询入口）。
     *
     * 注意：该方法只做查询条件规范化，不会修改数据库。
     */
    private void normalizeQueryFullNumberCondition(WhitelistedCreditCards condition) {
        if (condition == null) {
            return;
        }
        String inputFullNumber = condition.getFullNumber();
        if (StringUtils.isBlank(inputFullNumber)) {
            return;
        }
        String normalizedDigits = CardNumberUtils.normalizeDigits(inputFullNumber);
        String cardFinger = tryCardFingerprint(normalizedDigits);
        if (StringUtils.isNotBlank(cardFinger)) {
            condition.setCardFinger(cardFinger);
            condition.setFullNumber(null);
            return;
        }
        String masked = CardNumberUtils.normalizeMaskedPanForStorage(inputFullNumber);
        if (StringUtils.isNotBlank(masked)) {
            condition.setFullNumber(masked);
        } else {
            condition.setFullNumber(null);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importData(List<WhitelistedCreditCards> dataList, Integer importType, WhiteUploadRecord whiteUploadRecord) {
        int successCount = 0;
        
        try {
            // 根据导入类型执行不同操作（仅覆盖与移除）
            if (Objects.equals(importType, WhiteImportType.COVER.getCode())) {
                // 覆盖模式：存在更新，不存在插入，不删除
                processCoverMode(dataList, whiteUploadRecord);
                successCount = dataList.size();
            } else if (Objects.equals(importType, WhiteImportType.REMOVE.getCode())) {
                // 移除模式：批量删除数据
                processRemoveMode(dataList);
                successCount = dataList.size();
            }

            // 导入成功，更新记录状态
            whiteUploadRecord.setFinishedAt(new Date());
            whiteUploadRecord.setStatus(WhiteImportStatus.IMPORTED.ordinal()); // 成功状态
            whiteUploadRecordMapper.updateById(whiteUploadRecord);
            
            System.out.println("白名单导入完成: 成功处理 " + successCount + " 条数据");
            
        } catch (DuplicateKeyException e) {
            // 处理重复键异常，记录日志但不中断流程
            log.warn("导入过程中发现重复数据，已自动跳过: " + e.getMessage());
            
            // 导入成功，更新记录状态（即使有重复数据也认为导入成功）
            whiteUploadRecord.setFinishedAt(new Date());
            whiteUploadRecord.setStatus(WhiteImportStatus.IMPORTED.ordinal());
            whiteUploadRecordMapper.updateById(whiteUploadRecord);
            
            System.out.println("白名单导入完成: 成功处理 " + successCount + " 条数据，跳过重复数据");
            
        } catch (Exception e) {
            // 导入失败，更新记录状态
            whiteUploadRecord.setFinishedAt(new Date());
            whiteUploadRecord.setStatus(WhiteImportStatus.IMPORTED_FAIL.ordinal()); // 失败状态
            whiteUploadRecordMapper.updateById(whiteUploadRecord);
            // 记录错误日志
            log.error("白名单导入失败", e);
            // 抛出异常以触发事务回滚
            throw BusinessException.api("whitelist.import.failed");
        }
    }


    @Override
    public AjaxResult updateWhiteList(WhitelistedCreditCards whitelistedCreditCards) {

        Long shopId = whitelistedCreditCards.getShopId();
        String binNumber = whitelistedCreditCards.getBinNumber();
        String lastFourNumber = whitelistedCreditCards.getLastFourNumber();
        Integer riskLevel = whitelistedCreditCards.getRiskLevel();

        // 参数验证
        if (shopId == null || StringUtils.isBlank(binNumber) || StringUtils.isBlank(lastFourNumber)) {
            return AjaxResult.error(MessageUtils.message("whitelist.parameter.incomplete", shopId, binNumber, lastFourNumber));
        }

        // 验证商户是否存在
        Shop shop = shopMapper.selectById(shopId);
        if (shop == null) {
            return AjaxResult.error(StrUtil.format("shopId={} is not exist", shopId));
        }

        // 验证数据是否重复（binNumber + lastFourNumber + shopId）
        LambdaQueryWrapper<WhitelistedCreditCards> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WhitelistedCreditCards::getBinNumber, binNumber)
                   .eq(WhitelistedCreditCards::getLastFourNumber, lastFourNumber)
                   .eq(WhitelistedCreditCards::getShopId, shopId)
                   .eq(WhitelistedCreditCards::getRiskLevel, riskLevel);

        // 如果是更新操作，排除当前记录
        if (whitelistedCreditCards.getId() != null) {
            queryWrapper.ne(WhitelistedCreditCards::getId, whitelistedCreditCards.getId());
        }

        long count = this.count(queryWrapper);
        if (count > 0) {
            return AjaxResult.error(MessageUtils.message("whitelist.data.duplicate", binNumber, lastFourNumber, shopId));
        }
        // 执行更新操作
        fillCardFieldsForCompliance(whitelistedCreditCards);
        whitelistedCreditCards.setUpdatedAt(new Date());
        boolean result = this.updateById(whitelistedCreditCards);
        if (result) {
            log.debug(MessageUtils.message("whitelist.update.success", whitelistedCreditCards.getId(), binNumber, lastFourNumber, shopId));
        } else {
            log.warn(MessageUtils.message("whitelist.update.failed", whitelistedCreditCards.getId()));
        }
        return AjaxResult.success();
    }

    @Override
    public List<WhitelistedCreditCards> listMatchesByCardNumber(Set<Long> shopIds, String cardNumber) {
        if (CollUtil.isEmpty(shopIds) || StringUtils.isBlank(cardNumber)) {
            return Collections.emptyList();
        }

        String normalizedDigits = CardNumberUtils.normalizeDigits(cardNumber);
        String cardFinger = tryCardFingerprint(normalizedDigits);
        if (StringUtils.isNotBlank(cardFinger)) {
            List<WhitelistedCreditCards> fingerMatches = listMatchesByCardFinger(shopIds, cardFinger);
            if (CollUtil.isNotEmpty(fingerMatches)) {
                return fingerMatches;
            }
        }

        String maskedForQuery = CardNumberUtils.normalizeMaskedPanForStorage(cardNumber);
        if (StringUtils.isNotBlank(maskedForQuery)) {
            boolean legacyRawCandidate = normalizedDigits.matches("\\d{16,19}");
            LambdaQueryWrapper<WhitelistedCreditCards> fullWrapper = Wrappers.<WhitelistedCreditCards>lambdaQuery()
                    .in(WhitelistedCreditCards::getShopId, shopIds)
                    .and(wrapper -> {
                        wrapper.eq(WhitelistedCreditCards::getFullNumber, maskedForQuery);
                        if (legacyRawCandidate) {
                            wrapper.or().eq(WhitelistedCreditCards::getFullNumber, normalizedDigits);
                        }
                    })
                    .orderByDesc(WhitelistedCreditCards::getUpdatedAt)
                    .orderByDesc(WhitelistedCreditCards::getId);
            List<WhitelistedCreditCards> fullMatches = this.list(fullWrapper);
            if (CollUtil.isNotEmpty(fullMatches)) {
                return fullMatches;
            }
        }

        if (normalizedDigits.length() < 10) {
            return Collections.emptyList();
        }
        String binNumber = normalizedDigits.substring(0, 6);
        String lastFourNumber = normalizedDigits.substring(normalizedDigits.length() - 4);
        LambdaQueryWrapper<WhitelistedCreditCards> partialWrapper = Wrappers.<WhitelistedCreditCards>lambdaQuery()
                .in(WhitelistedCreditCards::getShopId, shopIds)
                .eq(WhitelistedCreditCards::getBinNumber, binNumber)
                .eq(WhitelistedCreditCards::getLastFourNumber, lastFourNumber)
                .orderByDesc(WhitelistedCreditCards::getUpdatedAt)
                .orderByDesc(WhitelistedCreditCards::getId);
        return this.list(partialWrapper);
    }

    @Override
    public List<WhitelistedCreditCards> listMatchesByCardFinger(Set<Long> shopIds, String cardFinger) {
        if (CollUtil.isEmpty(shopIds) || StringUtils.isBlank(cardFinger)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<WhitelistedCreditCards> wrapper = Wrappers.<WhitelistedCreditCards>lambdaQuery()
                .in(WhitelistedCreditCards::getShopId, shopIds)
                .eq(WhitelistedCreditCards::getCardFinger, cardFinger)
                .orderByDesc(WhitelistedCreditCards::getUpdatedAt)
                .orderByDesc(WhitelistedCreditCards::getId);
        return this.list(wrapper);
    }

    @Override
    public List<WhitelistedCreditCards> selectByFullNumber(String fullNumber, Long shopId) {
        if (StringUtils.isBlank(fullNumber) || shopId == null) {
            return Collections.emptyList();
        }

        String normalizedDigits = CardNumberUtils.normalizeDigits(fullNumber);
        String cardFinger = tryCardFingerprint(normalizedDigits);
        if (StringUtils.isNotBlank(cardFinger)) {
            List<WhitelistedCreditCards> fingerMatches = selectByCardFinger(cardFinger, shopId);
            if (CollUtil.isNotEmpty(fingerMatches)) {
                return fingerMatches;
            }
        }

        String maskedForQuery = CardNumberUtils.normalizeMaskedPanForStorage(fullNumber);
        boolean legacyRawCandidate = normalizedDigits.matches("\\d{16,19}");
        if (StringUtils.isBlank(maskedForQuery) && !legacyRawCandidate) {
            return Collections.emptyList();
        }

        LambdaQueryWrapper<WhitelistedCreditCards> wrapper = Wrappers.<WhitelistedCreditCards>lambdaQuery()
                .eq(WhitelistedCreditCards::getShopId, shopId);
        if (StringUtils.isNotBlank(maskedForQuery) && legacyRawCandidate) {
            wrapper.and(w -> w.eq(WhitelistedCreditCards::getFullNumber, maskedForQuery)
                    .or().eq(WhitelistedCreditCards::getFullNumber, normalizedDigits));
        } else if (StringUtils.isNotBlank(maskedForQuery)) {
            wrapper.eq(WhitelistedCreditCards::getFullNumber, maskedForQuery);
        } else {
            wrapper.eq(WhitelistedCreditCards::getFullNumber, normalizedDigits);
        }
        return this.list(wrapper);
    }

    @Override
    public List<WhitelistedCreditCards> selectByCardFinger(String cardFinger, Long shopId) {
        LambdaQueryWrapper<WhitelistedCreditCards> wrapper = Wrappers.<WhitelistedCreditCards>lambdaQuery()
                .eq(WhitelistedCreditCards::getCardFinger, cardFinger)
                .eq(WhitelistedCreditCards::getShopId, shopId);
        return this.list(wrapper);
    }

    @Override
    public List<WhitelistedCreditCards> selectByBinNumber(String binNumber, String lastFourNumber, Long shopId) {
        LambdaQueryWrapper<WhitelistedCreditCards> fullWrapper = Wrappers.<WhitelistedCreditCards>lambdaQuery()
                .eq(WhitelistedCreditCards::getBinNumber, binNumber)
                .eq(WhitelistedCreditCards::getLastFourNumber, lastFourNumber)
                .eq(WhitelistedCreditCards::getShopId, shopId);
        return this.list(fullWrapper);
    }

    /**
     * 覆盖模式处理
     * @param dataList 数据列表
     * @param whiteUploadRecord 上传记录
     */
    private void processCoverMode(List<WhitelistedCreditCards> dataList, WhiteUploadRecord whiteUploadRecord) {
        // 覆盖模式（优化）：不删除既有数据，存在则更新，不存在则插入
        Set<Long> validShopIds = collectAndValidateAllShopIds(dataList);

        // 统计与日志：计算数据中出现的全部 shopId 与无效 shopId 集合
        Set<Long> allShopIdsInData = new HashSet<>();
        for (WhitelistedCreditCards data : dataList) {
            if (StringUtils.isNotBlank(data.getShopIds())) {
                allShopIdsInData.addAll(extractShopIds(data.getShopIds()));
            }
        }
        Set<Long> invalidShopIds = new HashSet<>(allShopIdsInData);
        invalidShopIds.removeAll(validShopIds);

        List<WhitelistedCreditCards> upsertList = new ArrayList<>();
        Date now = new Date();

        int invalidRowCount = 0; // 无效的条数（字段缺失或经校验无可用shopId）

        for (WhitelistedCreditCards data : dataList) {
            CardInfo cardInfo = resolveCardInfo(data);
            if (cardInfo.isInvalid()) {
                invalidRowCount++;
                continue;
            }
            String shopIds = data.getShopIds();
            if (StringUtils.isBlank(shopIds)) {
                invalidRowCount++;
                continue;
            }
            String binNumber = cardInfo.getBinNumber();
            String lastFourNumber = cardInfo.getLastFourNumber();
            String storedFullNumber = cardInfo.getFullNumberToStore();
            String storedCardFinger = null;
            String fullNumberForFinger = cardInfo.getFullNumberForFinger();
            if (StringUtils.isNotBlank(fullNumberForFinger)) {
                try {
                    storedCardFinger = SignUtil.cardFingerprint(fullNumberForFinger);
                } catch (Exception e) {
                    log.warn("白名单导入计算卡指纹失败: fullNumber={}, err={}",
                            SignUtil.maskPan(fullNumberForFinger), e.getMessage());
                }
            }

            Set<Long> currentShopIds = extractShopIds(shopIds);
            currentShopIds.retainAll(validShopIds);
            if (currentShopIds.isEmpty()) {
                    invalidRowCount++;
                    continue;
                }

            for (Long shopId : currentShopIds) {
                WhitelistedCreditCards card = new WhitelistedCreditCards()
                        .setBinNumber(binNumber)
                        .setLastFourNumber(lastFourNumber)
                        .setRiskLevel(data.getRiskLevel())
                        .setShopId(shopId)
                        .setWhiteUploadId(whiteUploadRecord.getId())
                        .setCreatedAt(now)
                        .setUpdatedAt(now);
                card.setFullNumber(storedFullNumber);
                card.setCardFinger(storedCardFinger);
                upsertList.add(card);
            }
        }

        // 日志：有效/无效条数与无效 shopId
        int validCount = upsertList.size();
        log.info("白名单导入(覆盖模式)：有效条数={}, 无效条数={}", validCount, invalidRowCount);
        if (!invalidShopIds.isEmpty()) {
            log.warn("白名单导入(覆盖模式)：无效的 shopId 共 {} 个：{}", invalidShopIds.size(), invalidShopIds);
        }

        if (!upsertList.isEmpty()) {
            // 依赖唯一键 (bin_number, last_four_number, shop_id) 进行 ON DUPLICATE KEY UPDATE
            saveBatchOrUpdate(upsertList, DEFAULT_BATCH_SIZE);
        }
    }


    /**
     * 移除模式处理（优化版：流式处理 + 分批删除）
     * @param dataList 数据列表
     */
    private void processRemoveMode(List<WhitelistedCreditCards> dataList) {
        Set<Long> validShopIds = collectAndValidateAllShopIds(dataList);

        // 使用流式处理，避免一次性加载所有数据到内存
        List<CardFingerKey> fingerBuffer = new ArrayList<>(DELETE_BATCH_SIZE);
        List<CardTripleKey> tripleBuffer = new ArrayList<>(DELETE_BATCH_SIZE);
        Set<String> seenFingerKeys = new HashSet<>(); // 用于去重
        Set<String> seenTripleKeys = new HashSet<>(); // 用于去重
        int totalProcessed = 0;
        int totalDeleted = 0;

        for (WhitelistedCreditCards data : dataList) {
            CardInfo cardInfo = resolveCardInfo(data);
            if (cardInfo.isInvalid()) {
                continue;
            }
            String shopIds = data.getShopIds();
            if (StringUtils.isBlank(shopIds)) {
                continue;
            }
            String binNumber = cardInfo.getBinNumber();
            String lastFourNumber = cardInfo.getLastFourNumber();
            String cardFinger = tryCardFingerprint(cardInfo.getFullNumberForFinger());

            Set<Long> currentShopIds = extractShopIds(shopIds);
            currentShopIds.retainAll(validShopIds);
            if (currentShopIds.isEmpty()) {
                continue;
            }

            for (Long shopId : currentShopIds) {
                if (StringUtils.isNotBlank(cardFinger)) {
                    String key = buildFingerKey(shopId, cardFinger);
                    if (seenFingerKeys.add(key)) {
                        fingerBuffer.add(new CardFingerKey(shopId, cardFinger));
                        if (fingerBuffer.size() >= DELETE_BATCH_SIZE) {
                            int deleted = deleteByCardFingersOptimized(fingerBuffer);
                            totalDeleted += deleted;
                            totalProcessed += fingerBuffer.size();
                            fingerBuffer.clear();
                            if (seenFingerKeys.size() > 100000) {
                                seenFingerKeys.clear();
                            }
                        }
                    }
                }
                // 兼容：历史数据可能未落库 card_finger，回退用 (bin,last4,shopId) 删除
                String tripleKey = buildTripleKey(binNumber, lastFourNumber, shopId);
                if (seenTripleKeys.add(tripleKey)) {
                    tripleBuffer.add(new CardTripleKey(binNumber, lastFourNumber, shopId));
                    if (tripleBuffer.size() >= DELETE_BATCH_SIZE) {
                        int deleted = deleteByCardTriplesOptimized(tripleBuffer);
                        totalDeleted += deleted;
                        totalProcessed += tripleBuffer.size();
                        tripleBuffer.clear();
                        if (seenTripleKeys.size() > 100000) {
                            seenTripleKeys.clear();
                        }
                    }
                }
            }
        }

        // 处理剩余数据
        if (!fingerBuffer.isEmpty()) {
            int deleted = deleteByCardFingersOptimized(fingerBuffer);
            totalDeleted += deleted;
            totalProcessed += fingerBuffer.size();
        }
        if (!tripleBuffer.isEmpty()) {
            int deleted = deleteByCardTriplesOptimized(tripleBuffer);
            totalDeleted += deleted;
            totalProcessed += tripleBuffer.size();
        }

        log.info("白名单移除模式完成：处理 {} 条记录，实际删除 {} 条", totalProcessed, totalDeleted);
    }


    /**
     * 优化的批量删除方法：按 (shop_id, card_finger) 批量删除（优先，命中更精确）
     */
    private int deleteByCardFingersOptimized(List<CardFingerKey> keys) {
        int totalDeleted = 0;
        for (int i = 0; i < keys.size(); i += DELETE_BATCH_SIZE) {
            int endIndex = Math.min(i + DELETE_BATCH_SIZE, keys.size());
            List<WhitelistedCreditCards> params = new ArrayList<>(endIndex - i);
            for (CardFingerKey key : keys.subList(i, endIndex)) {
                params.add(new WhitelistedCreditCards()
                        .setShopId(key.getShopId())
                        .setCardFinger(key.getCardFinger()));
            }
            totalDeleted += getBaseMapper().deleteByCardFingers(params);
        }
        return totalDeleted;
    }

    /**
     * 优化的批量删除方法：按 (bin_number, last_four_number, shop_id) 批量删除（无指纹时回退）
     */
    private int deleteByCardTriplesOptimized(List<CardTripleKey> keys) {
        int totalDeleted = 0;
        for (int i = 0; i < keys.size(); i += DELETE_BATCH_SIZE) {
            int endIndex = Math.min(i + DELETE_BATCH_SIZE, keys.size());
            List<WhitelistedCreditCards> params = new ArrayList<>(endIndex - i);
            for (CardTripleKey key : keys.subList(i, endIndex)) {
                params.add(new WhitelistedCreditCards()
                        .setBinNumber(key.getBinNumber())
                        .setLastFourNumber(key.getLastFourNumber())
                        .setShopId(key.getShopId()));
            }
            totalDeleted += getBaseMapper().deleteByCardTriples(params);
        }
        return totalDeleted;
    }

    private CardInfo resolveCardInfo(WhitelistedCreditCards data) {
        CardInfo info = new CardInfo();
        String fullNumber = data.getFullNumber();
        if (StringUtils.isNotBlank(fullNumber)) {
            String sanitized = fullNumber.replaceAll("[\\s-]+", "");
            String[] segments = extractCardSegments(sanitized);
            if (segments == null) {
                info.setInvalid(true);
                return info;
            }
            info.setBinNumber(segments[0]);
            info.setLastFourNumber(segments[1]);
            if (sanitized.matches("\\d{16,19}")) {
                info.setFullNumberForFinger(sanitized);
                info.setFullNumberToStore(SignUtil.maskPan(sanitized));
            } else {
                info.setFullNumberToStore(segments[0] + "****" + segments[1]);
            }
            info.setInvalid(false);
            return info;
        }
        String bin = data.getBinNumber();
        String last = data.getLastFourNumber();
        if (StringUtils.isBlank(bin) || StringUtils.isBlank(last)) {
            info.setInvalid(true);
            return info;
        }
        String trimmedBin = bin.trim();
        String trimmedLast = last.trim();
        if (!trimmedBin.matches("\\d{6}") || !trimmedLast.matches("\\d{4}")) {
            info.setInvalid(true);
            return info;
        }
        info.setBinNumber(trimmedBin);
        info.setLastFourNumber(trimmedLast);
        info.setFullNumberToStore(trimmedBin + "****" + trimmedLast);
        info.setInvalid(false);
        return info;
    }

    private static String buildFingerKey(Long shopId, String cardFinger) {
        return shopId + "|" + cardFinger;
    }

    private static String buildTripleKey(String binNumber, String lastFourNumber, Long shopId) {
        return binNumber + "|" + lastFourNumber + "|" + shopId;
    }

    @Getter
    private static class CardTripleKey {
        private final String binNumber;
        private final String lastFourNumber;
        private final Long shopId;

        private CardTripleKey(String binNumber, String lastFourNumber, Long shopId) {
            this.binNumber = binNumber;
            this.lastFourNumber = lastFourNumber;
            this.shopId = shopId;
        }
    }

    @Getter
    private static class CardFingerKey {
        private final Long shopId;
        private final String cardFinger;

        private CardFingerKey(Long shopId, String cardFinger) {
            this.shopId = shopId;
            this.cardFinger = cardFinger;
        }
    }

    // 兼容模式上下文已移除
    private String[] extractCardSegments(String fullNumber) {
        if (StringUtils.isBlank(fullNumber)) {
            return null;
        }
        String sanitized = fullNumber.replaceAll("[\\s-]+", "");
        if (sanitized.matches("\\d{16,19}")) {
            return new String[] { sanitized.substring(0, 6), sanitized.substring(sanitized.length() - 4) };
        }
        Matcher matcher = MASKED_CARD_PATTERN.matcher(sanitized);
        if (matcher.matches()) {
            return new String[] { matcher.group(1), matcher.group(3) };
        }
        return null;
    }


    private static String tryCardFingerprint(String normalizedDigitsPan) {
        if (StringUtils.isBlank(normalizedDigitsPan) || !normalizedDigitsPan.matches("\\d{16,19}")) {
            return null;
        }
        try {
            return SignUtil.cardFingerprint(normalizedDigitsPan);
        } catch (Exception ignored) {
            return null;
        }
    }

    /**
     * 合规：禁止落库完整 PAN，仅允许保存“前六****后四”。
     * 允许输入完整 PAN，用于计算 card_finger 与补齐 bin/last4。
     */
    private void fillCardFieldsForCompliance(WhitelistedCreditCards entity) {
        if (entity == null) {
            return;
        }
        String inputFullNumber = entity.getFullNumber();
        if (StringUtils.isBlank(inputFullNumber)) {
            String bin = StringUtils.trimToEmpty(entity.getBinNumber());
            String last4 = StringUtils.trimToEmpty(entity.getLastFourNumber());
            if (bin.matches("\\d{6}") && last4.matches("\\d{4}")) {
                entity.setFullNumber(bin + "****" + last4);
            }
            return;
        }

        String normalizedDigits = CardNumberUtils.normalizeDigits(inputFullNumber);
        if (normalizedDigits.matches("\\d{16,19}")) {
            if (StringUtils.isBlank(entity.getCardFinger())) {
                String cardFinger = tryCardFingerprint(normalizedDigits);
                if (StringUtils.isNotBlank(cardFinger)) {
                    entity.setCardFinger(cardFinger);
                }
            }
            if (StringUtils.isBlank(entity.getBinNumber())) {
                entity.setBinNumber(normalizedDigits.substring(0, 6));
            }
            if (StringUtils.isBlank(entity.getLastFourNumber())) {
                entity.setLastFourNumber(normalizedDigits.substring(normalizedDigits.length() - 4));
            }
        }
        entity.setFullNumber(CardNumberUtils.normalizeMaskedPanForStorage(inputFullNumber));
    }

    @Getter
    private static class CardInfo {
        private String binNumber;
        private String lastFourNumber;
        /**
         * 落库字段 full_number：仅保存“前六****后四”
         */
        private String fullNumberToStore = "";
        /**
         * 仅用于计算 card_finger 的完整 PAN（不落库）
         */
        private String fullNumberForFinger = "";
        private boolean invalid;

        void setBinNumber(String binNumber) {
            this.binNumber = binNumber;
        }

        void setLastFourNumber(String lastFourNumber) {
            this.lastFourNumber = lastFourNumber;
        }

        void setFullNumberToStore(String fullNumberToStore) {
            this.fullNumberToStore = fullNumberToStore;
        }

        void setFullNumberForFinger(String fullNumberForFinger) {
            this.fullNumberForFinger = fullNumberForFinger;
        }

        boolean isInvalid() {
            return invalid;
        }
        void setInvalid(boolean invalid) {
            this.invalid = invalid;
        }
    }
}
