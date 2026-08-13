package com.games.common.enums;

/**
 * Thunes 转账状态枚举
 *
 * @author System
 * @date 2025-01-08
 */
public enum ThunesTransferStatus {

    CREATED("CREATED", "已创建"),
    QUOTATION_CREATED("QUOTATION_CREATED", "报价已创建"),
    TRANSACTION_CREATED("TRANSACTION_CREATED", "交易已创建"),
    CONFIRMED("CONFIRMED", "已确认"),
    PROCESSING("PROCESSING", "处理中"),
    COMPLETED("COMPLETED", "已完成"),
    FAILED("FAILED", "失败"),
    REVERSED("REVERSED", "已撤销"),
    CANCELLED("CANCELLED", "已取消");

    private final String code;
    private final String description;

    ThunesTransferStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static ThunesTransferStatus fromCode(String code) {
        for (ThunesTransferStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的转账状态代码: " + code);
    }
}