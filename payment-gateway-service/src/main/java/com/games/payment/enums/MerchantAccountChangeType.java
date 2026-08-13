package com.games.payment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 商户账户变更类型枚举
 *
 * @author Ticker
 * @date 2025-11-27
 */
@Getter
@AllArgsConstructor
public enum MerchantAccountChangeType {

    /**
     * 更新余额（增加）
     */
    UPDATE_BALANCE(1, "更新余额"),

    /**
     * 回滚余额（减少）
     */
    ROLLBACK_BALANCE(0, "回滚余额");

    private final Integer code;
    private final String desc;
}
