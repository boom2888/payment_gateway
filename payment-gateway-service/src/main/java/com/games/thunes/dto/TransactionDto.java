package com.games.thunes.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Thunes 交易响应数据传输对象
 *
 * @author System
 * @date 2025-01-08
 */
@Data
public class TransactionDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 交易 ID */
    private Long id;

    /** 外部参考ID */
    private String externalId;

    /** 状态 */
    private String status;

    /** 状态消息 */
    private String statusMessage;

    /** 状态类别 */
    private String statusClass;

    /** 状态类别消息 */
    private String statusClassMessage;

    /** 交易类型 */
    private String transactionType;

    /** Payer 交易参考号 */
    private String payerTransactionReference;

    /** 发送金额 */
    private AmountDto sentAmount;

    /** 批发汇率 */
    private BigDecimal wholesaleFxRate;

    /** 零售汇率 */
    private BigDecimal retailRate;

    /** 零售手续费 */
    private BigDecimal retailFee;

    /** 零售手续费货币 */
    private String retailFeeCurrency;

    /** 手续费 */
    private AmountDto fee;

    /** 汇款目的 */
    private String purposeOfRemittance;

    /** 文档参考号 */
    private String documentReferenceNumber;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date creationDate;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date modificationDate;

    @Data
    public static class AmountDto implements Serializable {
        private static final long serialVersionUID = 1L;

        private String currency;
        private BigDecimal amount;
    }
}