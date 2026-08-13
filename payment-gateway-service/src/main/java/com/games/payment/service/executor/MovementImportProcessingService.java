package com.games.payment.service.executor;

import com.games.payment.domain.MovementData;
import com.games.payment.service.IMovementDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Wraps the transactional part of movement import processing so it can be reused safely.
 */
@Slf4j
@Service
public class MovementImportProcessingService {

    @Resource
    private IMovementDataService movementDataService;

    @Transactional(rollbackFor = Exception.class)
    public void processImportData(Long movementImportId, List<MovementData> dataList) {
        Pattern pattern = Pattern.compile("(\\d+)$");
        for (MovementData movementData : dataList) {
            if (movementData == null) {
                continue;
            }
            String clientUniqueId = movementData.getClientUniqueId();
            if (clientUniqueId != null) {
                Matcher matcher = pattern.matcher(clientUniqueId);
                if (matcher.find()) {
                    Long number = Long.parseLong(matcher.group(1));
                    movementData.setClientUniqueId(String.valueOf(number));
                } else if (clientUniqueId.isEmpty()) {
                    movementData.setClientUniqueId(null);
                }
            } else {
                movementData.setClientUniqueId(null);
            }
            movementData.setMovementImportId(movementImportId);
        }

        log.info("开始批量保存 {} 条 MovementData 记录", dataList.size());
        movementDataService.saveBatch(dataList);
        log.info("MovementData 保存完成");

        log.info("开始计算商户结算费用");
        movementDataService.calculateMerchantSettlementFee(movementImportId);
    }
}
