package com.games.payment.service;

import com.games.payment.domain.ReportQuote;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 汇率报告Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface IReportQuoteService extends IService<ReportQuote> {

    /**
     * 查询汇率报告列表
     *
     * @param reportQuote 汇率报告
     * @return 汇率报告集合
     */
    List<ReportQuote> selectAllList(ReportQuote reportQuote);

    /**
     * 查询列表
     */
    List<ReportQuote> queryList(ReportQuote reportQuote);

}
