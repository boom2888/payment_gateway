package com.games.payment.enums;

import lombok.Getter;

@Getter
public enum UserKycStatus {
    NOT_STARTED(0, "未进行"),
    APPROVED(1, "已批准"),
    ID_PENDING(2, "身份证待处理"),
    AML_PENDING(3, "AML待处理"),
    REJECTED(4, "已拒绝");

    private final int code;
    private final String description;

    UserKycStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static UserKycStatus fromCode(int code) {
        for (UserKycStatus status : UserKycStatus.values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown UserKycStatus code: " + code);
    }
}
