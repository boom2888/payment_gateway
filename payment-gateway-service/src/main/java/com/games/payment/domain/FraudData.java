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
 * 欺诈数据对象 fraud_data
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("fraud_data")
public class FraudData  {

private static final long serialVersionUID=1L;


    /** 主键ID */
    @TableId(value = "id")
    private Long id;

    /** report_data_record表ID */
    @Excel(name = "report_data_record表ID")
    private Long uploadId;

    /** report_fraudulent表ID */
    @Excel(name = "report_fraudulent表ID")
    private Long reportFraudulentId;

    /** 每个欺诈订单的卡BIN */
    @Excel(name = "每个欺诈订单的卡BIN")
    private Long bin;

    /** 欺诈订单的日期 */
    @Excel(name = "欺诈订单的日期" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date issueDate;

    /** 欺诈金额，已转换为美元 */
    @Excel(name = "欺诈金额，已转换为美元")
    private BigDecimal usdAmount;

    /** 方法名(1-visa，2-mastercard) */
    @Excel(name = "方法名(1-visa，2-mastercard)")
    private Long methodName;

    /** 跟踪ID，订单表ID */
    @Excel(name = "跟踪ID，订单表ID")
    private Long trackId;

    /** 订单发生的日期 */
    @Excel(name = "订单发生的日期" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date transactionDate;

    /** 记录插入时间 */
    @Excel(name = "记录插入时间" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 更新时间 */
    @Excel(name = "更新时间" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    /** 订单表SSO订单ID */
    @Excel(name = "订单表SSO订单ID")
    private String ssoOrderId;

    /** 订单表商户ID */
    @Excel(name = "订单表商户ID")
    private Long merchantId;

    /** 订单表货币ID */
    @Excel(name = "订单表货币ID")
    private Long currencyId;

    /** 订单表法币金额 */
    @Excel(name = "订单表法币金额")
    private BigDecimal fiatAmount;

    /** 订单表状态 */
    @Excel(name = "订单表状态")
    private Long status;

    /** 订单表创建时间 */
    @Excel(name = "订单表创建时间" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date orderCreated;

    /** 订单表加密货币金额，订单美元金额 */
    @Excel(name = "订单表加密货币金额，订单美元金额")
    private BigDecimal cryptoAmount;

    /** 欺诈报告发卡国家 */
    @Excel(name = "欺诈报告发卡国家")
    private String issuerCountry;

}
