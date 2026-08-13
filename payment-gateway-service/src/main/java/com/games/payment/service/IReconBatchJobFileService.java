package com.games.payment.service;

import com.games.payment.domain.ReconBatchJobFile;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 对账批处理作业文件Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface IReconBatchJobFileService extends IService<ReconBatchJobFile> {

    /**
     * 查询对账批处理作业文件列表
     *
     * @param reconBatchJobFile 对账批处理作业文件
     * @return 对账批处理作业文件集合
     */
    List<ReconBatchJobFile> selectAllList(ReconBatchJobFile reconBatchJobFile);

    /**
     * 查询列表
     */
    List<ReconBatchJobFile> queryList(ReconBatchJobFile reconBatchJobFile);

}
