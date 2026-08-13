package com.games.payment.mapper;

import com.games.payment.domain.ReportPayments;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
/**
 * 支付报告Mapper接口
 *
 * @author Ticker
 * @date 2025-07-09
 */
public interface ReportPaymentsMapper extends BaseMapper<ReportPayments> {
    /**
     * 查询支付报告列表
     *
     * @param reportPayments 支付报告
     * @return 支付报告集合
     */
    List<ReportPayments> selectAllList(ReportPayments reportPayments);

}
