package com.games.api.bean;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RefundOrderResult {
    private boolean success;

    private String refundOrderId;

    private BigDecimal refundAmount;

    private String transactionId;

    private String currency;

    private String message;

    public RefundOrderResult() {}

    public RefundOrderResult(String message) {
        this.success = false;
        this.message = message;
    }
}
