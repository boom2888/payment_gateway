package com.games.thunes.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Thunes 转账响应 DTO
 *
 * @author System
 * @date 2025-01-08
 */
@Data
public class TransferResponseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 转账记录ID */
    private Long transferId;

    /** 外部订单号 */
    private String externalId;

    /** 转账状态 */
    private String status;

    /** Thunes 交易ID */
    private String thunesTransactionId;

    /** Thunes 报价ID */
    private String quotationId;

    /** 汇率 */
    private BigDecimal exchangeRate;

    /** 手续费 */
    private BigDecimal fee;

    /** 手续费货币 */
    private String feeCurrency;

    /** 目标金额 */
    private BigDecimal targetAmount;

    /** 目标货币 */
    private String targetCurrency;

    /** 错误信息 */
    private String errorMessage;
}