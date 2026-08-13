package com.games.pay.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class IndexOrderNumTrendVo {
    private Integer orderCount;
    private BigDecimal trend;
    // 添加金额相关字段
    private BigDecimal orderAmount;
    private BigDecimal amountTrend;
}
