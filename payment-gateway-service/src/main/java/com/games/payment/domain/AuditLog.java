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
 * 审计日志对象 audit_log
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("audit_log")
public class AuditLog  {

private static final long serialVersionUID=1L;


    /** 审计日志ID */
    @TableId(value = "id")
    private Long id;

    /** 审计实体类型：订单/客户的枚举 */
    @Excel(name = "审计实体类型：订单/客户的枚举")
    private Long auditEntityType;

    /** 订单的订单ID或客户的用户ID */
    @Excel(name = "订单的订单ID或客户的用户ID")
    private Long entityId;

    /** SaaS用户ID */
    @Excel(name = "SaaS用户ID")
    private Long saasUserCorporationId;

    /** 进行更改的用户ID（如果由系统更改则为-1） */
    @Excel(name = "进行更改的用户ID" , readConverterExp = "如=果由系统更改则为-1")
    private Long requesterId;

    /** JSON字符串 */
    @Excel(name = "JSON字符串")
    private String content;

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

    /** 0-创建，1-编辑，2-删除 */
    @Excel(name = "0-创建，1-编辑，2-删除")
    private Integer action;

}
