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
 * 欺诈报告对象 report_fraudulent
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("report_fraudulent")
public class ReportFraudulent  {

private static final long serialVersionUID=1L;


    /** 主键ID */
    @TableId(value = "id")
    private Long id;

    /** 上传文件ID */
    @Excel(name = "上传文件ID")
    private Long uploadId;

    /** 欺诈订单的日期 */
    @Excel(name = "欺诈订单的日期" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date issueDate;

    /** 欺诈金额，已转换为美元 */
    @Excel(name = "欺诈金额，已转换为美元")
    private BigDecimal usdAmount;

    /** 是否为visa或mastercard欺诈，1：visa，2：mastercard */
    @Excel(name = "是否为visa或mastercard欺诈，1：visa，2：mastercard")
    private Long methodName;

    /** 每个欺诈订单的卡BIN */
    @Excel(name = "每个欺诈订单的卡BIN")
    private Long bin;

    /** 订单ID */
    @Excel(name = "订单ID")
    private Long trackId;

    /** 订单发生的日期 */
    @Excel(name = "订单发生的日期" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date transactionDate;

    /** 1：成功，2：失败 */
    @Excel(name = "1：成功，2：失败")
    private Long status;

    /** 失败原因 */
    @Excel(name = "失败原因")
    private String failReason;

    /** 记录插入时间 */
    @Excel(name = "记录插入时间" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 更新时间 */
    @Excel(name = "更新时间" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    /** 卡发卡国家 */
    @Excel(name = "卡发卡国家")
    private String issuerCountry;

}
