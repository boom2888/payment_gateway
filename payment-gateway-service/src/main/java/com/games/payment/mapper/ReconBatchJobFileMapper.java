package com.games.payment.mapper;

import com.games.payment.domain.ReconBatchJobFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 对账批处理作业文件Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface ReconBatchJobFileMapper extends BaseMapper<ReconBatchJobFile> {
    /**
     * 查询对账批处理作业文件列表
     *
     * @param reconBatchJobFile 对账批处理作业文件
     * @return 对账批处理作业文件集合
     */
    List<ReconBatchJobFile> selectAllList(ReconBatchJobFile reconBatchJobFile);

}
