package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.ReconBatchJobFileMapper;
import com.games.payment.domain.ReconBatchJobFile;
import com.games.payment.service.IReconBatchJobFileService;

import java.util.List;
import java.util.Map;

/**
 * 对账批处理作业文件Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class ReconBatchJobFileServiceImpl extends ServiceImpl<ReconBatchJobFileMapper, ReconBatchJobFile> implements IReconBatchJobFileService {

    /**
     * 查询对账批处理作业文件列表
     *
     * @param reconBatchJobFile 对账批处理作业文件
     * @return 对账批处理作业文件
     */
    @Override
    public List<ReconBatchJobFile> selectAllList(ReconBatchJobFile reconBatchJobFile)
    {
        return getBaseMapper().selectAllList(reconBatchJobFile);
    }


    @Override
    public List<ReconBatchJobFile> queryList(ReconBatchJobFile reconBatchJobFile) {
        LambdaQueryWrapper<ReconBatchJobFile> lqw = Wrappers.lambdaQuery();
        if (reconBatchJobFile.getUploadTime() != null){
            lqw.eq(ReconBatchJobFile::getUploadTime ,reconBatchJobFile.getUploadTime());
        }
        if (StringUtils.isNotBlank(reconBatchJobFile.getFileUrl())){
            lqw.eq(ReconBatchJobFile::getFileUrl ,reconBatchJobFile.getFileUrl());
        }
        if (reconBatchJobFile.getStatus() != null){
            lqw.eq(ReconBatchJobFile::getStatus ,reconBatchJobFile.getStatus());
        }
        if (reconBatchJobFile.getCreatedAt() != null){
            lqw.eq(ReconBatchJobFile::getCreatedAt ,reconBatchJobFile.getCreatedAt());
        }
        if (reconBatchJobFile.getCreatedBy() != null){
            lqw.eq(ReconBatchJobFile::getCreatedBy ,reconBatchJobFile.getCreatedBy());
        }
        if (reconBatchJobFile.getDeletedAt() != null){
            lqw.eq(ReconBatchJobFile::getDeletedAt ,reconBatchJobFile.getDeletedAt());
        }
        if (reconBatchJobFile.getDeletedBy() != null){
            lqw.eq(ReconBatchJobFile::getDeletedBy ,reconBatchJobFile.getDeletedBy());
        }
        if (reconBatchJobFile.getDeleted() != null){
            lqw.eq(ReconBatchJobFile::getDeleted ,reconBatchJobFile.getDeleted());
        }
        if (reconBatchJobFile.getAcquirerId() != null){
            lqw.eq(ReconBatchJobFile::getAcquirerId ,reconBatchJobFile.getAcquirerId());
        }
        return this.list(lqw);
    }




}
