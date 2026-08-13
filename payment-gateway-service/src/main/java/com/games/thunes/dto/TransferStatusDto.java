package com.games.thunes.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Thunes 转账状态 DTO
 *
 * @author System
 * @date 2025-01-08
 */
@Data
public class TransferStatusDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 转账记录ID */
    private Long transferId;

    /** 外部订单号 */
    private String externalId;

    /** 内部状态 */
    private String status;

    /** Thunes 状态 */
    private String thunesStatus;

    /** Thunes 交易ID */
    private String thunesTransactionId;

    /** 转账金额 */
    private BigDecimal amount;

    /** 转账货币 */
    private String currency;

    /** 目标金额 */
    private BigDecimal targetAmount;

    /** 目标货币 */
    private String targetCurrency;

    /** 汇率 */
    private BigDecimal exchangeRate;

    /** 手续费 */
    private BigDecimal fee;

    /** 收款方姓名 */
    private String beneficiaryName;

    /** 失败原因 */
    private String failReason;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;
}