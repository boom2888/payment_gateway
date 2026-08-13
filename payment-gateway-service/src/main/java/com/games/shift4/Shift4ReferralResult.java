package com.games.shift4;

import lombok.Data;

import java.util.Map;

/**
 * S4 转介类操作结果
 */
@Data
public class Shift4ReferralResult {
    /**
     * 是否操作成功（z2=0）
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
     * 更新后的金额 (z25) – 字符串形式
     */
    private String updatedAmount;

    /**
     * 原始响应
     */
    private Map<String, String> rawResponse;
}
