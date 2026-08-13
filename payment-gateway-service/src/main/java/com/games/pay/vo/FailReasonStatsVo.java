package com.games.pay.vo;

import lombok.Data;

import java.util.List;

/**
 * 失败原因统计VO
 * 
 * @author system
 */
@Data
public class FailReasonStatsVo {
    
    /**
     * 失败原因统计列表
     */
    private List<FailReasonVo> failReasons;
    
    /**
     * 总失败订单数
     */
    private Integer totalFailedOrders;
    
    /**
     * 统计时间范围
     */
    private String timeRange;
    
    /**
     * 失败原因VO
     */
    @Data
    public static class FailReasonVo {
        /**
         * 失败原因类型名称
         */
        private String name;
        
        /**
         * 占比百分比
         */
        private Double value;
        
        /**
         * 订单数量
         */
        private Integer count;
        
        /**
         * 失败原因类型代码
         */
        private String type;
        
        /**
         * 完整的错误信息（用于tooltip显示）
         */
        private String fullName;
        
        public FailReasonVo() {}
        
        public FailReasonVo(String name, Double value, Integer count, String type) {
            this.name = name;
            this.value = value;
            this.count = count;
            this.type = type;
            this.fullName = name;
        }
        
        public FailReasonVo(String name, Double value, Integer count, String type, String fullName) {
            this.name = name;
            this.value = value;
            this.count = count;
            this.type = type;
            this.fullName = fullName;
        }
    }
}
