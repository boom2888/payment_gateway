package com.games.payment.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import com.games.common.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;


import java.math.BigDecimal;
import java.util.Date;

/**
 * Nuvei争议数据对象 nuvie_dispute_data
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("nuvie_dispute_data")
public class NuvieDisputeData  {

private static final long serialVersionUID=1L;


    /** 主键ID */
    @TableId(value = "id")
    private Long id;

    /** 订单表商户ID */
    @Excel(name = "订单表商户ID")
    private Long merchantId;

    /** 是否为visa或mastercard欺诈(1-visa，2-mastercard) */
    @Excel(name = "是否为visa或mastercard欺诈(1-visa，2-mastercard)")
    private Long methodName;

    /** 记录插入时间 */
    @Excel(name = "记录插入时间" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 更新时间 */
    @Excel(name = "更新时间" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

}
