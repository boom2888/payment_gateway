package com.games.api.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RiskControlEntity {

    private String userId;

    private String orderId;

    private Long merchantId;

    // 是否是新用户
    private Boolean isNewUser;

    // 邮箱
    private String email;

    // ip信息
    private String ipAddress;

    // 卡号
    private String cardNumber;

    // 卡指纹（不可逆，用于匹配/统计）
    private String cardFinger;

    // 卡组织 Visa、MasterCard、American Express
    private String cardOrganization;

    // 支付模式 Nuvei ACQ
    private String paymentMethod;

    // 支付方式 card:卡支付, google_pay:谷歌支付, apple_pay:苹果支付
    private String paymentType;

    // 支付货币 USD EUR
    private String paymentCurrency;

    // 支付金额
    private BigDecimal payAmount;

    // 交易次数
    private String transactionCount;

    // 是否是3Ds支付
    private Boolean is3DSAuthenticated;

    // 发卡行
    private String issuingBank;

    // 支付地址所在国家
    private String billingCountry;

    // 详细的支付地址
    private String billingAddress;

}
