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
 * 费用类型对象 fee_type
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("fee_type")
public class FeeType  {

private static final long serialVersionUID=1L;


    /** 费用类型ID */
    @TableId(value = "id")
    private Long id;

    /** 费用类型：注册、创建账户、月度订阅、入金交易、出金交易 */
    @Excel(name = "费用类型：注册、创建账户、月度订阅、入金交易、出金交易")
    private String name;

    /** 固定金额和百分比适用状态(0-两者，1-固定金额，2-百分比) */
    @Excel(name = "固定金额和百分比适用状态(0-两者，1-固定金额，2-百分比)")
    private Long type;

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

    /** 风险等级 */
    @Excel(name = "风险等级")
    private Long riskLevel;

    /** 交易类型 */
    @Excel(name = "交易类型")
    private Long transactionType;

}
