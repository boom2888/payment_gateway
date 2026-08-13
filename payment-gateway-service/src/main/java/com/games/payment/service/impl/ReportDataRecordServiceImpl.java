package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.ReportDataRecordMapper;
import com.games.payment.domain.ReportDataRecord;
import com.games.payment.service.IReportDataRecordService;

import java.util.List;
import java.util.Map;

/**
 * 报告数据记录Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class ReportDataRecordServiceImpl extends ServiceImpl<ReportDataRecordMapper, ReportDataRecord> implements IReportDataRecordService {

    /**
     * 查询报告数据记录列表
     *
     * @param reportDataRecord 报告数据记录
     * @return 报告数据记录
     */
    @Override
    public List<ReportDataRecord> selectAllList(ReportDataRecord reportDataRecord)
    {
        return getBaseMapper().selectAllList(reportDataRecord);
    }


    @Override
    public List<ReportDataRecord> queryList(ReportDataRecord reportDataRecord) {
        LambdaQueryWrapper<ReportDataRecord> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(reportDataRecord.getReference())){
            lqw.eq(ReportDataRecord::getReference ,reportDataRecord.getReference());
        }
        if (reportDataRecord.getType() != null){
            lqw.eq(ReportDataRecord::getType ,reportDataRecord.getType());
        }
        if (StringUtils.isNotBlank(reportDataRecord.getUrl())){
            lqw.eq(ReportDataRecord::getUrl ,reportDataRecord.getUrl());
        }
        if (reportDataRecord.getLineCount() != null){
            lqw.eq(ReportDataRecord::getLineCount ,reportDataRecord.getLineCount());
        }
        if (reportDataRecord.getStatus() != null){
            lqw.eq(ReportDataRecord::getStatus ,reportDataRecord.getStatus());
        }
        if (reportDataRecord.getCreatedTime() != null){
            lqw.eq(ReportDataRecord::getCreatedTime ,reportDataRecord.getCreatedTime());
        }
        return this.list(lqw);
    }




}
