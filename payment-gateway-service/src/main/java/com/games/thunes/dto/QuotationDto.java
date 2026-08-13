package com.games.thunes.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Thunes 报价响应数据传输对象
 *
 * @author System
 * @date 2025-01-08
 */
@Data
public class QuotationDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 报价 ID */
    private Long id;

    /** 外部参考ID */
    private String externalId;

    /** Payer 信息 */
    private PayerDto payer;

    /** 报价模式 */
    private String mode;

    /** 交易类型 */
    private String transactionType;

    /** 源信息 */
    private SourceDto source;

    /** 目标信息 */
    private DestinationDto destination;

    /** 发送金额 */
    private AmountDto sentAmount;

    /** 批发汇率 */
    private BigDecimal wholesaleFxRate;

    /** 手续费 */
    private AmountDto fee;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date creationDate;

    /** 过期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date expirationDate;

    @Data
    public static class SourceDto implements Serializable {
        private static final long serialVersionUID = 1L;

        private String countryIsoCode;
        private String currency;
        private BigDecimal amount;
    }

    @Data
    public static class DestinationDto implements Serializable {
        private static final long serialVersionUID = 1L;

        private String currency;
        private BigDecimal amount;
    }

    @Data
    public static class AmountDto implements Serializable {
        private static final long serialVersionUID = 1L;

        private String currency;
        private BigDecimal amount;
    }
}