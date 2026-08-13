package com.games.payment.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Thunes Payer 实体类
 *
 * @author System
 * @date 2025-01-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "thunes_payer", autoResultMap = true)
public class ThunesPayer implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Thunes Payer ID */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /** Payer 名称 */
    @TableField("name")
    private String name;

    /** 货币代码 */
    @TableField("currency")
    private String currency;

    /** 国家代码 */
    @TableField("country_code")
    private String countryCode;

    /** 精度 */
    @TableField("precision_value")
    private Integer precisionValue;

    /** 增量 */
    @TableField("increment_value")
    private BigDecimal incrementValue;

    /** 最小金额 */
    @TableField("min_amount")
    private BigDecimal minAmount;

    /** 最大金额 */
    @TableField("max_amount")
    private BigDecimal maxAmount;

    /** 服务ID */
    @TableField("service_id")
    private Long serviceId;

    /** 服务名称 */
    @TableField("service_name")
    private String serviceName;

    /** 交易类型配置JSON */
    @TableField(value = "transaction_types_json", typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> transactionTypesJson;

    /** 是否启用 */
    @TableField("is_active")
    private Boolean isActive;

    /** 最后同步时间 */
    @TableField("last_sync_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastSyncAt;

    /** 创建时间 */
    @TableField("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    /** 更新时间 */
    @TableField("updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    /**
     * 检查是否支持指定的交易类型
     */
    public boolean supportsTransactionType(String transactionType) {
        return transactionTypesJson != null && transactionTypesJson.containsKey(transactionType);
    }

    /**
     * 获取指定交易类型的配置
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getTransactionTypeConfig(String transactionType) {
        if (transactionTypesJson == null) {
            return null;
        }
        Object config = transactionTypesJson.get(transactionType);
        return config instanceof Map ? (Map<String, Object>) config : null;
    }
}