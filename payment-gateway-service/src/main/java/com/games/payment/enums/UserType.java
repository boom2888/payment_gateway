package com.games.payment.enums;

import lombok.Getter;

@Getter
public enum UserType {
    PERSONAL(0, "个人客户"),
    ENTERPRISE(1, "企业客户"),
    SAAS(2, "SaaS用户"),
    PLATFORM(3, "平台用户"),
    MERCHANT(4, "商户用户");

    private final int code;
    private final String description;

    UserType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static UserType fromCode(int code) {
        for (UserType type : UserType.values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown UserType code: " + code);
    }
}