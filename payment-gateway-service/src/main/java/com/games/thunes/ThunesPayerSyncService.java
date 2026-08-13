package com.games.thunes;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.games.payment.domain.ThunesPayer;
import com.games.payment.service.IThunesPayerService;
import com.games.thunes.dto.PayerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Thunes Payer 同步服务
 * 负责从 Thunes API 同步 Payer 数据到本地数据库
 *
 * @author System
 * @date 2025-01-09
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ThunesPayerSyncService {

    private final ThunesApiService thunesApiService;
    private final IThunesPayerService payerService;

    /**
     * 执行全量同步
     */
    @Transactional(rollbackFor = Exception.class)
    public void fullSync() {
        long startTime = System.currentTimeMillis();
        
        try {
            log.info("开始执行 Thunes Payer 全量同步");
            
            // 调用 API 获取所有 Payer 数据
            List<PayerDto> apiPayers = thunesApiService.getPayers(null, null);
            
            if (apiPayers.isEmpty()) {
                log.warn("API 返回的 Payer 数据为空");
                return;
            }
            
            log.info("获取到 {} 条 Payer 数据，开始处理", apiPayers.size());
            
            // 获取现有的 Payer 数据
            List<ThunesPayer> existingPayers = payerService.list();
            Map<Long, ThunesPayer> existingPayerMap = existingPayers.stream()
                    .collect(Collectors.toMap(ThunesPayer::getId, p -> p));
            
            // 转换并分类处理
            List<ThunesPayer> newPayers = new ArrayList<>();
            List<ThunesPayer> updatedPayers = new ArrayList<>();
            int successCount = 0;
            int failedCount = 0;
            
            for (PayerDto apiPayer : apiPayers) {
                try {
                    ThunesPayer thunesPayer = payerService.convertFromDto(apiPayer);
                    
                    if (existingPayerMap.containsKey(apiPayer.getId())) {
                        // 更新现有记录
                        ThunesPayer existing = existingPayerMap.get(apiPayer.getId());
                        thunesPayer.setCreatedAt(existing.getCreatedAt()); // 保持原创建时间
                        updatedPayers.add(thunesPayer);
                    } else {
                        // 新增记录
                        newPayers.add(thunesPayer);
                    }
                    successCount++;
                } catch (Exception e) {
                    log.error("处理 Payer 数据失败 - ID: {}, 名称: {}", apiPayer.getId(), apiPayer.getName(), e);
                    failedCount++;
                }
            }
            
            // 批量保存新增和更新的数据
            if (!newPayers.isEmpty()) {
                payerService.saveBatch(newPayers, 1000);
                log.info("新增 Payer 记录: {} 条", newPayers.size());
            }
            
            if (!updatedPayers.isEmpty()) {
                payerService.updateBatchById(updatedPayers, 1000);
                log.info("更新 Payer 记录: {} 条", updatedPayers.size());
            }
            
            // 标记不存在的记录为非活跃状态
            Set<Long> apiPayerIds = apiPayers.stream().map(PayerDto::getId).collect(Collectors.toSet());
            int deactivatedCount = deactivateRemovedPayers(apiPayerIds);
            
            long duration = System.currentTimeMillis() - startTime;
            
            log.info("Thunes Payer 全量同步完成 - 总数: {}, 成功: {}, 失败: {}, 新增: {}, 更新: {}, 停用: {}, 耗时: {}ms", 
                    apiPayers.size(), successCount, failedCount, newPayers.size(), updatedPayers.size(), deactivatedCount, duration);
            
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("Thunes Payer 全量同步异常，耗时: {}ms", duration, e);
            throw e;
        }
    }

    /**
     * 停用已删除的 Payers
     */
    private int deactivateRemovedPayers(Set<Long> activePayerIds) {
        if (activePayerIds.isEmpty()) {
            return 0;
        }
        
        LambdaQueryWrapper<ThunesPayer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ThunesPayer::getIsActive, true)
               .notIn(ThunesPayer::getId, activePayerIds);
        
        List<ThunesPayer> toDeactivate = payerService.list(wrapper);
        if (toDeactivate.isEmpty()) {
            return 0;
        }
        
        // 批量更新为非活跃状态
        toDeactivate.forEach(payer -> {
            payer.setIsActive(false);
            payer.setLastSyncAt(LocalDateTime.now());
        });
        
        payerService.updateBatchById(toDeactivate, 1000);
        
        log.info("停用已删除的 Payer 记录: {} 条", toDeactivate.size());
        return toDeactivate.size();
    }

    /**
     * 获取 Payers（优先从数据库查询，API 作为备用）
     */
    public List<PayerDto> getPayersWithFallback(String countryCode, String currency) {
        try {
            // 优先从数据库查询
            List<ThunesPayer> dbPayers;
            
            if (countryCode != null && currency != null) {
                dbPayers = payerService.getPayersByCountryAndCurrency(countryCode, currency);
            } else if (countryCode != null) {
                dbPayers = payerService.getPayersByCountry(countryCode);
            } else if (currency != null) {
                dbPayers = payerService.getPayersByCurrency(currency);
            } else {
                dbPayers = payerService.getAllActivePayers();
            }
            
            // 如果数据库有数据，直接返回
            if (!dbPayers.isEmpty()) {
                log.debug("从数据库获取 Payer 数据: {} 条 - 国家: {}, 货币: {}", 
                         dbPayers.size(), countryCode, currency);
                return dbPayers.stream()
                        .map(payerService::convertToDto)
                        .collect(Collectors.toList());
            }
            
            // 数据库无数据，回退到 API 调用
            log.warn("数据库中无 Payer 数据，回退到 API 调用 - 国家: {}, 货币: {}", countryCode, currency);
            return thunesApiService.getPayers(countryCode, currency);
            
        } catch (Exception e) {
            log.error("获取 Payer 数据失败，回退到 API 调用 - 国家: {}, 货币: {}", countryCode, currency, e);
            return thunesApiService.getPayers(countryCode, currency);
        }
    }

    /**
     * 检查数据库中是否有 Payer 数据
     */
    public boolean hasPayerData() {
        return payerService.count() > 0;
    }


    /**
     * 获取最后同步时间（从 Payer 表中获取）
     */
    public LocalDateTime getLastSyncTime() {
        LambdaQueryWrapper<ThunesPayer> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(ThunesPayer::getLastSyncAt)
               .select(ThunesPayer::getLastSyncAt)
               .last("LIMIT 1");
        
        ThunesPayer lastPayer = payerService.getOne(wrapper);
        return lastPayer != null ? lastPayer.getLastSyncAt() : null;
    }

    /**
     * 获取同步统计信息
     */
    public PayerSyncStats getSyncStats() {
        PayerSyncStats stats = new PayerSyncStats();
        
        // 总 Payer 数量
        stats.setTotalPayerCount((long) payerService.count());
        
        // 活跃 Payer 数量
        LambdaQueryWrapper<ThunesPayer> activeWrapper = new LambdaQueryWrapper<>();
        activeWrapper.eq(ThunesPayer::getIsActive, true);
        stats.setActivePayerCount((long) payerService.count(activeWrapper));
        
        // 最后同步时间
        stats.setLastSyncTime(getLastSyncTime());
        
        return stats;
    }

    /**
     * Payer 同步统计信息
     */
    public static class PayerSyncStats {
        private Long totalPayerCount;
        private Long activePayerCount;
        private LocalDateTime lastSyncTime;

        // Getters and Setters
        public Long getTotalPayerCount() { return totalPayerCount; }
        public void setTotalPayerCount(Long totalPayerCount) { this.totalPayerCount = totalPayerCount; }

        public Long getActivePayerCount() { return activePayerCount; }
        public void setActivePayerCount(Long activePayerCount) { this.activePayerCount = activePayerCount; }

        public LocalDateTime getLastSyncTime() { return lastSyncTime; }
        public void setLastSyncTime(LocalDateTime lastSyncTime) { this.lastSyncTime = lastSyncTime; }
    }
}