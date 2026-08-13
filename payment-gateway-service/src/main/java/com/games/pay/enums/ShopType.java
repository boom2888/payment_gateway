package com.games.pay.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ShopType {

    SHOP(1),
    EMI_ROLE(2);

    private final Integer code;
}
