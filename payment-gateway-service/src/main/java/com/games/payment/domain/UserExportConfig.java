package com.games.payment.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 用户导出配置对象 user_export_config
 *
 * @author System
 * @date 2025-01-03
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("user_export_config")
public class UserExportConfig {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id")
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 模块名称(如: order, refundOrder, customer) */
    private String module;

    /** 导出字段配置（逗号分隔） */
    private String exportFields;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;
}

