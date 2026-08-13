package com.games.api.enums;

public enum PaymentMethod {

    NUVEI("nuvie");

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    PaymentMethod(String name) {
        this.name = name;
    }
}
