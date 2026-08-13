package com.games.api.enums;

import cn.hutool.core.util.StrUtil;
import com.games.common.exception.BusinessException;
import lombok.Getter;

import java.util.EnumSet;
import java.util.Set;

@Getter
public enum RefundStatus {

    SUCCESS(0),
    FAILED(1),
    PROCESSING(2);

    private final Integer code;

    RefundStatus(Integer code) {
        this.code = code;
    }

    public static final Set<RefundStatus> canNotRefundStatus = EnumSet.of(RefundStatus.SUCCESS, RefundStatus.PROCESSING);

    public static RefundStatus getName(Integer code) {
        for (RefundStatus refundStatus : RefundStatus.values()) {
            if (refundStatus.getCode().equals(code)) {
                return refundStatus;
            }
        }
        throw new BusinessException(StrUtil.format("status: {} is illegal", code));
    }
}
