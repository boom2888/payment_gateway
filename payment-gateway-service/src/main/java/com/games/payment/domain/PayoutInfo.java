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
 * 支付信息对象 payout_info
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("payout_info")
public class PayoutInfo  {

private static final long serialVersionUID=1L;


    /** 主键ID */
    @TableId(value = "id")
    private Long id;

    /** 订单表ID */
    @Excel(name = "订单表ID")
    private Long orderId;

    /** 欧元处理费用 */
    @Excel(name = "欧元处理费用")
    private BigDecimal processingFee;

    /** 渠道自动扣除商户金额 */
    @Excel(name = "渠道自动扣除商户金额")
    private BigDecimal deductPayoutAmount;

    /** 渠道自动扣除商户费用 */
    @Excel(name = "渠道自动扣除商户费用")
    private BigDecimal deductFee;

    /** 渠道API汇率 */
    @Excel(name = "渠道API汇率")
    private BigDecimal deductRate;

    /** 商户账户货币ID */
    @Excel(name = "商户账户货币ID")
    private Long merchantCurrencyId;

    /** 记录插入时间 */
    @Excel(name = "记录插入时间" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 更新时间 */
    @Excel(name = "更新时间" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

}
