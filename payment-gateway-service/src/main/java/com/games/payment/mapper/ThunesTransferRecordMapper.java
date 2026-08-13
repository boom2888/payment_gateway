package com.games.payment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.games.payment.domain.ThunesTransferRecord;
import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Thunes 转账记录 Mapper 接口
 *
 * @author System
 * @date 2025-01-08
 */
@Mapper
public interface ThunesTransferRecordMapper extends BaseMapper<ThunesTransferRecord> {

        /**
         * 根据商户ID和外部订单号查询转账记录
         *
         * @param merchantId 商户ID
         * @param externalId 外部订单号
         * @return 转账记录
         */
        ThunesTransferRecord selectByMerchantIdAndExternalId(@Param("merchantId") Long merchantId,
                        @Param("externalId") String externalId);

        /**
         * 根据 Thunes 交易ID查询转账记录
         *
         * @param transactionId Thunes交易ID
         * @return 转账记录
         */
        ThunesTransferRecord selectByTransactionId(@Param("transactionId") String transactionId);

        /**
         * 更新转账状态
         *
         * @param id           记录ID
         * @param status       新状态
         * @param thunesStatus Thunes状态
         * @param failReason   失败原因（可选）
         * @return 更新行数
         */
        int updateStatus(@Param("id") Long id,
                        @Param("status") String status,
                        @Param("thunesStatus") String thunesStatus,
                        @Param("failReason") String failReason);

        /**
         * 查询转账记录列表
         *
         * @param thunesTransferRecord 转账记录
         * @return 转账记录集合
         */
        public List<ThunesTransferRecord> selectList(ThunesTransferRecord thunesTransferRecord);
}