package com.games.common.enums;

import lombok.Getter;

@Getter
public enum TradeCallBackStatus {

    OK("OK", "通过"),

    FAIL("FAIL", "失败"),

    NEW("NEW", "待回调"),

    ;

    TradeCallBackStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static TradeCallBackStatus getByCode(String code) {
        for (TradeCallBackStatus status : TradeCallBackStatus.values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }

    private String code;

    private String desc;
}
