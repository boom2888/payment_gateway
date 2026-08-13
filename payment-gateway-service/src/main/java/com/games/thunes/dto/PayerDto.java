package com.games.thunes.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Thunes Payer 数据传输对象
 *
 * @author System
 * @date 2025-01-08
 */
@Data
public class PayerDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Payer ID */
    private Long id;

    /** Payer 名称 */
    private String name;

    /** 货币代码 */
    private String currency;

    /** 国家代码 */
    private String countryIsoCode;

    /** 精度 */
    private Integer precision;

    /** 增量 */
    private BigDecimal increment;

    /** 最小金额 */
    private BigDecimal minAmount;

    /** 最大金额 */
    private BigDecimal maxAmount;

    /** 服务信息 */
    private ServiceDto service;

    /** 支持的交易类型及其要求 */
    private Map<String, TransactionTypeDto> transactionTypes;

    @Data
    public static class ServiceDto implements Serializable {
        private static final long serialVersionUID = 1L;

        /** 服务 ID */
        private Long id;

        /** 服务名称 */
        private String name;
    }

    @Data
    public static class TransactionTypeDto implements Serializable {
        private static final long serialVersionUID = 1L;

        /** 最小交易金额 */
        private BigDecimal minimumTransactionAmount;

        /** 最大交易金额 */
        private BigDecimal maximumTransactionAmount;

        /** 接受的收款方标识符组合 */
        private List<List<String>> creditPartyIdentifiersAccepted;

        /** 必需的发送方实体字段组合 */
        private List<List<String>> requiredSendingEntityFields;

        /** 必需的收款方实体字段组合 */
        private List<List<String>> requiredReceivingEntityFields;

        /** 必需的文档组合 */
        private List<List<String>> requiredDocuments;

        /** 接受的汇款目的值 */
        private List<String> purposeOfRemittanceValuesAccepted;
    }

    /**
     * 检查是否支持指定的交易类型
     */
    public boolean supportsTransactionType(String transactionType) {
        return transactionTypes != null && transactionTypes.containsKey(transactionType);
    }

    /**
     * 获取指定交易类型的配置
     */
    public TransactionTypeDto getTransactionTypeConfig(String transactionType) {
        return transactionTypes != null ? transactionTypes.get(transactionType) : null;
    }
}