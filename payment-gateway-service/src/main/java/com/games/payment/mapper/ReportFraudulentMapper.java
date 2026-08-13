package com.games.payment.mapper;

import com.games.payment.domain.ReportFraudulent;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 欺诈报告Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface ReportFraudulentMapper extends BaseMapper<ReportFraudulent> {
    /**
     * 查询欺诈报告列表
     *
     * @param reportFraudulent 欺诈报告
     * @return 欺诈报告集合
     */
    List<ReportFraudulent> selectAllList(ReportFraudulent reportFraudulent);

}
