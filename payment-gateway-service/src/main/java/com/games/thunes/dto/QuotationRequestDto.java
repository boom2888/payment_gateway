package com.games.thunes.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Thunes 报价请求数据传输对象
 *
 * @author System
 * @date 2025-01-08
 */
@Data
public class QuotationRequestDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 外部参考ID */
    private String external_id;

    /** Payer ID */
    private Long payer_id;

    /** 报价模式 (SOURCE_AMOUNT 或 DESTINATION_AMOUNT) */
    private String mode;

    /** 交易类型 (C2C, B2C, C2B, B2B) */
    private String transaction_type;

    /** 源信息 */
    private SourceDto source;

    /** 目标信息 */
    private DestinationDto destination;

    // 为了兼容 Java 命名规范，提供 getter/setter 方法
    public String getExternalId() {
        return external_id;
    }

    public void setExternalId(String externalId) {
        this.external_id = externalId;
    }

    public Long getPayerId() {
        return payer_id;
    }

    public void setPayerId(Long payerId) {
        this.payer_id = payerId;
    }

    public String getTransactionType() {
        return transaction_type;
    }

    public void setTransactionType(String transactionType) {
        this.transaction_type = transactionType;
    }

    @Data
    public static class SourceDto implements Serializable {
        private static final long serialVersionUID = 1L;

        /** 国家代码 */
        private String country_iso_code;

        /** 货币代码 */
        private String currency;

        /** 金额 */
        private BigDecimal amount;

        // 兼容方法
        public String getCountryIsoCode() {
            return country_iso_code;
        }

        public void setCountryIsoCode(String countryIsoCode) {
            this.country_iso_code = countryIsoCode;
        }
    }

    @Data
    public static class DestinationDto implements Serializable {
        private static final long serialVersionUID = 1L;

        /** 国家代码 */
        private String country_iso_code;

        /** 货币代码 */
        private String currency;

        /** 金额（可选，取决于模式） */
        private BigDecimal amount;

        // 兼容方法
        public String getCountryIsoCode() {
            return country_iso_code;
        }

        public void setCountryIsoCode(String countryIsoCode) {
            this.country_iso_code = countryIsoCode;
        }
    }
}