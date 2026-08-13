package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.UploadCardRecordMapper;
import com.games.payment.domain.UploadCardRecord;
import com.games.payment.service.IUploadCardRecordService;

import java.util.List;
import java.util.Map;

/**
 * 上传卡记录Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class UploadCardRecordServiceImpl extends ServiceImpl<UploadCardRecordMapper, UploadCardRecord> implements IUploadCardRecordService {

    /**
     * 查询上传卡记录列表
     *
     * @param uploadCardRecord 上传卡记录
     * @return 上传卡记录
     */
    @Override
    public List<UploadCardRecord> selectAllList(UploadCardRecord uploadCardRecord)
    {
        return getBaseMapper().selectAllList(uploadCardRecord);
    }


    @Override
    public List<UploadCardRecord> queryList(UploadCardRecord uploadCardRecord) {
        LambdaQueryWrapper<UploadCardRecord> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(uploadCardRecord.getFileUrl())){
            lqw.eq(UploadCardRecord::getFileUrl ,uploadCardRecord.getFileUrl());
        }
        if (uploadCardRecord.getCreatedAt() != null){
            lqw.eq(UploadCardRecord::getCreatedAt ,uploadCardRecord.getCreatedAt());
        }
        if (uploadCardRecord.getState() != null){
            lqw.eq(UploadCardRecord::getState ,uploadCardRecord.getState());
        }
        if (uploadCardRecord.getType() != null){
            lqw.eq(UploadCardRecord::getType ,uploadCardRecord.getType());
        }
        return this.list(lqw);
    }




}
