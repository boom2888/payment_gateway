package com.games.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.games.payment.domain.ThunesTransferRecord;
import com.games.payment.mapper.ThunesTransferRecordMapper;
import com.games.payment.service.IThunesTransferRecordService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Thunes 转账记录 Service 实现类
 *
 * @author System
 * @date 2025-01-08
 */
@Service
public class ThunesTransferRecordServiceImpl extends ServiceImpl<ThunesTransferRecordMapper, ThunesTransferRecord>
        implements IThunesTransferRecordService {

    @Override
    public ThunesTransferRecord getByMerchantIdAndExternalId(Long merchantId, String externalId) {
        return baseMapper.selectByMerchantIdAndExternalId(merchantId, externalId);
    }

    @Override
    public ThunesTransferRecord getByTransactionId(String transactionId) {
        return baseMapper.selectByTransactionId(transactionId);
    }

    @Override
    public boolean updateTransferStatus(Long id, String status, String thunesStatus, String failReason) {
        int result = baseMapper.updateStatus(id, status, thunesStatus, failReason);
        return result > 0;
    }

    @Override
    public boolean createTransferRecord(ThunesTransferRecord record) {
        if (record.getCreatedAt() == null) {
            record.setCreatedAt(new Date());
        }
        if (record.getUpdatedAt() == null) {
            record.setUpdatedAt(new Date());
        }
        return save(record);
    }

    @Override
    public LambdaQueryWrapper<ThunesTransferRecord> lambdaQueryByStatus(String status) {
        return new LambdaQueryWrapper<ThunesTransferRecord>()
                .eq(ThunesTransferRecord::getStatus, status);
    }

    @Override
    public List<ThunesTransferRecord> selectList(ThunesTransferRecord record) {
        return baseMapper.selectList(record);
    }
}