package com.games.common.enums;

import lombok.Getter;

@Getter
public enum BusinessProfileStatus {

    INITIAL(-1, "待提交资料"),

    WAIT(0, "待审核"),

    OK(1, "已批准"),

    FAIL(2, "已拒绝"),

    BINDERR_WAIT(3, "待Binderr审核"),

    BINDERR_FAIL(4, "Binderr审核失败")
    ;

    BusinessProfileStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static BusinessProfileStatus getByCode(Integer code) {
        for (BusinessProfileStatus status : BusinessProfileStatus.values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }

    private final Integer code;

    private final String desc;
}
