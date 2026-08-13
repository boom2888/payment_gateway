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
 * 收款方列对象 emi_payee
 *
 * @author Ticker
 * @date 2025-10-16
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("emi_payee")
public class EmiPayee  {

private static final long serialVersionUID=1L;


    /** id */
    @TableId(value = "id")
    private Long id;

    /** 账号 */
    @Excel(name = "账号")
    private String account;

    /** 开户银行 */
    @Excel(name = "开户银行")
    private String bankOfDeposit;

    /** 开户名 */
    @Excel(name = "开户名")
    private String accountName;

    /** 账户类型 */
    @Excel(name = "账户类型")
    private String accountType;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 用途 */
    @Excel(name = "用途")
    private String use;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

}
