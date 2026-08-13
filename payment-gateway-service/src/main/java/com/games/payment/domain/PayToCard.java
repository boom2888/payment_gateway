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
 * 支付到卡对象 pay_to_card
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("pay_to_card")
public class PayToCard  {

private static final long serialVersionUID=1L;


    /** 主键ID */
    @TableId(value = "id")
    private Long id;

    /** 发送方 */
    @Excel(name = "发送方")
    private Long userId;

    /** 货币ID */
    @Excel(name = "货币ID")
    private Long currencyId;

    /** 收款人姓名 */
    @Excel(name = "收款人姓名")
    private String counterpartyName;

    /** 交易对手ID */
    @Excel(name = "交易对手ID")
    private String counterpartyId;

    /** 链接状态可能的值：[已创建，失败，等待中，激活，过期，已取消，处理中，已处理] */
    @Excel(name = "链接状态可能的值：[已创建，失败，等待中，激活，过期，已取消，处理中，已处理]")
    private String state;

    /** 创建时间 */
    @Excel(name = "创建时间" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 支付到卡链接ID */
    @Excel(name = "支付到卡链接ID")
    private String linkId;

    /** 账户ID */
    @Excel(name = "账户ID")
    private String accountId;

}
