package com.games.payment.service;

import com.games.payment.domain.ReportDataRecord;
import com.baomidou.mybatisplus.spring.service.IService;
import java.util.List;

/**
 * 报告数据记录Service接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface IReportDataRecordService extends IService<ReportDataRecord> {

    /**
     * 查询报告数据记录列表
     *
     * @param reportDataRecord 报告数据记录
     * @return 报告数据记录集合
     */
    List<ReportDataRecord> selectAllList(ReportDataRecord reportDataRecord);

    /**
     * 查询列表
     */
    List<ReportDataRecord> queryList(ReportDataRecord reportDataRecord);

}
