package com.games.common.enums;

import lombok.Getter;

@Getter
public enum EmiProfileStatus {

    INITIAL(0, "待提交资料"),

    WAIT(1, "待审核"),

    BINDERR_WAIT(2, "待binderr审核"),

    OK(3, "已批准"),

    FAIL(4, "审核失败");

    EmiProfileStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static EmiProfileStatus getByCode(Integer code) {
        for (EmiProfileStatus status : EmiProfileStatus.values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }

    private final Integer code;

    private final String desc;
}
