package com.games.pay.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 每小时订单成功率统计VO
 * 
 * @author Generated
 */
@Data
public class HourlySuccessRateVo {
    /**
     * 日期时间列表（格式：MM-dd HH:00）
     */
    private List<String> hourLabels;
    
    /**
     * 成功率列表（百分比）
     */
    private List<BigDecimal> successRates;
    
    /**
     * 总订单数列表
     */
    private List<Integer> totalOrders;
    
    /**
     * 成功订单数列表
     */
    private List<Integer> successOrders;
    
    /**
     * 失败订单数列表
     */
    private List<Integer> failedOrders;
}

