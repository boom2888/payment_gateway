package com.games.payment.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.games.common.annotation.Excel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 白名单导入记录对象 movement_import
 *
 * @author Ticker
 * @date 2025-07-21
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("white_upload_record")
public class WhiteUploadRecord {

private static final long serialVersionUID=1L;


    /** 导入id */
    @TableId(value = "id")
    private Long id;

    /** 导入报告名 */
    @Excel(name = "报告原名")
    private String originName;

    @Excel(name = "导入新名")
    private String newName;

    /** 导入报告地址 */
    @Excel(name = "导入报告地址")
    private String fileUrl;

    /** 总条数 */
    @Excel(name = "导入总条数")
    private Integer importTotalNum;

    /** 状态 */
    @Excel(name = "状态")
    private Integer status;


    /** 导入时间 */
    @Excel(name = "导入时间" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date finishedAt;

    /** 记录删除时间 */
    @Excel(name = "记录删除时间" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deletedAt;

    /** 最后删除记录的用户ID */
    @Excel(name = "最后删除记录的用户ID")
    private Long deletedBy;

    /** 类型(0-默认，1-已删除) */
    @Excel(name = "类型(0-默认，1-已删除)")
    @TableLogic
    private Long deleted;

    private String remark;

    @TableField(exist = false)
    private Integer dataCount;
}
