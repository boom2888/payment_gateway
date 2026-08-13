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
 * 账户对象 account
 *
 * @author Ticker
 * @date 2025-07-07
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("account")
public class Account  {

private static final long serialVersionUID=1L;


    /** 账户ID */
    @TableId(value = "id")
    private Long id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 账户名称 */
    @Excel(name = "账户名称")
    private String name;

    /** 账户信息的银行账户ID */
    @Excel(name = "账户信息的银行账户ID")
    private Long bankAccountId;

    /** 总余额 */
    @Excel(name = "总余额")
    private BigDecimal totalBalance;

    /** 可用余额 */
    @Excel(name = "可用余额")
    private BigDecimal availableBalance;

    /** 冻结余额 */
    @Excel(name = "冻结余额")
    private BigDecimal freezedBalance;

    /** 账户分类颜色 */
    @Excel(name = "账户分类颜色")
    private String color;

    /** 账户状态(0-激活，1-暂停，2-关闭) */
    @Excel(name = "账户状态(0-激活，1-暂停，2-关闭)")
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

    /** 交易对手ID */
    @Excel(name = "交易对手ID")
    private String counterpartyId;

    /** 交易对手状态 */
    @Excel(name = "交易对手状态")
    private String counterpartyState;

    /** 创建交易对手区分类型，1：revolut 2：GB 3：US 4：AUD 5：INR 6：EUR 7：SWIFT 8：SWIFTMAX 9：CARD */
    @Excel(name = "创建交易对手区分类型，1：revolut 2：GB 3：US 4：AUD 5：INR 6：EUR 7：SWIFT 8：SWIFTMAX 9：CARD")
    private Long counterpartyType;

    /** 第三方账户ID */
    @Excel(name = "第三方账户ID")
    private String internalBankAccountId;

}
