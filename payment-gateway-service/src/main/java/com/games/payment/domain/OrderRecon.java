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
 * 订单对账对象 order_recon
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("order_recon")
public class OrderRecon  {

private static final long serialVersionUID=1L;


    /** 订单对账ID */
    @TableId(value = "id")
    private Long id;

    /** 对账批处理作业ID */
    @Excel(name = "对账批处理作业ID")
    private Long reconId;

    /** 订单ID */
    @Excel(name = "订单ID")
    private Long orderId;

    /** Checkout：支付ID */
    @Excel(name = "Checkout：支付ID")
    private String acquirerBatchPaymentId;

    /** 收单机构支付ID */
    @Excel(name = "收单机构支付ID")
    private String acquirerPaymentId;

    /** 注释 */
    @Excel(name = "注释")
    private String comment;

    /** 注意：0-未匹配，1-已匹配 */
    @Excel(name = "注意：0-未匹配，1-已匹配")
    private Long status;

    /** 记录创建时间 */
    @Excel(name = "记录创建时间" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 创建记录的用户ID */
    @Excel(name = "创建记录的用户ID")
    private Long createdBy;

    /** 记录删除时间 */
    @Excel(name = "记录删除时间" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deletedAt;

    /** 最后删除记录的用户ID */
    @Excel(name = "最后删除记录的用户ID")
    private Long deletedBy;

    /** 类型(0-默认，1-已删除) */
    @Excel(name = "类型(0-默认，1-已删除)")
    private Long deleted;

    /** 记录描述 */
    @Excel(name = "记录描述")
    private String remark;

}
