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
 * 报告数据记录对象 report_data_record
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("report_data_record")
public class ReportDataRecord  {

private static final long serialVersionUID=1L;


    /** 主键ID */
    @TableId(value = "id")
    private Long id;

    /** 参考号 */
    @Excel(name = "参考号")
    private String reference;

    /** 类型 */
    @Excel(name = "类型")
    private Long type;

    /** URL */
    @Excel(name = "URL")
    private String url;

    /** 行数 */
    @Excel(name = "行数")
    private Long lineCount;

    /** 状态(1-默认，2-上传完成，3-已初始化，4-处理中，5-已完成) */
    @Excel(name = "状态(1-默认，2-上传完成，3-已初始化，4-处理中，5-已完成)")
    private Long status;

    /** 创建时间 */
    @Excel(name = "创建时间" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

}
