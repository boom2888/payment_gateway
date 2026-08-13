package com.games.payment.service.executor;

import com.games.common.enums.MovementImportStatus;
import com.games.payment.domain.MovementData;
import com.games.payment.domain.MovementImport;
import com.games.payment.mapper.MovementImportMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * Handles long running movement import processing in the background.
 */
@Slf4j
@Component
public class MovementImportAsyncExecutor {

    @Resource
    private MovementImportMapper movementImportMapper;
    @Resource
    private MovementImportProcessingService movementImportProcessingService;

    /**
     * Run the heavy import processing asynchronously so the HTTP thread returns immediately.
     */
    @Async
    public void processImportDataAsync(Long movementImportId, List<MovementData> dataList) {
        log.info("========== 开始异步处理导入数据，报告ID: {}，数据量: {} ==========",
                movementImportId, dataList.size());
        long startTime = System.currentTimeMillis();

        try {
            movementImportProcessingService.processImportData(movementImportId, dataList);

            long duration = System.currentTimeMillis() - startTime;
            log.info("========== 异步处理完成，报告ID: {}，耗时: {} ms ({} 秒) ==========",
                    movementImportId, duration, duration / 1000.0);
        } catch (Throwable e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("========== 异步处理失败，报告ID: {}，耗时: {} ms，错误: {} ==========",
                    movementImportId, duration, e.getMessage(), e);

            MovementImport movementImport = new MovementImport();
            movementImport.setId(movementImportId);
            movementImport.setRemark(e.getMessage());
            movementImport.setStatus(MovementImportStatus.GENERATED_FAIL.ordinal());
            movementImportMapper.updateById(movementImport);
            // 重新抛出，确保调用方感知异常（尽管当前为 @Async 场景）
            throw e;
        }
    }

}
