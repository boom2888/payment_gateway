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
 * 订阅费用对象 subscription_fee
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("subscription_fee")
public class SubscriptionFee  {

private static final long serialVersionUID=1L;


    /** 订阅模型 */
    @TableId(value = "id")
    private Long id;

    /** 订阅模型ID */
    @Excel(name = "订阅模型ID")
    private Long subscriptionModelId;

    /** 订阅级别：默认，高级 */
    @Excel(name = "订阅级别：默认，高级")
    private Long feeTypeId;

    /** 固定金额 */
    @Excel(name = "固定金额")
    private BigDecimal fixedAmount;

    /** 百分比 */
    @Excel(name = "百分比")
    private BigDecimal percentage;

    /** 应用选项(0-两者，1-最小值，2-最大值) */
    @Excel(name = "应用选项(0-两者，1-最小值，2-最大值)")
    private Long option;

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
