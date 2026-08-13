package com.games.payment.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import com.games.common.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;


import java.math.BigDecimal;
import java.util.Date;

/**
 * 汇率报告对象 report_quote
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("report_quote")
public class ReportQuote  {


    /** 主键ID */
    @TableId(value = "id")
    private Long id;

    /** 源货币代码 */
    @Excel(name = "源货币代码", langKey = "payment.reportQuote.excel.fromCode")
    private String fromCode;

    /** 源货币代码对应的货币表ID */
    @Excel(name = "源货币代码对应的货币表ID", langKey = "payment.reportQuote.excel.fromId")
    private Long fromId;

    /** 目标货币代码 */
    @Excel(name = "目标货币代码", langKey = "payment.reportQuote.excel.toCode")
    private String toCode;

    /** 目标货币代码对应的货币表ID */
    @Excel(name = "目标货币代码对应的货币表ID", langKey = "payment.reportQuote.excel.toId")
    private Long toId;

    /** 创建时间 */
    @Excel(name = "创建时间", langKey = "payment.reportQuote.excel.createdAt", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 更新时间 */
    @Excel(name = "更新时间", langKey = "payment.reportQuote.excel.updatedAt", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    /** 从源货币转换为目标货币的汇率 */
    @Excel(name = "从源货币转换为目标货币的汇率", langKey = "payment.reportQuote.excel.quote")
    private BigDecimal quote;

}
