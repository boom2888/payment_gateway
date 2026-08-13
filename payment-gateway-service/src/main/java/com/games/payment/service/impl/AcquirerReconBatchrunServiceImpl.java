package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.AcquirerReconBatchrunMapper;
import com.games.payment.domain.AcquirerReconBatchrun;
import com.games.payment.service.IAcquirerReconBatchrunService;

import java.util.List;
import java.util.Map;

/**
 * 收单机构对账批处理Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class AcquirerReconBatchrunServiceImpl extends ServiceImpl<AcquirerReconBatchrunMapper, AcquirerReconBatchrun> implements IAcquirerReconBatchrunService {

    /**
     * 查询收单机构对账批处理列表
     *
     * @param acquirerReconBatchrun 收单机构对账批处理
     * @return 收单机构对账批处理
     */
    @Override
    public List<AcquirerReconBatchrun> selectAllList(AcquirerReconBatchrun acquirerReconBatchrun)
    {
        return getBaseMapper().selectAllList(acquirerReconBatchrun);
    }


    @Override
    public List<AcquirerReconBatchrun> queryList(AcquirerReconBatchrun acquirerReconBatchrun) {
        LambdaQueryWrapper<AcquirerReconBatchrun> lqw = Wrappers.lambdaQuery();
        if (acquirerReconBatchrun.getAcquirerId() != null){
            lqw.eq(AcquirerReconBatchrun::getAcquirerId ,acquirerReconBatchrun.getAcquirerId());
        }
        if (StringUtils.isNotBlank(acquirerReconBatchrun.getAcquirerBatchPayoutId())){
            lqw.eq(AcquirerReconBatchrun::getAcquirerBatchPayoutId ,acquirerReconBatchrun.getAcquirerBatchPayoutId());
        }
        if (acquirerReconBatchrun.getReconStart() != null){
            lqw.eq(AcquirerReconBatchrun::getReconStart ,acquirerReconBatchrun.getReconStart());
        }
        if (acquirerReconBatchrun.getReconEnd() != null){
            lqw.eq(AcquirerReconBatchrun::getReconEnd ,acquirerReconBatchrun.getReconEnd());
        }
        if (acquirerReconBatchrun.getTotalNumberOfOrder() != null){
            lqw.eq(AcquirerReconBatchrun::getTotalNumberOfOrder ,acquirerReconBatchrun.getTotalNumberOfOrder());
        }
        if (acquirerReconBatchrun.getMatchedNumberOfOrder() != null){
            lqw.eq(AcquirerReconBatchrun::getMatchedNumberOfOrder ,acquirerReconBatchrun.getMatchedNumberOfOrder());
        }
        if (acquirerReconBatchrun.getUnmatchedNumberOfOrder() != null){
            lqw.eq(AcquirerReconBatchrun::getUnmatchedNumberOfOrder ,acquirerReconBatchrun.getUnmatchedNumberOfOrder());
        }
        if (acquirerReconBatchrun.getCreatedAt() != null){
            lqw.eq(AcquirerReconBatchrun::getCreatedAt ,acquirerReconBatchrun.getCreatedAt());
        }
        if (acquirerReconBatchrun.getCreatedBy() != null){
            lqw.eq(AcquirerReconBatchrun::getCreatedBy ,acquirerReconBatchrun.getCreatedBy());
        }
        if (acquirerReconBatchrun.getDeletedAt() != null){
            lqw.eq(AcquirerReconBatchrun::getDeletedAt ,acquirerReconBatchrun.getDeletedAt());
        }
        if (acquirerReconBatchrun.getDeletedBy() != null){
            lqw.eq(AcquirerReconBatchrun::getDeletedBy ,acquirerReconBatchrun.getDeletedBy());
        }
        if (acquirerReconBatchrun.getDeleted() != null){
            lqw.eq(AcquirerReconBatchrun::getDeleted ,acquirerReconBatchrun.getDeleted());
        }
        if (acquirerReconBatchrun.getReconFileId() != null){
            lqw.eq(AcquirerReconBatchrun::getReconFileId ,acquirerReconBatchrun.getReconFileId());
        }
        if (acquirerReconBatchrun.getTotalCapturedAmount() != null){
            lqw.eq(AcquirerReconBatchrun::getTotalCapturedAmount ,acquirerReconBatchrun.getTotalCapturedAmount());
        }
        if (acquirerReconBatchrun.getTotalChargebackAmount() != null){
            lqw.eq(AcquirerReconBatchrun::getTotalChargebackAmount ,acquirerReconBatchrun.getTotalChargebackAmount());
        }
        if (acquirerReconBatchrun.getTotalRefundAmount() != null){
            lqw.eq(AcquirerReconBatchrun::getTotalRefundAmount ,acquirerReconBatchrun.getTotalRefundAmount());
        }
        if (acquirerReconBatchrun.getTotalAcquirerFeeAmount() != null){
            lqw.eq(AcquirerReconBatchrun::getTotalAcquirerFeeAmount ,acquirerReconBatchrun.getTotalAcquirerFeeAmount());
        }
        if (acquirerReconBatchrun.getTotalSettledAmount() != null){
            lqw.eq(AcquirerReconBatchrun::getTotalSettledAmount ,acquirerReconBatchrun.getTotalSettledAmount());
        }
        return this.list(lqw);
    }




}
