package com.games.shift4;

import lombok.Data;

/**
 * Shift4 授权操作可选参数
 * 对应 API 文档中 a2/a9/a10/a11/a14 等可配置字段
 */
@Data
public class Shift4AuthorizationOptions {
    /**
     * 自定义请求ID (a1)
     */
    private String requestId;

    /**
     * 支付来源 (a2)，默认不发送使用网关默认值
     */
    private Integer paymentSourceType;

    /**
     * 交易类型 (a9)
     */
    private Integer transactionType;

    /**
     * 授权类型 (a10) 1=Final、2=Pre、3=Deferred
     */
    private Integer authorisationType;

    /**
     * 预计捕获次数 (a11)
     */
    private Integer multipleCaptureCount;

    /**
     * 是否允许部分授权 (a14)
     */
    private Boolean allowPartialAuth;

    /**
     * 动态账单描述 (i2)
     */
    private String dynamicDescriptor;

    /**
     * Echo 字段 (d2)
     */
    private String echoField;

    /**
     * 路由处理器 (r1)
     */
    private String routingProcessorCode;

    /**
     * 路由目标MID (r2)
     */
    private String routingProcessorMid;

    /**
     * 路由顺序号 (r3)
     */
    private Integer routingSequence;

    /**
     * 是否跳过风控 (f21)
     */
    private Boolean skipFraudCheck;

    /**
     * 自定义风控阈值 (f22)
     */
    private Integer fraudThreshold;
}
