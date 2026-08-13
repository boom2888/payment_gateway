package com.games.common.enums;

import lombok.Getter;

/**
 * Shift4 操作代码枚举
 * 定义所有支持的Shift4 API操作类型
 */
@Getter
public enum Shift4OperationCode {

    /**
     * [1] Sale - 授权+捕获
     * 一步完成授权和捕获，直接从持卡人账户扣款
     */
    SALE("1", "Sale"),

    /**
     * [5] Referral Credit - 转介退款
     * 用于退款已捕获的交易
     */
    REFERRAL_CREDIT("5", "Referral Credit"),

    /**
     * [11] Use Token - Sale
     * 使用已有的 Token 进行支付（不需要传卡号）
     */
    USE_TOKEN_SALE("11", "Use Token - Sale"),

    /**
     * [23] Create Token with Sale - 创建Token并支付
     * 在支付的同时创建可重用的Token
     */
    CREATE_TOKEN_WITH_SALE("23", "Create Token with Sale"),

    /**
     * [92] 3DS Completions - 完成3DS认证
     * 用于完成3DS认证流程
     */
    TDS_COMPLETIONS("92", "3DS Completions"),

    /**
     * [101] Past Transaction Retrieval - 查询历史交易
     * 通过requestId或responseId查询历史交易
     */
    PAST_TRANSACTION_RETRIEVAL("101", "Past Transaction Retrieval");

    /**
     * 操作代码
     */
    private final String code;

    /**
     * 操作描述
     */
    private final String description;

    Shift4OperationCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 根据代码获取枚举
     */
    public static Shift4OperationCode fromCode(String code) {
        for (Shift4OperationCode op : values()) {
            if (op.code.equals(code)) {
                return op;
            }
        }
        throw new IllegalArgumentException("Unknown operation code: " + code);
    }
}
