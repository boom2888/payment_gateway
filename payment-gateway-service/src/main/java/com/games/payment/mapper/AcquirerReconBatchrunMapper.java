package com.games.payment.mapper;

import com.games.payment.domain.AcquirerReconBatchrun;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 收单机构对账批处理Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface AcquirerReconBatchrunMapper extends BaseMapper<AcquirerReconBatchrun> {
    /**
     * 查询收单机构对账批处理列表
     *
     * @param acquirerReconBatchrun 收单机构对账批处理
     * @return 收单机构对账批处理集合
     */
    List<AcquirerReconBatchrun> selectAllList(AcquirerReconBatchrun acquirerReconBatchrun);

}
