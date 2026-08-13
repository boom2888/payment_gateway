package com.games.web.task;

import com.games.thunes.ThunesConfigService;
import com.games.thunes.ThunesConfigTransactionService;
import com.games.thunes.ThunesPayerSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Thunes 配置同步定时任务
 *
 * @author System
 * @date 2025-01-09
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ThunesConfigSyncTask {

    private final ThunesConfigService thunesConfigService;
    private final ThunesConfigTransactionService thunesConfigTransactionService;
    private final ThunesPayerSyncService payerSyncService;


    /**
     * 每日凌晨2点执行完整同步（备用）
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void dailyFullSync() {
        log.info("开始执行 Thunes 配置每日完整同步");
        
        try {
            // 1. 完整同步 Payer 数据
            payerSyncService.fullSync();
            
            // 2. 完整同步配置数据
            thunesConfigService.manualSyncAllConfig();
            
            log.info("Thunes 配置每日完整同步执行完成");
            
        } catch (Exception e) {
            log.error("Thunes 配置每日完整同步执行失败", e);
        }
    }

}