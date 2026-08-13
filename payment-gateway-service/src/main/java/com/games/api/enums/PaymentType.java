package com.games.api.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 支付方式类型
 */
public enum PaymentType {

    /** 信用卡支付 */
    CARD("card"),

    /** Google Pay 支付 */
    GOOGLE_PAY("google_pay"),

    /** Apple Pay 支付 */
    APPLE_PAY("apple_pay"),

    /** 二维码支付 */
    QR_CODE("qr_code");

    @JsonValue
    private final String value;

    PaymentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static PaymentType fromValue(String value) {
        for (PaymentType type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown PaymentType value: " + value);
    }
}
