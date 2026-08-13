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
 * 支付数据对象 payments_data
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("payments_data")
public class PaymentsData  {

private static final long serialVersionUID=1L;


    /** 主键ID */
    @TableId(value = "id")
    private Long id;

    /** 订单表商户ID */
    @Excel(name = "订单表商户ID")
    private Long merchantId;

    /** 是否为visa或mastercard欺诈（1：visa，2：mastercard） */
    @Excel(name = "是否为visa或mastercard欺诈" , readConverterExp = "1=：visa，2：mastercard")
    private Long methodName;

    /** 订单ID */
    @Excel(name = "订单ID")
    private Long orderId;

    /** 交易日期 */
    @Excel(name = "交易日期" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date;

    /** 上传文件ID */
    @Excel(name = "上传文件ID")
    private Long uploadId;

    /** 原始金额 */
    @Excel(name = "原始金额")
    private BigDecimal amount;

    /** 货币 */
    @Excel(name = "货币")
    private String currency;

    /** 交易结果 */
    @Excel(name = "交易结果")
    private String transactionResult;

    /** 每个欺诈订单的卡BIN */
    @Excel(name = "每个欺诈订单的卡BIN")
    private Long bin;

    /** 原因代码 */
    @Excel(name = "原因代码")
    private String reasonCode;

    /** 转换为欧元汇率 */
    @Excel(name = "转换为欧元汇率")
    private BigDecimal quote;

    /** 订单表SSO订单ID */
    @Excel(name = "订单表SSO订单ID")
    private String ssoOrderId;

    /** 订单表订单UUID */
    @Excel(name = "订单表订单UUID")
    private String orderUuid;

    /** 订单表货币ID */
    @Excel(name = "订单表货币ID")
    private Long currencyId;

    /** 欧元金额 */
    @Excel(name = "欧元金额")
    private BigDecimal finalAmount;

    /** 订单表创建时间 */
    @Excel(name = "订单表创建时间" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date orderCreated;

    /** 订单表加密货币金额，订单美元金额 */
    @Excel(name = "订单表加密货币金额，订单美元金额")
    private BigDecimal cryptoAmount;

    /** 订单状态（1：成功，2：失败） */
    @Excel(name = "订单状态" , readConverterExp = "1=：成功，2：失败")
    private Long orderStatus;

    /** 记录插入时间 */
    @Excel(name = "记录插入时间" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 更新时间 */
    @Excel(name = "更新时间" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    /** 交易ID */
    @Excel(name = "交易ID")
    private String transactionId;

    /** 卡发卡国家 */
    @Excel(name = "卡发卡国家")
    private String issuerCountry;

}
