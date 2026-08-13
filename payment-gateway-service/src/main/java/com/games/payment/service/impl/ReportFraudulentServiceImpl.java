package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.ReportFraudulentMapper;
import com.games.payment.domain.ReportFraudulent;
import com.games.payment.service.IReportFraudulentService;

import java.util.List;
import java.util.Map;

/**
 * 欺诈报告Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class ReportFraudulentServiceImpl extends ServiceImpl<ReportFraudulentMapper, ReportFraudulent> implements IReportFraudulentService {

    /**
     * 查询欺诈报告列表
     *
     * @param reportFraudulent 欺诈报告
     * @return 欺诈报告
     */
    @Override
    public List<ReportFraudulent> selectAllList(ReportFraudulent reportFraudulent)
    {
        return getBaseMapper().selectAllList(reportFraudulent);
    }


    @Override
    public List<ReportFraudulent> queryList(ReportFraudulent reportFraudulent) {
        LambdaQueryWrapper<ReportFraudulent> lqw = Wrappers.lambdaQuery();
        if (reportFraudulent.getUploadId() != null){
            lqw.eq(ReportFraudulent::getUploadId ,reportFraudulent.getUploadId());
        }
        if (reportFraudulent.getIssueDate() != null){
            lqw.eq(ReportFraudulent::getIssueDate ,reportFraudulent.getIssueDate());
        }
        if (reportFraudulent.getUsdAmount() != null){
            lqw.eq(ReportFraudulent::getUsdAmount ,reportFraudulent.getUsdAmount());
        }
        if (reportFraudulent.getMethodName() != null){
            lqw.like(ReportFraudulent::getMethodName ,reportFraudulent.getMethodName());
        }
        if (reportFraudulent.getBin() != null){
            lqw.eq(ReportFraudulent::getBin ,reportFraudulent.getBin());
        }
        if (reportFraudulent.getTrackId() != null){
            lqw.eq(ReportFraudulent::getTrackId ,reportFraudulent.getTrackId());
        }
        if (reportFraudulent.getTransactionDate() != null){
            lqw.eq(ReportFraudulent::getTransactionDate ,reportFraudulent.getTransactionDate());
        }
        if (reportFraudulent.getStatus() != null){
            lqw.eq(ReportFraudulent::getStatus ,reportFraudulent.getStatus());
        }
        if (StringUtils.isNotBlank(reportFraudulent.getFailReason())){
            lqw.eq(ReportFraudulent::getFailReason ,reportFraudulent.getFailReason());
        }
        if (reportFraudulent.getCreatedAt() != null){
            lqw.eq(ReportFraudulent::getCreatedAt ,reportFraudulent.getCreatedAt());
        }
        if (reportFraudulent.getUpdatedAt() != null){
            lqw.eq(ReportFraudulent::getUpdatedAt ,reportFraudulent.getUpdatedAt());
        }
        if (StringUtils.isNotBlank(reportFraudulent.getIssuerCountry())){
            lqw.eq(ReportFraudulent::getIssuerCountry ,reportFraudulent.getIssuerCountry());
        }
        return this.list(lqw);
    }




}
