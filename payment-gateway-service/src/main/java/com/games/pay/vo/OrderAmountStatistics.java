package com.games.pay.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderAmountStatistics {
    /**
     * 日期 (yyyy-MM-dd格式)
     */
    private String date;
    private String type;

    /**
     * 状态码
     */
    private String status;

    /**
     * 订单金额
     */
    private BigDecimal amount;
}
