package com.games.pay.dto;

import lombok.Data;

import java.util.Date;

@Data
public class OrderStatusStatistics {
    /**
     * 日期 (yyyy-MM-dd格式)
     */
    private String date;
    
    /**
     * 状态码
     */
    private String status;
    private String type;

    /**
     * 订单数量
     */
    private Integer count;
}
