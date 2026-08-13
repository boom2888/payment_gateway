package com.games.api.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MerchantNotificationType {

    /** 1 - WIDGET */
    WIDGET(1, "WIDGET"),

    /** 2 - API */
    API(2, "API"),

    /** 3 - CP */
    CP(3, "CP");

    @JsonValue
    private final int code;
    private final String desc;

    MerchantNotificationType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static MerchantNotificationType fromCode(int code) {
        for (MerchantNotificationType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown MerchantNotificationType code: " + code);
    }
}
