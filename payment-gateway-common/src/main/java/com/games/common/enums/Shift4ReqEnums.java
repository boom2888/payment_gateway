package com.games.common.enums;

import lombok.Getter;

/**
 * Shift4 API请求参数枚举
 * 用于映射Shift4 API的参数代码到有意义的名称，避免硬编码
 */
@Getter
public enum Shift4ReqEnums {

    // 基础参数
    MERCHANT_ID("M", "商户ID"),
    OPERATION_CODE("O", "操作代码"),
    SIGNATURE("K", "签名"),

    // a组 - 交易信息
    REQUEST_ID("a1", "请求ID"),
    PAYMENT_SOURCE_TYPE("a2", "支付来源类型"),
    AMOUNT("a4", "交易金额(最小单位)"),
    CURRENCY("a5", "货币代码"),
    MERCHANT_INVOICE_ID("a8", "商户发票ID"),

    // b组 - 卡信息
    CARD_NUMBER("b1", "卡号"),
    CARD_BRAND_CODE("b2", "卡品牌代码"),
    CARD_EXPIRY_MONTH("b3", "卡过期月份"),
    CARD_EXPIRY_YEAR("b4", "卡过期年份"),
    CARD_CVV("b5", "CVV安全码"),

    // c组 - 持卡人信息
    CARDHOLDER_NAME("c1", "持卡人姓名"),
    CARDHOLDER_EMAIL("c3", "持卡人邮箱"),
    ADDRESS_HOUSE_NUMBER("c4", "门牌号"),
    ADDRESS_STREET("c5", "街道地址"),
    ADDRESS_CITY("c7", "城市"),
    ADDRESS_STATE("c8", "州/省"),
    ADDRESS_COUNTRY("c9", "国家"),
    ADDRESS_ZIP("c10", "邮政编码"),

    // d组 - 设备信息
    IP_ADDRESS("d1", "IP地址"),
    ECHO_FIELD("d2", "回显字段"),
    BROWSER_USER_AGENT("d5", "浏览器User-Agent"),
    BROWSER_LANGUAGE("d6", "浏览器语言"),

    // f组 - 欺诈检测
    SKIP_FRAUD_CHECK("f21", "跳过欺诈检测"),
    FRAUD_THRESHOLD("f22", "欺诈阈值"),

    // g组 - 转介/引用信息
    TOKEN("g1", "Token"),
    RESPONSE_ID("g2", "原交易响应ID"),
    AUTHORIZATION_CODE("g3", "授权码"),
    ORIGINAL_REQUEST_ID("g4", "原始请求ID"),
    REFERRED_TRANSACTION_ID("g5", "转介交易ID"),
    INITIAL_TRANSACTION_ID("g6", "初始交易ID"),

    // h组 - 商户信息
    MERCHANT_REFERENCE_NUMBER("h9", "商户参考号"),
    SELLER_INFORMATION("h15", "卖家信息"),

    // i组 - 描述信息
    DYNAMIC_DESCRIPTOR("i2", "动态描述符"),

    // r组 - 路由信息
    ROUTING_PROCESSOR_CODE("r1", "路由处理器代码"),
    ROUTING_PROCESSOR_MID("r2", "路由处理器商户ID"),
    ROUTING_SEQUENCE("r3", "路由序列"),

    // z组 - 响应信息
    RESPONSE_TRANSACTION_ID("z1", "Shift4交易ID"),
    RESPONSE_CODE("z2", "响应代码"),
    RESPONSE_MESSAGE("z3", "响应消息"),
    RESPONSE_3DS_STATUS("z21", "3DS状态"),
    RESPONSE_UPDATED_AMOUNT("z25", "更新后金额"),
    RESPONSE_PROCESSOR_TRANSACTION_ID("z39", "处理器交易ID"),

    // 3DS相关
    TDS_INITIATE("3ds_initiate", "3DS发起标识"),
    TDS_CHANNEL("3ds_channel", "3DS渠道"),
    TDS_CHALLENGE_WINDOW_SIZE("3ds_challengewindowsize", "3DS挑战窗口大小"),
    TDS_PURCHASE_DATE("3ds_purchasedate", "3DS购买日期"),
    TDS_TRANSTYPE("3ds_transtype", "3DS交易类型"),
    TDS_BROWSER_ACCEPT_HEADER("3ds_browseracceptheader", "浏览器Accept头"),
    TDS_BROWSER_TZ("3ds_browsertz", "浏览器时区"),
    TDS_REDIRECT_URL("3ds_redirect_url", "3DS重定向URL"),
    TDS_BROWSER_COLOR_DEPTH("3ds_browsercolordepth", "浏览器颜色深度"),
    TDS_BROWSER_SCREEN_WIDTH("3ds_browserscreenwidth", "浏览器屏幕宽度"),
    TDS_BROWSER_SCREEN_HEIGHT("3ds_browserscreenheight", "浏览器屏幕高度"),
    TDS_BROWSER_JAVA_ENABLED("3ds_browserjavaenabled", "浏览器Java启用"),
    ACS_URL("acsUrl", "ACS URL"),
    C_REQ("cReq", "挑战请求"),
    C_RES("cRes", "挑战响应"),
    TDS_TRXID("3ds_trxid", "3DS交易ID"),
    TDS_COMPIND("3ds_compind", "3DS完成指示器"),

    // 加密货币相关
    CRYPTO_CURRENCY_TYPE("crypto_currency_type", "加密货币类型"),

    // AFT Recipient 信息 (j组)
    RECIPIENT_ACCOUNT_NUMBER("j2", "收款人账号/PAN"),
    RECIPIENT_FIRST_NAME("j5", "收款人名"),
    RECIPIENT_STREET_ADDRESS("j6", "收款人街道地址"),
    RECIPIENT_CITY("j7", "收款人城市"),
    RECIPIENT_STATE("j8", "收款所在州/省份"),
    RECIPIENT_COUNTRY_CODE("j9", "收款人国家代码"),
    RECIPIENT_SURNAME("j13", "收款人姓"),

    // AFT Sender 信息 (s组)
    SENDER_FIRST_NAME("s10", "付款人名"),
    SENDER_SURNAME("s11", "付款人姓"),
    SENDER_STREET_ADDRESS("s12", "付款人街道地址"),
    SENDER_CITY("s13", "付款人城市"),
    SENDER_STATE_CODE("s14", "发送人州代码"),
    SENDER_COUNTRY_CODE("s15", "付款人国家代码"),
    SENDER_REFERENCE_NUMBER("s17", "发送人参考号"),
    SENDER_SOURCE_OF_FUNDS("s18", "资金来源"),
    SENDER_DATE_OF_BIRTH("s19", "发送人出生日期"),
    SENDER_POSTAL_CODE("s20", "发送人邮政编码");

    private final String code;
    private final String description;

    Shift4ReqEnums(String code, String description) {
        this.code = code;
        this.description = description;
    }

	@Override
    public String toString() {
        return code;
    }
}
