package com.games.payment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CardBrandEnums {

    VISA("VISA"),

    MASTERCARD("MASTERCARD");

    private final String value;
}
