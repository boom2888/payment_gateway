package com.games.payment.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.spring.service.IService;

import com.games.payment.domain.ThunesTransferRecord;

import java.util.List;

/**
 * Thunes 转账记录 Service 接口
 *
 * @author System
 * @date 2025-01-08
 */
public interface IThunesTransferRecordService extends IService<ThunesTransferRecord> {

    /**
     * 根据商户ID和外部订单号查询转账记录
     *
     * @param merchantId 商户ID
     * @param externalId 外部订单号
     * @return 转账记录
     */
    ThunesTransferRecord getByMerchantIdAndExternalId(Long merchantId, String externalId);

    /**
     * 根据 Thunes 交易ID查询转账记录
     *
     * @param transactionId Thunes交易ID
     * @return 转账记录
     */
    ThunesTransferRecord getByTransactionId(String transactionId);

    /**
     * 更新转账状态
     *
     * @param id           记录ID
     * @param status       新状态
     * @param thunesStatus Thunes状态
     * @param failReason   失败原因（可选）
     * @return 是否更新成功
     */
    boolean updateTransferStatus(Long id, String status, String thunesStatus, String failReason);

    /**
     * 创建转账记录
     *
     * @param record 转账记录
     * @return 是否创建成功
     */
    boolean createTransferRecord(ThunesTransferRecord record);

    /**
     * 根据状态查询转账记录
     *
     * @param status 状态
     * @return 查询条件构造器
     */
    LambdaQueryWrapper<ThunesTransferRecord> lambdaQueryByStatus(String status);

    /**
     * 查询转账记录列表
     *
     * @param record 转账记录
     * @return 转账记录集合
     */
    List<ThunesTransferRecord> selectList(ThunesTransferRecord record);
}