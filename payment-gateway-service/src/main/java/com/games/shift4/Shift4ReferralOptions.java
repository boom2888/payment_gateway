package com.games.shift4;

import lombok.Data;

import java.math.BigDecimal;

/**
 * S4 转介类操作可选参数
 * 对应 Capture/Void/Incremental 等请求可选字段
 */
@Data
public class Shift4ReferralOptions {
    /**
     * 自定义请求ID(a1)
     */
    private String requestId;

    /**
     * 指定货币代码(a5)，默认取订单币种
     */
    private String currency;

    /**
     * 金额(a4)，若方法参数传入则以方法参数为准
     */
    private BigDecimal amount;

    /**
     * 原交易 Response ID (g2)，默认取订单orderUuid
     */
    private String responseId;

    /**
     * 原授权码 (g3)
     */
    private String authorizationCode;

    /**
     * 原请求ID (g4)
     */
    private String originalRequestId;

    /**
     * 初始交易ID (g6)
     */
    private String initialTransactionId;

    /**
     * 二级参考号 (h9)
     */
    private String merchantReferenceNumber;

    /**
     * 市场/卖家信息 (h15)
     */
    private String sellerInformation;

    /**
     * 动态账单描述 (i2)
     */
    private String dynamicDescriptor;

    /**
     * Echo 字段 (d2)
     */
    private String echoField;

    /**
     * 支付来源类型 (a2)
     */
    private Integer paymentSourceType;

    /**
     * 路由处理器 (r1)
     */
    private String routingProcessorCode;

    /**
     * 路由目标MID (r2)
     */
    private String routingProcessorMid;

    /**
     * 路由顺序 (r3)
     */
    private Integer routingSequence;

    /**
     * 是否跳过风控 (f21)
     */
    private Boolean skipFraudCheck;

    /**
     * 风控阈值 (f22)
     */
    private Integer fraudThreshold;

    /**
     * 加密货币类型(crypto_currency_type)
     */
    private String cryptoCurrencyType;

    /**
     * 卡有效期月（部分操作需要）
     */
    private String cardExpiryMonth;

    /**
     * 卡有效期年（部分操作需要）
     */
    private String cardExpiryYear;
}
