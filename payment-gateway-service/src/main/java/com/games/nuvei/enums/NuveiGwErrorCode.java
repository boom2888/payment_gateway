package com.games.nuvei.enums;

import lombok.Getter;

@Getter
public enum NuveiGwErrorCode {
    APPROVED(0, 0),
    DECLINED(-1, 0),
    FILTER_ERROR(-1100, 0);

    private final int gwErrorCode;

    private final int gwExtendedErrorCode;

    NuveiGwErrorCode(int code, int extendedErrorCode) {
        this.gwErrorCode = code;
        this.gwExtendedErrorCode = extendedErrorCode;
    }

    public static NuveiGwErrorCode fromGwErrorCode(int gwErrorCode) {
        for (NuveiGwErrorCode code : NuveiGwErrorCode.values()) {
            if (code.gwErrorCode == gwErrorCode) {
                return code;
            }
        }
        return null;
    }
}
