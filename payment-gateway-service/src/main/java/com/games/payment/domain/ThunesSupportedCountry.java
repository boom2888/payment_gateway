package com.games.payment.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * Thunes 支持的国家和货币配置实体
 *
 * @author System
 * @date 2025-01-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("thunes_supported_country")
public class ThunesSupportedCountry implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 国家代码（ISO 3166-1 alpha-3） */
    @TableField("country_code")
    private String countryCode;

    /** 货币代码（ISO 4217） */
    @TableField("currency")
    private String currency;

    /** 该国家货币的支付方数量 */
    @TableField("payer_count")
    private Integer payerCount;

    /** 是否启用（1-启用，0-禁用） */
    @TableField("is_active")
    private Boolean isActive;

    /** 创建时间 */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private Date createdAt;

    /** 更新时间 */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private Date updatedAt;
}