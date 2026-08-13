package com.games.api.enums;

public enum RiskCacheName {

    RISK_NUVEI_CARD_3DS("risk:nuvei:card:3ds"),

    RISK_NUVEI_CARD_2DS("risk:nuvei:card:2ds"),

    RISK_NUVEI_APPLE_PAY_3DS("risk:nuvei:apple_pay:3ds"),

    RISK_NUVEI_APPLE_PAY_2DS("risk:nuvei:apple_pay:2ds"),

    RISK_ENTITY("entity:"),

    RISK_CARD_NUM("card_num:"),

    USER_ID("user_id");

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    RiskCacheName(String name) {
        this.name = name;
    }
}
