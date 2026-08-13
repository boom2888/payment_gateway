package com.games.payment.mapper;

import com.games.payment.domain.ReportDataRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 报告数据记录Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface ReportDataRecordMapper extends BaseMapper<ReportDataRecord> {
    /**
     * 查询报告数据记录列表
     *
     * @param reportDataRecord 报告数据记录
     * @return 报告数据记录集合
     */
    List<ReportDataRecord> selectAllList(ReportDataRecord reportDataRecord);

}
