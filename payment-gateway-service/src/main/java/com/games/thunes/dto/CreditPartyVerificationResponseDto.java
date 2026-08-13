package com.games.thunes.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Thunes 收款方验证响应 DTO
 *
 * @author System
 * @date 2025-01-09
 */
@Data
public class CreditPartyVerificationResponseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 验证请求 ID */
    private Long id;

    /** 账户状态 */
    private String account_status;

    /** 姓名匹配结果（如果支持） */
    private String name_match_result;

    /** 验证消息 */
    private String message;

    /**
     * 检查账户是否可用于接收资金
     */
    public boolean isAccountActive() {
        return "ACTIVE".equalsIgnoreCase(account_status) || 
               "AVAILABLE".equalsIgnoreCase(account_status);
    }

    /**
     * 检查姓名是否匹配
     */
    public boolean isNameMatched() {
        return "MATCH".equalsIgnoreCase(name_match_result) || 
               "EXACT_MATCH".equalsIgnoreCase(name_match_result);
    }
}