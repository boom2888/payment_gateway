package com.games.api.enums;

import lombok.Getter;

/**
 * 支付供应商枚举
 */
@Getter
public enum PaymentProvider {
    
    /**
     * WynPay支付供应商
     */
    WYNPAY("wynpay", "WynPay支付"),
    
    /**
     * Nuvei支付供应商
     */
    NUVEI("nuvei", "Nuvei支付"),

    
    /**
     * CaratPay支付供应商
     */
    CARAT_PAY("carat_pay", "CaratPay支付");

    
    private final String code;
    private final String description;
    
    PaymentProvider(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 根据代码获取支付供应商
     * @param code 供应商代码
     * @return 支付供应商枚举
     */
    public static PaymentProvider fromCode(String code) {
        for (PaymentProvider provider : values()) {
            if (provider.getCode().equals(code)) {
                return provider;
            }
        }
        throw new IllegalArgumentException("未知的支付供应商: " + code);
    }
}
