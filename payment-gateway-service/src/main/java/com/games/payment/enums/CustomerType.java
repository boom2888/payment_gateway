package com.games.payment.enums;

import lombok.Getter;

@Getter
public enum CustomerType {
    PERSONAL(0, "个人"),
    ENTERPRISE(1, "企业");

    private final int code;
    private final String description;

    CustomerType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static CustomerType fromCode(int code) {
        for (CustomerType type : CustomerType.values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown CustomerType code: " + code);
    }
}
