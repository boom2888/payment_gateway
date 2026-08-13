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
 * 支付报告对象 report_payments
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("report_payments")
public class ReportPayments  {

private static final long serialVersionUID=1L;


    /** 主键ID */
    @TableId(value = "id")
    private Long id;

    /** 上传文件ID */
    @Excel(name = "上传文件ID")
    private Long uploadId;

    /** 卡BIN */
    @Excel(name = "卡BIN")
    private Long bin;

    /** 参考号 */
    @Excel(name = "参考号")
    private Long reference;

    /** 方法名(1-visa，2-mastercard) */
    @Excel(name = "方法名(1-visa，2-mastercard)")
    private Long methodName;

    /** 状态 */
    @Excel(name = "状态")
    private Long status;

    /** 添加记录时间 */
    @Excel(name = "添加记录时间" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 更新记录时间 */
    @Excel(name = "更新记录时间" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    /** 发卡国家 */
    @Excel(name = "发卡国家")
    private String issuerCountry;

    /** 失败原因 */
    @Excel(name = "失败原因")
    private String failReason;

    /** 操作类型 */
    @Excel(name = "操作类型")
    private String actionType;

}
