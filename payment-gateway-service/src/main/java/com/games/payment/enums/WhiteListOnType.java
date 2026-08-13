package com.games.payment.enums;

import lombok.Getter;

@Getter
public enum WhiteListOnType {

    ON(1),
    OFF(0);
    private final int value;
    WhiteListOnType(int value) {
        this.value = value;
    }
}
