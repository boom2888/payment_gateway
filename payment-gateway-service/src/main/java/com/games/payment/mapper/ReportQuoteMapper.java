package com.games.payment.mapper;

import com.games.payment.domain.ReportQuote;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 汇率报告Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface ReportQuoteMapper extends BaseMapper<ReportQuote> {
    /**
     * 查询汇率报告列表
     *
     * @param reportQuote 汇率报告
     * @return 汇率报告集合
     */
    List<ReportQuote> selectAllList(ReportQuote reportQuote);

}
