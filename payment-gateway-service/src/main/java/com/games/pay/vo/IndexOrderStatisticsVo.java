package com.games.pay.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class IndexOrderStatisticsVo {

    private Integer todayOrderCount;
    private BigDecimal todayTotalAmount;
    private BigDecimal yesterdayTotalAmount;
    private BigDecimal last7DaysTotalAmount;
}
