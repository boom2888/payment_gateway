package com.games.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Thunes 交易状态码枚举
 * 根据 Thunes API 文档定义的状态码
 *
 * @author System
 * @date 2025-01-13
 */
@Getter
@AllArgsConstructor
public enum ThunesStatusCode {

    // ==================== 3xxxx 拒绝状态 ====================
    REJECTED_BARRED_SENDER("30202", "REJECTED-BARRED-SENDER", "发送方被列入黑名单", ThunesTransferStatus.FAILED),
    REJECTED_BARRED_BENEFICIARY("30201", "REJECTED-BARRED-BENEFICIARY", "收款方被列入黑名单", ThunesTransferStatus.FAILED),
    REJECTED_INSUFFICIENT_BALANCE("30500", "REJECTED-INSUFFICIENT-BALANCE", "余额不足", ThunesTransferStatus.FAILED),

    // ==================== 7xxxx 完成状态 ====================
    COMPLETED("70000", "COMPLETED", "交易完成", ThunesTransferStatus.COMPLETED),

    // ==================== 8xxxx 撤销状态 ====================
    REVERSED("80000", "REVERSED", "交易已撤销", ThunesTransferStatus.REVERSED),

    // ==================== 9xxxx 拒绝状态 ====================
    DECLINED_SLS_SENDER("90110", "DECLINED-SLS-SENDER", "发送方SLS验证失败", ThunesTransferStatus.FAILED),
    DECLINED_SLS_BENEFICIARY("90120", "DECLINED-SLS-BENEFICIARY", "收款方SLS验证失败", ThunesTransferStatus.FAILED),
    DECLINED_INVALID_BENEFICIARY("90200", "DECLINED-INVALID-BENEFICIARY", "收款方信息无效", ThunesTransferStatus.FAILED),
    DECLINED_BARRED_BENEFICIARY("90201", "DECLINED-BARRED-BENEFICIARY", "收款方被禁止", ThunesTransferStatus.FAILED),
    DECLINED_INVALID_BENEFICIARY_DETAILS("90210", "DECLINED-INVALID-BENEFICIARY-DETAILS", "收款方详细信息无效",
            ThunesTransferStatus.FAILED),
    DECLINED_LIMITATIONS_ON_TRANSACTION_VALUE("90305", "DECLINED-LIMITATIONS-ON-TRANSACTION-VALUE", "交易金额超出限制",
            ThunesTransferStatus.FAILED),
    DECLINED_LIMITATIONS_ON_BENEFICIARY_VALUE("90320", "DECLINED-LIMITATIONS-ON-BENEFICIARY-VALUE", "收款方金额超出限制",
            ThunesTransferStatus.FAILED),
    DECLINED_LIMITATIONS_ON_ACCOUNT_VALUE("90330", "DECLINED-LIMITATIONS-ON-ACCOUNT-VALUE", "账户金额超出限制",
            ThunesTransferStatus.FAILED),
    DECLINED_LIMITATIONS_ON_ACCOUNT_QUANTITY("90370", "DECLINED-LIMITATIONS-ON-ACCOUNT-QUANTITY", "账户交易次数超出限制",
            ThunesTransferStatus.FAILED),
    DECLINED_PAYER_CURRENTLY_UNAVAILABLE("90400", "DECLINED-PAYER-CURRENTLY-UNAVAILABLE", "付款方当前不可用",
            ThunesTransferStatus.FAILED),

    // ==================== 6xxxx 处理中状态 ====================
    PROCESSING("60000", "PROCESSING", "交易处理中", ThunesTransferStatus.PROCESSING),

    // ==================== 未知状态 ====================
    UNKNOWN("99999", "UNKNOWN", "未知状态", ThunesTransferStatus.PROCESSING);

    /**
     * 状态码
     */
    private final String code;

    /**
     * 状态名称 (英文)
     */
    private final String name;

    /**
     * 状态描述 (中文)
     */
    private final String description;

    /**
     * 对应的内部转账状态
     */
    private final ThunesTransferStatus internalStatus;

    /**
     * 状态码缓存 Map
     */
    private static final Map<String, ThunesStatusCode> CODE_MAP = new HashMap<>();

    static {
        for (ThunesStatusCode status : values()) {
            CODE_MAP.put(status.getCode(), status);
        }
    }

    /**
     * 根据状态码获取枚举
     *
     * @param code 状态码
     * @return 枚举对象，找不到返回 UNKNOWN
     */
    public static ThunesStatusCode fromCode(String code) {
        if (code == null || code.isEmpty()) {
            return UNKNOWN;
        }
        return CODE_MAP.getOrDefault(code, UNKNOWN);
    }

    /**
     * 根据状态码前缀判断内部状态
     *
     * @param statusCode 状态码
     * @return 内部转账状态
     */
    public static ThunesTransferStatus resolveInternalStatus(String statusCode) {
        if (statusCode == null || statusCode.isEmpty()) {
            return ThunesTransferStatus.PROCESSING;
        }

        // 先尝试精确匹配
        ThunesStatusCode exactMatch = CODE_MAP.get(statusCode);
        if (exactMatch != null) {
            return exactMatch.getInternalStatus();
        }

        // 根据前缀判断
        char prefix = statusCode.charAt(0);
        switch (prefix) {
            case '7':
                return ThunesTransferStatus.COMPLETED;
            case '8':
                return ThunesTransferStatus.REVERSED;
            case '3':
            case '9':
                return ThunesTransferStatus.FAILED;
            case '6':
            default:
                return ThunesTransferStatus.PROCESSING;
        }
    }

    /**
     * 根据状态码获取失败原因描述
     *
     * @param statusCode    状态码
     * @param statusMessage 原始状态消息
     * @return 格式化的失败原因
     */
    public static String resolveFailReason(String statusCode, String statusMessage) {
        ThunesStatusCode status = fromCode(statusCode);

        if (status == UNKNOWN) {
            // 未知状态码，返回原始消息
            return statusMessage != null ? statusMessage : "未知错误";
        }

        // 返回格式: [状态码] 中文描述 (英文名称)
        return String.format("[%s] %s (%s)", status.getCode(), status.getDescription(), status.getName());
    }

    /**
     * 判断是否为失败状态
     *
     * @param statusCode 状态码
     * @return 是否失败
     */
    public static boolean isFailedStatus(String statusCode) {
        if (statusCode == null || statusCode.isEmpty()) {
            return false;
        }
        ThunesTransferStatus internalStatus = resolveInternalStatus(statusCode);
        return internalStatus == ThunesTransferStatus.FAILED || internalStatus == ThunesTransferStatus.REVERSED;
    }

    /**
     * 判断是否为完成状态
     *
     * @param statusCode 状态码
     * @return 是否完成
     */
    public static boolean isCompletedStatus(String statusCode) {
        if (statusCode == null || statusCode.isEmpty()) {
            return false;
        }
        return resolveInternalStatus(statusCode) == ThunesTransferStatus.COMPLETED;
    }
}
