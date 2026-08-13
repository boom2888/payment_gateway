package com.games.common.enums;

import cn.hutool.core.util.StrUtil;
import com.games.common.utils.MessageUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 失败原因类型枚举
 * 用于将具体的失败原因归类为通用类型
 * 
 * @author system
 */
public enum FailReasonType {
    
    /**
     * IP黑名单拦截
     */
    IP_BLACKLIST("fail.reason.ip.blacklist", Arrays.asList(
        "TRANSREASON1=1000000154",
        "IP_BLACKLIST",
        "IP_BLOCKED",
        "BLACKLIST_IP"
    )),
    
    /**
     * 银行卡被拒绝
     */
    CARD_DECLINED("fail.reason.card.declined", Arrays.asList(
        "CARD_DECLINED",
        "DECLINED",
        "CARD_REJECTED",
        "PAYMENT_DECLINED",
        "TRANSREASON1=1000000001"
    )),
    
    /**
     * 余额不足
     */
    INSUFFICIENT_FUNDS("fail.reason.insufficient.balance", Arrays.asList(
        "INSUFFICIENT_FUNDS",
        "INSUFFICIENT_BALANCE",
        "LOW_BALANCE",
        "BALANCE_TOO_LOW",
        "TRANSREASON1=1000000002"
    )),
    
    /**
     * 3DS认证失败
     */
    THREEDS_FAILED("fail.reason.threeds.failed", Arrays.asList(
        "THREEDS_FAILED",
        "3DS_FAILED",
        "AUTHENTICATION_FAILED",
        "3DS_AUTH_FAILED",
        "TRANSREASON1=1000000003"
    )),
    
    /**
     * 风控拦截
     */
    RISK_CONTROL("fail.reason.risk.control", Arrays.asList(
        "RISK_CONTROL",
        "FRAUD_DETECTED",
        "RISK_SCORE_HIGH",
        "FRAUD_PREVENTION",
        "TRANSREASON1=1000000004"
    )),
    
    /**
     * 网络超时
     */
    NETWORK_TIMEOUT("fail.reason.network.timeout", Arrays.asList(
        "NETWORK_TIMEOUT",
        "TIMEOUT",
        "CONNECTION_TIMEOUT",
        "REQUEST_TIMEOUT",
        "TRANSREASON1=1000000005"
    )),
    
    /**
     * 商户限制
     */
    MERCHANT_LIMIT("fail.reason.merchant.limit", Arrays.asList(
        "MERCHANT_LIMIT",
        "MERCHANT_BLOCKED",
        "MERCHANT_SUSPENDED",
        "TRANSREASON1=1000000006"
    )),
    
    /**
     * 系统错误
     */
    SYSTEM_ERROR("fail.reason.system.error", Arrays.asList(
        "SYSTEM_ERROR",
        "INTERNAL_ERROR",
        "SERVER_ERROR",
        "TRANSREASON1=1000000007"
    )),
    
    /**
     * 其他原因
     */
    OTHER("fail.reason.other", Arrays.asList());
    
    private final String messageKey;
    private final List<String> keywords;
    
    FailReasonType(String messageKey, List<String> keywords) {
        this.messageKey = messageKey;
        this.keywords = keywords;
    }
    
    /**
     * 获取国际化显示名称
     */
    public String getDisplayName() {
        return MessageUtils.message(messageKey);
    }
    
    public List<String> getKeywords() {
        return keywords;
    }
    
    /**
     * 根据失败原因字符串匹配对应的类型
     * 
     * @param failReason 失败原因字符串
     * @return 匹配的失败原因类型，如果没有匹配则返回OTHER
     */
    public static FailReasonType matchFailReason(String failReason) {
        if (StrUtil.isBlank(failReason)) {
            return OTHER;
        }
        
        String upperFailReason = failReason.toUpperCase().trim();
        
        // 遍历所有类型（除了OTHER）
        for (FailReasonType type : values()) {
            if (type == OTHER) {
                continue;
            }
            
            // 检查是否包含关键词
            for (String keyword : type.getKeywords()) {
                if (upperFailReason.contains(keyword.toUpperCase())) {
                    return type;
                }
            }
        }
        
        return OTHER;
    }
    
    /**
     * 获取所有类型的显示名称
     * 
     * @return 显示名称数组
     */
    public static String[] getAllDisplayNames() {
        return Arrays.stream(values())
                .map(FailReasonType::getDisplayName)
                .toArray(String[]::new);
    }
}
