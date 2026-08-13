package com.games.payment.service;

import com.games.payment.domain.ReportFraudulent;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 欺诈报告Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface IReportFraudulentService extends IService<ReportFraudulent> {

    /**
     * 查询欺诈报告列表
     *
     * @param reportFraudulent 欺诈报告
     * @return 欺诈报告集合
     */
    List<ReportFraudulent> selectAllList(ReportFraudulent reportFraudulent);

    /**
     * 查询列表
     */
    List<ReportFraudulent> queryList(ReportFraudulent reportFraudulent);

}
