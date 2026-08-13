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
 * 交易对象 transaction
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("transaction")
public class Transaction  {

private static final long serialVersionUID=1L;


    /** 交易ID */
    @TableId(value = "id")
    private Long id;

    /** 订单ID */
    @Excel(name = "订单ID")
    private Long orderId;

    /** 账户信息的账户ID */
    @Excel(name = "账户信息的账户ID")
    private Long accountId;

    /** 交易货币代码 */
    @Excel(name = "交易货币代码")
    private String currencyCode;

    /** 交易对手受益人姓名 */
    @Excel(name = "交易对手受益人姓名")
    private String cpName;

    /** 交易对手邮箱 */
    @Excel(name = "交易对手邮箱")
    private String cpEmail;

    /** 交易对手银行名称 */
    @Excel(name = "交易对手银行名称")
    private String cpBankName;

    /** 交易对手排序代码 */
    @Excel(name = "交易对手排序代码")
    private String cpSortCode;

    /** 交易对手账户名称 */
    @Excel(name = "交易对手账户名称")
    private String cpAccountName;

    /** 交易对手账户号码 */
    @Excel(name = "交易对手账户号码")
    private String cpAccountNumber;

    /** 交易对手国际银行账户号码 */
    @Excel(name = "交易对手国际银行账户号码")
    private String cpIban;

    /** 交易对手BIC Swift */
    @Excel(name = "交易对手BIC Swift")
    private String cpBicSwift;

    /** 交易类型(0-客户，1-费用，2-收单机构结算交易) */
    @Excel(name = "交易类型(0-客户，1-费用，2-收单机构结算交易)")
    private Long type;

    /** 交易货币金额 */
    @Excel(name = "交易货币金额")
    private BigDecimal amount;

    /** 交易费用明细（JSON格式） */
    @Excel(name = "交易费用明细" , readConverterExp = "J=SON格式")
    private String feeDetail;

    /** 交易费用金额 */
    @Excel(name = "交易费用金额")
    private BigDecimal feeAmount;

    /** 入金或出金(0-入金，1-出金) */
    @Excel(name = "入金或出金(0-入金，1-出金)")
    private Long inOut;

    /** 交易状态(0-待处理，1-已完成，2-就绪) */
    @Excel(name = "交易状态(0-待处理，1-已完成，2-就绪)")
    private Long status;

    /** 交易结果注释 */
    @Excel(name = "交易结果注释")
    private String comment;

    /** 内部交易类型(0-外部，1-内部) */
    @Excel(name = "内部交易类型(0-外部，1-内部)")
    private Long internal;

    /** 客户输入的交易描述 */
    @Excel(name = "客户输入的交易描述")
    private String description;

    /** 风险等级ID */
    @Excel(name = "风险等级ID")
    private Long riskLevel;

    /** 记录创建时间 */
    @Excel(name = "记录创建时间" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 创建记录的用户ID */
    @Excel(name = "创建记录的用户ID")
    private Long createdBy;

}
