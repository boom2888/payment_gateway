package com.games.payment.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.games.payment.domain.MerchantAccountChangeLog;
import com.games.payment.mapper.MerchantAccountChangeLogMapper;
import com.games.payment.service.IMerchantAccountChangeLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * 商户资产变更记录Service业务层处理
 *
 * @author Ticker
 * @date 2025-11-27
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class MerchantAccountChangeLogServiceImpl
        extends ServiceImpl<MerchantAccountChangeLogMapper, MerchantAccountChangeLog>
        implements IMerchantAccountChangeLogService {

    @Resource
    private MerchantAccountChangeLogMapper merchantAccountChangeLogMapper;

    @Override
    public List<MerchantAccountChangeLog> selectByMovementImportId(Long movementImportId, Integer type) {
        if (movementImportId == null) {
            log.warn("根据 movementImportId 查询变更记录时，movementImportId 为空");
            return Collections.emptyList();
        }
        log.info("根据 movementImportId 查询变更记录，movementImportId: {}，type: {}", movementImportId, type);
        List<MerchantAccountChangeLog> result = merchantAccountChangeLogMapper
                .selectByMovementImportId(movementImportId, type);
        log.info("根据 movementImportId 查询到变更记录条数: {}", result == null ? 0 : result.size());
        return result;
    }

    @Override
    public void batchSave(List<MerchantAccountChangeLog> changeLogs) {
        if (changeLogs == null || changeLogs.isEmpty()) {
            log.warn("变更记录列表为空，跳过保存");
            return;
        }

        log.info("批量保存商户资产变更记录，共 {} 条", changeLogs.size());
        merchantAccountChangeLogMapper.batchInsert(changeLogs);
        log.info("商户资产变更记录保存完成");
    }
}
