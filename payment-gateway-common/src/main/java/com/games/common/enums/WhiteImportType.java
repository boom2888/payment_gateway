package com.games.common.enums;

import lombok.Getter;

/**
 * 白名单导入类型
 * 
 * @author lor
 *
 */
@Getter
public enum WhiteImportType
{
    COVER(1, "覆盖"),

    COMPATIBLE(2, "兼容"),

    REMOVE(3, "移除"),
    ;

    WhiteImportType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static WhiteImportType getByCode(Integer code) {
        for (WhiteImportType status : WhiteImportType.values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }

    private final Integer code;
    private final String desc;
}
