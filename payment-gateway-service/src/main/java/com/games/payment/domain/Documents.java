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
 * 文档对象 documents
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("documents")
public class Documents  {

private static final long serialVersionUID=1L;


    /** 文档ID */
    @TableId(value = "id")
    private Long id;

    /** 企业客户公司ID */
    @Excel(name = "企业客户公司ID")
    private Long businessCustomerCorporationId;

    /** 仅限个人客户的客户ID */
    @Excel(name = "仅限个人客户的客户ID")
    private Long customerId;

    /** 文档URL */
    @Excel(name = "文档URL")
    private String documentUrl;

    /** 关联config_document_type.code */
    @Excel(name = "关联config_document_type.code")
    private String documentTypeCode;

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

    /** 记录描述 */
    @Excel(name = "记录描述")
    private String remark;

    /** 类型(0-默认，1-已删除) */
    @Excel(name = "类型(0-默认，1-已删除)")
    private Long deleted;

    /** 其他文档描述 */
    @Excel(name = "其他文档描述")
    private String otherDocumentDesc;

    /** 仅限企业类型股东 */
    @Excel(name = "仅限企业类型股东")
    private Long businessMemberId;

}
