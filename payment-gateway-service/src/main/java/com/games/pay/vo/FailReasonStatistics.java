package com.games.pay.vo;

import lombok.Data;

/**
 * 失败原因统计数据
 * 
 * @author system
 */
@Data
public class FailReasonStatistics {
    
    /**
     * 失败原因
     */
    private String failReason;
    
    /**
     * 订单数量
     */
    private Integer count;
}
