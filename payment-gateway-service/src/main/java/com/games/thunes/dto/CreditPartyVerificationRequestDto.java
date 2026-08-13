package com.games.thunes.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Thunes 收款方验证请求 DTO
 *
 * @author System
 * @date 2025-01-09
 */
@Data
public class CreditPartyVerificationRequestDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 收款方标识符 */
    private CreditPartyIdentifierDto credit_party_identifier;

    /** 收款方信息（可选，取决于支付方要求） */
    private BeneficiaryDto beneficiary;

    @Data
    public static class CreditPartyIdentifierDto implements Serializable {
        private static final long serialVersionUID = 1L;

        /** 手机号（移动钱包） */
        private String msisdn;

        /** 银行账号 */
        private String bank_account_number;

        /** SWIFT BIC 代码 */
        private String swift_bic_code;

        /** 排序代码（英国） */
        private String sort_code;

        /** 账号（英国） */
        private String account_number;

        /** IBAN（欧洲） */
        private String iban;

        /** CLABE（墨西哥） */
        private String clabe;
    }

    @Data
    public static class BeneficiaryDto implements Serializable {
        private static final long serialVersionUID = 1L;

        private String firstname;
        private String lastname;
        private String country_iso_code;
        private String id_number;
        private String id_type;
        // 可根据需要添加更多字段
    }
}