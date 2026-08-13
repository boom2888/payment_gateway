package com.games.thunes.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 转账交易动态数据
 * 存储在 JSON 字段中的交易相关信息
 *
 * @author System
 * @date 2025-01-12
 */
@Data
public class TransactionData implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 发送方信息 */
    private PartyInfo sender;

    /** 收款方信息 */
    private PartyInfo beneficiary;

    /** 支付方式信息 */
    private PaymentMethodInfo paymentMethod;

    /** 合规信息 */
    private ComplianceInfo compliance;

    @Data
    public static class PartyInfo implements Serializable {
        private static final long serialVersionUID = 1L;

        /** 参与方类型：INDIVIDUAL（个人）或 BUSINESS（企业） */
        private String type;

        /** 个人信息 */
        private IndividualInfo individual;

        /** 企业信息 */
        private BusinessInfo business;
    }

    @Data
    public static class IndividualInfo implements Serializable {
        private static final long serialVersionUID = 1L;

        private String firstName;
        private String lastName;
        private String lastName2;
        private String middleName;
        private String nativeName;
        private String code;
        private String idType;
        private String idCountryIsoCode;
        private String idNumber;
        private String idDeliveryDate;
        private String idExpirationDate;
        private String address;
        private String postalCode;
        private String city;
        private String provinceState;
        private String countryIsoCode;
        private String msisdn;
        private String email;
        private String dateOfBirth;
        private String gender;
        private String occupation;
        private String bankAccountNumber;
        private String bankAccountHolderName;
        private String beneficiaryRelationship;
        private String sourceOfFunds;
        private String nationalityCountryIsoCode;
        private String countryOfBirthIsoCode;
    }

    @Data
    public static class BusinessInfo implements Serializable {
        private static final long serialVersionUID = 1L;

        private String registeredName;
        private String tradingName;
        private String code;
        private String registrationNumber;
        private String taxId;
        private String dateOfIncorporation;
        private String address;
        private String postalCode;
        private String city;
        private String provinceState;
        private String countryIsoCode;
        private String msisdn;
        private String email;
        private String businessRelationship;

        /** 代表人信息 */
        private RepresentativeInfo representative;
    }

    @Data
    public static class RepresentativeInfo implements Serializable {
        private static final long serialVersionUID = 1L;

        private String firstName;
        private String lastName;
        private String lastname2;
        private String middleName;
        private String nativeName;
        private String idType;
        private String idCountryIsoCode;
        private String idNumber;
        private String idDeliveryDate;
        private String idExpirationDate;
    }

    @Data
    public static class PaymentMethodInfo implements Serializable {
        private static final long serialVersionUID = 1L;

        /** 支付方式类型：BANK_ACCOUNT, MOBILE_WALLET, CASH_PICKUP */
        private String type;

        /** 银行账户信息 */
        private BankAccountInfo bankAccount;

        /** 移动钱包信息 */
        private MobileWalletInfo mobileWallet;
    }

    @Data
    public static class BankAccountInfo implements Serializable {
        private static final long serialVersionUID = 1L;

        private String accountNumber;
        private String bankAccountNumber;
        private String branchNumber;
        private String accountType;
        private String swiftBicCode;
        private String sortCode;
        private String iban;
        private String clabe;
        private String bankName;
    }

    @Data
    public static class MobileWalletInfo implements Serializable {
        private static final long serialVersionUID = 1L;

        private String phoneNumber;
        private String provider;
    }

    @Data
    public static class ComplianceInfo implements Serializable {
        private static final long serialVersionUID = 1L;

        /** 汇款目的 */
        private String purposeOfRemittance;

        /** 资金来源 */
        private String sourceOfFunds;

        /** 与收款方关系 */
        private String relationship;

        /** 外部参考代码 */
        private String externalCode;

        /** 文档参考号 */
        private String documentReferenceNumber;

        /** 回调URL */
        private String callbackUrl;
    }
}
