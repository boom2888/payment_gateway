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
 * 加密货币交易对象 crypto_transaction
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("crypto_transaction")
public class CryptoTransaction  {

private static final long serialVersionUID=1L;


    /** 加密货币ID */
    @TableId(value = "id")
    private Long id;

    /** 订单ID */
    @Excel(name = "订单ID")
    private Long orderId;

    /** 网络交易ID */
    @Excel(name = "网络交易ID")
    private String transactionId;

    /** 交易哈希 */
    @Excel(name = "交易哈希")
    private String transactionHash;

    /** 加密货币数量 */
    @Excel(name = "加密货币数量")
    private BigDecimal cryptoAmount;

    /** 网络费用 */
    @Excel(name = "网络费用")
    private BigDecimal networkFee;

    /** 来源钱包地址 */
    @Excel(name = "来源钱包地址")
    private String fromWallet;

    /** 目标钱包地址 */
    @Excel(name = "目标钱包地址")
    private String toWallet;

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
