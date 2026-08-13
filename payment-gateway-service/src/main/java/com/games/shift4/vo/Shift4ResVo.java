package com.games.shift4.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Shift4 API 响应参数 VO
 * 基于 S4-Source-Payment-API-Specifications-v1.9-rev-16.pdf Response Fields 部分
 */
@Data
public class Shift4ResVo {

    // ========== 基础参数 ==========

    /**
     * Shift4-assigned Gateway Merchant ID
     * 商户ID
     */
    @JsonProperty("M")
    private String merchantId;

    /**
     * Transaction processing timestamp
     * 交易处理时间戳，格式: MM/dd/yyyy HH:mm:ss
     */
    @JsonProperty("T")
    private String timestamp;

    // ========== a组 - 交易信息（Echo参数）==========

    /**
     * Request ID as sent in the request
     * 请求ID
     */
    @JsonProperty("a1")
    private String requestId;

    /**
     * Payment Source Type as sent in the request
     * 支付来源类型
     */
    @JsonProperty("a2")
    private String paymentSourceType;

    /**
     * Requested Billing Amount as sent in the request
     * 请求的账单金额
     */
    @JsonProperty("a4")
    private String amount;

    /**
     * Currency code
     * 货币代码
     */
    @JsonProperty("a5")
    private String currency;

    /**
     * Transaction date (local date) yyMMdd
     * 交易日期
     */
    @JsonProperty("a6")
    private String transactionDate;

    /**
     * Transaction time (local time) HHmmss
     * 交易时间
     */
    @JsonProperty("a7")
    private String transactionTime;

    /**
     * Transaction Type as sent in the request
     * 交易类型
     */
    @JsonProperty("a9")
    private String transactionType;

    // ========== b组 - 卡信息 ==========

    /**
     * PAN – Primary Account Number (Masked)
     * 卡号（掩码格式: ######****####）
     */
    @JsonProperty("b1")
    private String cardNumber;

    /**
     * Card type
     * 卡类型: 0=Unknown, 1=Visa, 2=Mastercard, 3=American Express,
     * 4=Isracard, 9=Maestro, 10=JCB, 12=Discover, 13=Diners, 14=Cartes Bancaires
     */
    @JsonProperty("b2")
    private String cardType;

    /**
     * Card expiration month
     * 卡过期月份
     */
    @JsonProperty("b3")
    private String cardExpiryMonth;

    /**
     * Card expiration year
     * 卡过期年份
     */
    @JsonProperty("b4")
    private String cardExpiryYear;

    /**
     * Payment Account Reference (PAR)
     * 支付账户参考
     */
    @JsonProperty("b20")
    private String paymentAccountReference;

    // ========== c组 - 持卡人信息 ==========

    /**
     * Cardholder's full name
     * 持卡人全名
     */
    @JsonProperty("c1")
    private String cardholderName;

    // ========== d组 - 设备/回显信息 ==========

    /**
     * Echo parameter as sent in the request
     * 回显参数
     */
    @JsonProperty("d2")
    private String echoField;

    // ========== g组 - Token/引用信息 ==========

    /**
     * Token - Shift4-generated Token that refers to a stored card profile
     * Shift4生成的Token，指向存储的卡资料
     */
    @JsonProperty("g1")
    private String token;

    /**
     * Response ID as sent in the request
     * 响应ID（请求中发送的）
     */
    @JsonProperty("g2")
    private String responseId;

    // ========== h组 - 商户信息 ==========

    /**
     * Sub-Merchant ID
     * 子商户ID
     */
    @JsonProperty("h3")
    private String subMerchantId;

    /**
     * Merchant Reference Number
     * 商户参考号
     */
    @JsonProperty("h9")
    private String merchantReferenceNumber;

    // ========== i组 - 描述信息 ==========

    /**
     * Transaction Free Text Description
     * 交易自由文本描述
     */
    @JsonProperty("i1")
    private String transactionDescription;

    // ========== j组 - 收款人信息 ==========

    /**
     * Primary Account Recipient's date of birth (YYYYMMDD)
     * 主账户收款人出生日期
     */
    @JsonProperty("j1")
    private String recipientDateOfBirth;

    /**
     * Recipient PAN or account number
     * 收款人卡号或账号
     */
    @JsonProperty("j2")
    private String recipientAccountNumber;

    /**
     * Primary Account Recipient's Postal Code
     * 主账户收款人邮编
     */
    @JsonProperty("j3")
    private String recipientPostalCode;

    /**
     * Primary Account Recipient's partial surname
     * 主账户收款人部分姓氏
     */
    @JsonProperty("j4")
    private String recipientPartialSurname;

    // ========== z组 - 响应信息 ==========

    /**
     * Shift4 Gateway Transaction ID
     * Shift4网关交易ID，唯一标识此交易
     */
    @JsonProperty("z1")
    private String transactionId;

    /**
     * Gateway Response Code
     * 网关响应代码。0表示成功，05表示被处理器拒绝
     */
    @JsonProperty("z2")
    private String responseCode;

    /**
     * Description of the operation's result
     * 操作结果描述
     */
    @JsonProperty("z3")
    private String responseMessage;

    /**
     * Authorisation Code
     * 授权码
     */
    @JsonProperty("z4")
    private String authorizationCode;

    /**
     * Risk Score - Fraud protection service's response
     * 风险评分
     */
    @JsonProperty("z5")
    private String riskScore;

    /**
     * Processing Response Reason Code
     * 处理响应原因代码
     */
    @JsonProperty("z6")
    private String processingResponseCode;

    /**
     * AVS Response - Address Verification Service response
     * 地址验证服务响应
     */
    @JsonProperty("z9")
    private String avsResponse;

    /**
     * RRN - Retrieval Reference Number
     * 检索参考号
     */
    @JsonProperty("z13")
    private String rrn;

    /**
     * CVV2 response code
     * CVV2响应代码: M=匹配, N=不匹配, P=未处理, S=应有但未提供, U=不可用等
     */
    @JsonProperty("z14")
    private String cvvResponse;

    /**
     * Approved Billing Amount
     * 批准的账单金额（用于部分授权）
     */
    @JsonProperty("z15")
    private String approvedAmount;

    /**
     * Balance Response
     * 余额响应（借记卡/预付卡）
     */
    @JsonProperty("z16")
    private String balanceResponse;

    /**
     * Balance Response Currency
     * 余额响应货币
     */
    @JsonProperty("z17")
    private String balanceResponseCurrency;

    /**
     * Fraud protection service transmission result
     * 欺诈保护服务传输结果
     */
    @JsonProperty("z21")
    private String fraudProtectionResult;

    /**
     * Updated amount of the transaction
     * 更新后的交易金额（用于授权作废和增量授权）
     */
    @JsonProperty("z25")
    private String updatedAmount;

    /**
     * Full name verification result (ANI)
     * 全名验证结果: A=匹配, B=部分匹配, C=不匹配, U=未验证, N=不支持
     */
    @JsonProperty("z26")
    private String fullNameVerificationResult;

    /**
     * First Name Verification Result (ANI)
     * 名字验证结果
     */
    @JsonProperty("z27")
    private String firstNameVerificationResult;

    /**
     * Last Name Verification Result (ANI)
     * 姓氏验证结果
     */
    @JsonProperty("z28")
    private String lastNameVerificationResult;

    /**
     * Processor Routing Method
     * 处理器路由方法: 1=路由参数, 2=路由规则, 3=处理器优先级
     */
    @JsonProperty("z30")
    private String processorRoutingMethod;

    /**
     * Processor Routing Rule ID
     * 处理器路由规则ID
     */
    @JsonProperty("z31")
    private String processorRoutingRuleId;

    /**
     * Payment Processor that processed the transaction
     * 处理交易的支付处理器
     */
    @JsonProperty("z33")
    private String paymentProcessor;

    /**
     * Payment Processor MID
     * 支付处理器商户ID
     */
    @JsonProperty("z34")
    private String paymentProcessorMid;

    /**
     * Smart Routing reroute indicator
     * 智能路由重新路由指示器
     */
    @JsonProperty("z35")
    private String smartRoutingReroute;

    /**
     * First Payment Processor (for Smart Routing)
     * 第一个支付处理器（智能路由场景）
     */
    @JsonProperty("z36")
    private String firstPaymentProcessor;

    /**
     * Original Response Code from first Payment Processor
     * 第一个支付处理器的原始响应代码
     */
    @JsonProperty("z37")
    private String firstProcessorResponseCode;

    /**
     * Payment Processor Transaction ID
     * 支付处理器交易ID，用于与处理器对账
     */
    @JsonProperty("z39")
    private String processorTransactionId;

    /**
     * Fraud Explanation Array
     * 欺诈解释数组，包含风险评分和触发的规则
     */
    @JsonProperty("z40")
    private String fraudExplanation;

    /**
     * Original Response Code from Processor
     * 处理器的原始响应代码
     */
    @JsonProperty("z41")
    private String originalProcessorResponseCode;

    /**
     * Shift4 stand-in service indicator
     * Shift4备用服务指示器: 1=待处理, 2=最终响应, 3-5=各种阈值限制
     */
    @JsonProperty("z43")
    private String standInServiceIndicator;

    /**
     * Merchant Advice Code (MAC)
     * 商户建议代码，指示是否建议重试
     */
    @JsonProperty("z44")
    private String merchantAdviceCode;

    /**
     * Initial transaction ID
     * 初始交易ID，用于后续商户发起的交易
     */
    @JsonProperty("z50")
    private String initialTransactionId;

    /**
     * Fast funds indicator
     * 快速资金指示器: Y=支持国内和跨境, C=仅跨境, D=仅国内, N=无结果
     */
    @JsonProperty("z51")
    private String fastFundsIndicator;

    /**
     * Payment ID - Unique transaction identifier for related transactions
     * 支付ID，关联同一购买的所有交易
     */
    @JsonProperty("z55")
    private String paymentId;

    /**
     * Transaction Link Identifier (TLID)
     * 交易链接标识符，用于链接整个交易生命周期的相关事件
     */
    @JsonProperty("z70")
    private String transactionLinkId;

    // ========== 3DS 相关 ==========

    /**
     * The ECI assigned to the authentication
     * 分配给认证的ECI
     */
    @JsonProperty("3ds_eci")
    private String tdsEci;

    /**
     * The authentication value received from the issuer
     * 从发卡行收到的认证值
     */
    @JsonProperty("3ds_cavv")
    private String tdsCavv;

    /**
     * The assigned 3D transaction ID
     * 分配的3D交易ID
     */
    @JsonProperty("3ds_trxid")
    private String tdsTrxId;

    /**
     * XID generated as part of the authentication (3DS 1.0.2 only)
     * 认证过程中生成的XID（仅3DS 1.0.2）
     */
    @JsonProperty("3ds_xid")
    private String tdsXid;

    /**
     * The result of the authentication process
     * 认证过程结果: A=尝试处理, Y=成功, N=失败, C=需要挑战, R=拒绝, U=无法执行, I=仅信息, D=解耦认证
     */
    @JsonProperty("3ds_status")
    private String tdsStatus;

    /**
     * Shift4 recommendation whether to initiate payment
     * Shift4建议是否发起支付: y=是, n=否
     */
    @JsonProperty("3ds_valid_payment")
    private String tdsValidPayment;

    /**
     * 3D Secure protocol version
     * 3D Secure协议版本: 1.0, 2.0, 2.1.0, 2.2.0
     */
    @JsonProperty("3ds_version")
    private String tdsVersion;

    /**
     * The issuer URL for the authentication process
     * 发卡行认证URL
     */
    @JsonProperty("3ds_acsurl")
    private String tdsAcsUrl;

    /**
     * 3DS Method URL - 3DS 2.0方法URL
     * 需要先调用此URL收集设备信息
     */
    @JsonProperty("3ds_method")
    private String tdsMethodUrl;

    /**
     * PaReq for 3DS 1.0 flows
     * 3DS 1.0流程的PaReq
     */
    @JsonProperty("3ds_pareq")
    private String tdsPaReq;

    /**
     * Unique transaction identifier assigned by ACS
     * ACS分配的唯一交易标识符
     */
    @JsonProperty("3ds_acstrxid")
    private String tdsAcsTrxId;

    // ========== 其他 ==========

    /**
     * Funding indicator - true if transaction is AFT
     * 资金指示器，如果交易是AFT则为true
     */
    @JsonProperty("funding")
    private String funding;

    /**
     * Signature
     * 签名
     */
    @JsonProperty("K")
    private String signature;

    /**
     * Operation code
     * 操作代码
     */
    @JsonProperty("O")
    private String operationCode;

    /**
     * Version
     * 版本
     */
    @JsonProperty("V")
    private String version;
}
