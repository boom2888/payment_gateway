package com.games.payment.service;

import com.games.payment.domain.AcquirerReconBatchrun;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 收单机构对账批处理Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface IAcquirerReconBatchrunService extends IService<AcquirerReconBatchrun> {

    /**
     * 查询收单机构对账批处理列表
     *
     * @param acquirerReconBatchrun 收单机构对账批处理
     * @return 收单机构对账批处理集合
     */
    List<AcquirerReconBatchrun> selectAllList(AcquirerReconBatchrun acquirerReconBatchrun);

    /**
     * 查询列表
     */
    List<AcquirerReconBatchrun> queryList(AcquirerReconBatchrun acquirerReconBatchrun);

}
