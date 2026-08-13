package com.games.wynpay.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum WynApiType {
    /** 创建订单 */
    CREATE_ORDER("/create"),

    /** 支付 */
    PAY_ORDER("/pay");

    @JsonValue
    private final String path;

    WynApiType(String value) {
        this.path = value;
    }

    public String getPath() {
        return path;
    }

    public static WynApiType fromValue(String value) {
        for (WynApiType type : values()) {
            if (type.path.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown PaymentType value: " + value);
    }
}
