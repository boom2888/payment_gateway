package com.games.payment.service;

import com.games.payment.domain.ReportPayments;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 支付报告Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface IReportPaymentsService extends IService<ReportPayments> {

    /**
     * 查询支付报告列表
     *
     * @param reportPayments 支付报告
     * @return 支付报告集合
     */
    List<ReportPayments> selectAllList(ReportPayments reportPayments);

    /**
     * 查询列表
     */
    List<ReportPayments> queryList(ReportPayments reportPayments);

}
