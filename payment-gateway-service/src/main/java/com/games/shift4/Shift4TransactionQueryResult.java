package com.games.shift4;

import lombok.Data;

import java.util.Map;

/**
 * Shift4 交易查询结果
 * 用于操作代码 [101] - Past Transaction Retrieval
 */
@Data
public class Shift4TransactionQueryResult {
    /**
     * 是否查询成功（z2=0）
     */
    private boolean success;

    /**
     * 响应码 z2
     */
    private String responseCode;

    /**
     * 响应信息 z3
     */
    private String message;

    /**
     * Shift4 交易ID (z1)
     */
    private String shift4TransactionId;

    /**
     * 处理器交易ID (z39)
     */
    private String processorTransactionId;

    /**
     * 交易金额 (z42)
     */
    private String amount;

    /**
     * 货币代码 (z43)
     */
    private String currency;

    /**
     * 授权码 (z41)
     */
    private String authorizationCode;

    /**
     * 卡号后4位 (z44)
     */
    private String cardLast4;

    /**
     * 卡品牌 (z45)
     */
    private String cardBrand;

    /**
     * 交易日期 (z46)
     */
    private String transactionDate;

    /**
     * 交易时间 (z47)
     */
    private String transactionTime;

    /**
     * 原始响应
     */
    private Map<String, String> rawResponse;
}
