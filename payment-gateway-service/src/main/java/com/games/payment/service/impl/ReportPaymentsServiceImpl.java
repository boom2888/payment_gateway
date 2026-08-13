package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.ReportPaymentsMapper;
import com.games.payment.domain.ReportPayments;
import com.games.payment.service.IReportPaymentsService;

import java.util.List;
import java.util.Map;

/**
 * 支付报告Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class ReportPaymentsServiceImpl extends ServiceImpl<ReportPaymentsMapper, ReportPayments> implements IReportPaymentsService {

    /**
     * 查询支付报告列表
     *
     * @param reportPayments 支付报告
     * @return 支付报告
     */
    @Override
    public List<ReportPayments> selectAllList(ReportPayments reportPayments)
    {
        return getBaseMapper().selectAllList(reportPayments);
    }


    @Override
    public List<ReportPayments> queryList(ReportPayments reportPayments) {
        LambdaQueryWrapper<ReportPayments> lqw = Wrappers.lambdaQuery();
        if (reportPayments.getUploadId() != null){
            lqw.eq(ReportPayments::getUploadId ,reportPayments.getUploadId());
        }
        if (reportPayments.getBin() != null){
            lqw.eq(ReportPayments::getBin ,reportPayments.getBin());
        }
        if (reportPayments.getReference() != null){
            lqw.eq(ReportPayments::getReference ,reportPayments.getReference());
        }
        if (reportPayments.getMethodName() != null){
            lqw.like(ReportPayments::getMethodName ,reportPayments.getMethodName());
        }
        if (reportPayments.getStatus() != null){
            lqw.eq(ReportPayments::getStatus ,reportPayments.getStatus());
        }
        if (reportPayments.getCreatedAt() != null){
            lqw.eq(ReportPayments::getCreatedAt ,reportPayments.getCreatedAt());
        }
        if (reportPayments.getUpdatedAt() != null){
            lqw.eq(ReportPayments::getUpdatedAt ,reportPayments.getUpdatedAt());
        }
        if (StringUtils.isNotBlank(reportPayments.getIssuerCountry())){
            lqw.eq(ReportPayments::getIssuerCountry ,reportPayments.getIssuerCountry());
        }
        if (StringUtils.isNotBlank(reportPayments.getFailReason())){
            lqw.eq(ReportPayments::getFailReason ,reportPayments.getFailReason());
        }
        if (StringUtils.isNotBlank(reportPayments.getActionType())){
            lqw.eq(ReportPayments::getActionType ,reportPayments.getActionType());
        }
        return this.list(lqw);
    }




}
