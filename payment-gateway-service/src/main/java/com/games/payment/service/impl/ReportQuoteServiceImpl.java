package com.games.payment.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import com.games.payment.mapper.ReportQuoteMapper;
import com.games.payment.domain.ReportQuote;
import com.games.payment.service.IReportQuoteService;

import java.util.List;
import java.util.Map;

/**
 * 汇率报告Service业务层处理
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Service
public class ReportQuoteServiceImpl extends ServiceImpl<ReportQuoteMapper, ReportQuote> implements IReportQuoteService {

    /**
     * 查询汇率报告列表
     *
     * @param reportQuote 汇率报告
     * @return 汇率报告
     */
    @Override
    public List<ReportQuote> selectAllList(ReportQuote reportQuote)
    {
        return getBaseMapper().selectAllList(reportQuote);
    }


    @Override
    public List<ReportQuote> queryList(ReportQuote reportQuote) {
        LambdaQueryWrapper<ReportQuote> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(reportQuote.getFromCode())){
            lqw.eq(ReportQuote::getFromCode ,reportQuote.getFromCode());
        }
        if (reportQuote.getFromId() != null){
            lqw.eq(ReportQuote::getFromId ,reportQuote.getFromId());
        }
        if (StringUtils.isNotBlank(reportQuote.getToCode())){
            lqw.eq(ReportQuote::getToCode ,reportQuote.getToCode());
        }
        if (reportQuote.getToId() != null){
            lqw.eq(ReportQuote::getToId ,reportQuote.getToId());
        }
        if (reportQuote.getCreatedAt() != null){
            lqw.eq(ReportQuote::getCreatedAt ,reportQuote.getCreatedAt());
        }
        if (reportQuote.getUpdatedAt() != null){
            lqw.eq(ReportQuote::getUpdatedAt ,reportQuote.getUpdatedAt());
        }
        if (reportQuote.getQuote() != null){
            lqw.eq(ReportQuote::getQuote ,reportQuote.getQuote());
        }
        return this.list(lqw);
    }




}
