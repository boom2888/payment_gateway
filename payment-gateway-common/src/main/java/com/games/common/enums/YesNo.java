package com.games.common.enums;

import lombok.Getter;

/**
 * 是否
 *
 * @author games
 */
@Getter
public enum YesNo {
    /**
     * 1
     */
    YES("Y"),

    /**
     * 0
     */
    NO("N");
    YesNo(String value) {
        this.value = value;
    }

    private final String value;
}
