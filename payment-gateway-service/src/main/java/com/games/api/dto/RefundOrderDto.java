package com.games.api.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RefundOrderDto {

    /**
     * 商户ID
     */
    private String merchantId;

    /**
     * 商户退款订单号，区别于支付时的订单号
     */
    private String ssoOrderId;

    /**
     * 返回商户的退款订单号
     */
    private String refundOrderId;

    /**
     *
     */
    private BigDecimal refundAmount;

    private BigDecimal remainingRefundableAmount;

    private String currency;

    private String status;

    private String refundTime;

    private String failReasons;

    private String refundSsoOrderId;

}
