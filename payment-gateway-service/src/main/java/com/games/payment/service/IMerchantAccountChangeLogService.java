package com.games.payment.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.games.payment.domain.MerchantAccountChangeLog;

import java.util.List;

/**
 * 商户资产变更记录Service接口
 *
 * @author Ticker
 * @date 2025-11-27
 */
public interface IMerchantAccountChangeLogService extends IService<MerchantAccountChangeLog> {


    List<MerchantAccountChangeLog> selectByMovementImportId(Long movementImportId, Integer type);

    /**
     * 批量保存变更记录
     *
     * @param changeLogs 变更记录列表
     */
    void batchSave(List<MerchantAccountChangeLog> changeLogs);
}
