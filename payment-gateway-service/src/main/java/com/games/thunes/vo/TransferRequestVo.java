package com.games.thunes.vo;

import lombok.Data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Thunes 转账请求 VO
 *
 * @author System
 * @date 2025-01-08
 */
@Data
public class TransferRequestVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 商户ID */
    @NotNull(message = "merchantId 商户ID不能为空")
    private Long merchantId;

    /** 签名 */
    @NotBlank(message = "signature 签名不能为空")
    private String signature;

    /** 外部订单号 */
    @NotBlank(message = "externalId 外部订单号不能为空")
    private String externalId;

    /** 转账金额 */
    @NotNull(message = "amount 转账金额不能为空")
    @Positive(message = "amount 转账金额必须大于0")
    private BigDecimal amount;

    /** 转账货币 */
    @NotBlank(message = "currency 转账货币不能为空")
    private String currency;

    /** 目标国家代码 */
    @NotBlank(message = "targetCountry 目标国家代码不能为空")
    private String targetCountry;

    /** 目标货币 */
    @NotBlank(message = "targetCurrency 目标货币不能为空")
    private String targetCurrency;

    /** 交易类型 (C2C, B2C, C2B, B2B) */
    @NotBlank(message = "transactionType 交易类型不能为空")
    private String transactionType;

    /** 收款方信息 */
    @Valid
    private BeneficiaryInfoVo beneficiary;

    /** 发送方信息 */
    @Valid
    private SenderInfoVo sender;

    /** 汇款目的 */
    @NotBlank(message = "purposeOfRemittance 汇款目的不能为空")
    @Pattern(regexp = "^(FAMILY_SUPPORT|EDUCATION|GIFT_AND_DONATION|MEDICAL_TREATMENT|MAINTENANCE_EXPENSES|TRAVEL|SMALL_VALUE_REMITTANCE|LIBERALIZED_REMITTANCE|OTHER|CONSTRUCTION_EXPENSES|HOTEL_ACCOMMODATION|ADVERTISING_EXPENSES|ADVISORY_FEES|BUSINESS_INSURANCE|INSURANCE_CLAIMS|DELIVERY_FEES|EXPORTED_GOODS|SERVICE_CHARGES|LOAN_PAYMENT|OFFICE_EXPENSES|PROPERTY_PURCHASE|PROPERTY_RENTAL|ROYALTY_FEES|SHARES_INVESTMENT|FUND_INVESTMENT|TAX_PAYMENT|TRANSPORTATION_FEES|UTILITY_BILLS|PERSONAL_TRANSFER|SALARY_PAYMENT|OTHER_FEES|COMPUTER_SERVICES|REWARD_PAYMENT|INFLUENCER_PAYMENT)$", message = "purposeOfRemittance 汇款目的值无效，支持的值：FAMILY_SUPPORT, EDUCATION, GIFT_AND_DONATION, MEDICAL_TREATMENT, MAINTENANCE_EXPENSES, TRAVEL, SMALL_VALUE_REMITTANCE, LIBERALIZED_REMITTANCE, OTHER, CONSTRUCTION_EXPENSES, HOTEL_ACCOMMODATION, ADVERTISING_EXPENSES, ADVISORY_FEES, BUSINESS_INSURANCE, INSURANCE_CLAIMS, DELIVERY_FEES, EXPORTED_GOODS, SERVICE_CHARGES, LOAN_PAYMENT, OFFICE_EXPENSES, PROPERTY_PURCHASE, PROPERTY_RENTAL, ROYALTY_FEES, SHARES_INVESTMENT, FUND_INVESTMENT, TAX_PAYMENT, TRANSPORTATION_FEES, UTILITY_BILLS, PERSONAL_TRANSFER, SALARY_PAYMENT, OTHER_FEES, COMPUTER_SERVICES, REWARD_PAYMENT, INFLUENCER_PAYMENT")
    private String purposeOfRemittance;

    /** 商户回调URL */
    private String callbackUrl;

    /** 外部参考代码 */
    private String externalCode;

    /** 文档参考号 */
    private String documentReferenceNumber;

    /** 收款方企业信息（C2B/B2B 交易必需） */
    @Valid
    private ReceivingBusinessInfoVo receivingBusiness;

    /** 发送方企业信息（B2C/B2B 交易必需） */
    @Valid
    private SendingBusinessInfoVo sendingBusiness;

    @Data
    public static class BeneficiaryInfoVo implements Serializable {
        private static final long serialVersionUID = 1L;

        /** 收款方姓氏（C2C/B2C 交易必需） */
        private String lastName;

        /** 收款方附加姓氏 */
        private String lastName2;

        /** 收款方中间名 */
        private String middleName;

        /** 收款方名字（C2C/B2C 交易必需） */
        private String firstName;

        /** 收款方本地全名 */
        private String nativeName;

        /** 收款方国籍代码 */
        private String nationalityCountryIsoCode;

        /** 收款方标识代码 */
        private String code;

        /** 出生日期 (ISO 8601 格式: YYYY-MM-DD) */
        private String dateOfBirth;

        /** 出生国家代码 */
        private String countryOfBirthIsoCode;

        /** 性别 */
        private String gender;

        /** 注册名称（B2B/C2B 交易必需） */
        private String registeredName;

        /** 银行账号 */
        private String bankAccountNumber;

        /** ABA 路由号（美国） */
        private String abaRoutingNumber;

        /** 分行号 */
        private String branchNumber;

        /** 账户类型 */
        private String accountType;

        /** SWIFT BIC 代码 */
        private String swiftBicCode;

        /** 排序代码（英国） */
        private String sortCode;

        /** 账号（英国） */
        private String accountNumber;

        /** IBAN（欧洲） */
        private String iban;

        /** CLABE（墨西哥） */
        private String clabe;

        /** 银行账户持有人姓名 */
        private String bankAccountHolderName;

        /** 手机号（移动钱包） */
        private String msisdn;

        /** 地址 */
        private String address;

        /** 邮政编码 */
        private String postalCode;

        /** 城市 */
        private String city;

        /** 省份/州 */
        private String provinceState;

        /** 国家代码 */
        private String countryIsoCode;

        /** 邮箱 */
        private String email;

        /** 证件类型 */
        @Pattern(regexp = "^(PASSPORT|NATIONAL_ID|DRIVING_LICENSE|SOCIAL_SECURITY|TAX_ID|SENIOR_CITIZEN_ID|BIRTH_CERTIFICATE|VILLAGE_ELDER_ID|RESIDENT_CARD|ALIEN_REGISTRATION|PAN_CARD|VOTERS_ID|HEALTH_CARD|EMPLOYER_ID|OTHER)$", message = "beneficiary.idType 证件类型值无效，支持的值：PASSPORT, NATIONAL_ID, DRIVING_LICENSE, SOCIAL_SECURITY, TAX_ID, SENIOR_CITIZEN_ID, BIRTH_CERTIFICATE, VILLAGE_ELDER_ID, RESIDENT_CARD, ALIEN_REGISTRATION, PAN_CARD, VOTERS_ID, HEALTH_CARD, EMPLOYER_ID, OTHER")
        private String idType;

        /** 证件签发国家代码 */
        private String idCountryIsoCode;

        /** 证件号码 */
        private String idNumber;

        /** 证件签发日期 (ISO 8601 格式: YYYY-MM-DD) */
        private String idDeliveryDate;

        /** 证件到期日期 (ISO 8601 格式: YYYY-MM-DD) */
        private String idExpirationDate;

        /** 税务ID */
        private String taxId;

        /** 职业 */
        private String occupation;
    }

    @Data
    public static class SenderInfoVo implements Serializable {
        private static final long serialVersionUID = 1L;

        /** 发送方姓氏（C2C/C2B 交易必需） */
        private String lastName;

        /** 发送方附加姓氏 */
        private String lastName2;

        /** 发送方中间名 */
        private String middleName;

        /** 发送方名字（C2C/C2B 交易必需） */
        private String firstName;

        /** 发送方本地全名 */
        private String nativeName;

        private String nationalityCountryIsoCode;
        
        private String dateOfBirth;
        
        private String countryOfBirthIsoCode;

        @Pattern(regexp = "^(MALE|FEMALE)?$", message = "sender.gender 性别只能是 MALE 或 FEMALE")
        private String gender;

        /** 发送方地址 */
        private String address;

        private String postalCode;

        /** 发送方城市 */
        private String city;

        /** 省份/州 */
        private String provinceState;

        /** 国家代码（B2B/B2C 交易必需） */
        private String countryIsoCode;

        // 注意：源国家固定为 MLT（马耳他），不需要用户传入

        private String msisdn;
        
        private String email;

        @Pattern(regexp = "^(PASSPORT|NATIONAL_ID|DRIVING_LICENSE|SOCIAL_SECURITY|TAX_ID|SENIOR_CITIZEN_ID|BIRTH_CERTIFICATE|VILLAGE_ELDER_ID|RESIDENT_CARD|ALIEN_REGISTRATION|PAN_CARD|VOTERS_ID|HEALTH_CARD|EMPLOYER_ID|OTHER)$", message = "sender.idType 证件类型值无效，支持的值：PASSPORT, NATIONAL_ID, DRIVING_LICENSE, SOCIAL_SECURITY, TAX_ID, SENIOR_CITIZEN_ID, BIRTH_CERTIFICATE, VILLAGE_ELDER_ID, RESIDENT_CARD, ALIEN_REGISTRATION, PAN_CARD, VOTERS_ID, HEALTH_CARD, EMPLOYER_ID, OTHER")
        private String idType;

        private String idCountryIsoCode;

        private String idNumber;

        private String idDeliveryDate;

        private String idExpirationDate;

        private String bankAccountNumber;
        
        private String occupation;

        private String beneficiaryRelationship;

        private String sourceOfFunds;

        // B2C/B2B 交易所需的实体字段（根据交易类型动态验证）
        /** 注册名称（公司名称） - B2C/B2B 必需 */
        private String registeredName;

        /** 实体代码 - B2C/B2B 必需 */
        private String code;

        /** 注册号码（营业执照号等） - B2C/B2B 必需 */
        private String registrationNumber;

        /** 税务ID - 可选 */
        private String taxId;
    }

    /**
     * 收款方企业信息（C2B/B2B 交易必需）
     */
    @Data
    public static class ReceivingBusinessInfoVo implements Serializable {
        private static final long serialVersionUID = 1L;

        /** 注册名称（公司名称） */
        private String registeredName;

        /** 交易名称（品牌名称） */
        private String tradingName;

        /** 地址 */
        private String address;

        /** 邮政编码 */
        private String postalCode;

        /** 城市 */
        private String city;

        /** 省份/州 */
        private String provinceState;

        /** 国家代码 */
        private String countryIsoCode;

        /** 手机号 */
        private String msisdn;

        /** 邮箱 */
        private String email;

        /** 注册号码（营业执照号等） */
        private String registrationNumber;

        /** 税务ID */
        private String taxId;

        /** 成立日期 (ISO 8601 格式: YYYY-MM-DD) */
        private String dateOfIncorporation;

        /** 代表人姓氏 */
        private String representativeLastname;

        /** 代表人附加姓氏 */
        private String representativeLastname2;

        /** 代表人名字 */
        private String representativeFirstname;

        /** 代表人中间名 */
        private String representativeMiddlename;

        /** 代表人本地全名 */
        private String representativeNativename;

        /** 代表人证件类型 */
        @Pattern(regexp = "^(PASSPORT|NATIONAL_ID|DRIVING_LICENSE|SOCIAL_SECURITY|TAX_ID|SENIOR_CITIZEN_ID|BIRTH_CERTIFICATE|VILLAGE_ELDER_ID|RESIDENT_CARD|ALIEN_REGISTRATION|PAN_CARD|VOTERS_ID|HEALTH_CARD|EMPLOYER_ID|OTHER)?$", message = "receivingBusiness.representativeIdType 证件类型值无效")
        private String representativeIdType;

        /** 代表人证件国家代码 */
        private String representativeIdCountryIsoCode;

        /** 代表人证件号码 */
        private String representativeIdNumber;

        /** 代表人证件签发日期 */
        private String representativeIdDeliveryDate;

        /** 代表人证件到期日期 */
        private String representativeIdExpirationDate;

        // ========== 银行账户信息（B2B 交易需要） ==========
        
        /** 银行账号 */
        private String bankAccountNumber;

        /** ABA 路由号（美国） */
        private String abaRoutingNumber;

        /** 分行号 */
        private String branchNumber;

        /** 账户类型 */
        private String accountType;

        /** SWIFT BIC 代码 */
        private String swiftBicCode;

        /** 排序代码（英国） */
        private String sortCode;

        /** 账号（英国） */
        private String accountNumber;

        /** IBAN（欧洲） */
        private String iban;

        /** CLABE（墨西哥） */
        private String clabe;
    }

    /**
     * 发送方企业信息（B2C/B2B 交易必需）
     */
    @Data
    public static class SendingBusinessInfoVo implements Serializable {
        private static final long serialVersionUID = 1L;

        /** 注册名称（公司名称） */
        private String registeredName;

        /** 交易名称（品牌名称） */
        private String tradingName;

        /** 地址 */
        private String address;

        /** 邮政编码 */
        private String postalCode;

        /** 城市 */
        private String city;

        /** 省份/州 */
        private String provinceState;

        /** 国家代码 */
        private String countryIsoCode;

        /** 手机号（国际格式） */
        private String msisdn;

        /** 邮箱地址 */
        private String email;

        /** 注册号码（营业执照号等） */
        private String registrationNumber;

        /** 发送方企业识别代码 */
        private String code;

        /** 税务标识符 */
        private String taxId;

        /** 企业成立日期 (ISO 8601 格式: YYYY-MM-DD) */
        private String dateOfIncorporation;

        /** 代表人姓氏 */
        private String representativeLastname;

        /** 代表人附加姓氏 */
        private String representativeLastname2;

        /** 代表人名字 */
        private String representativeFirstname;

        /** 代表人中间名 */
        private String representativeMiddlename;

        /** 代表人本地全名 */
        private String representativeNativename;

        /** 代表人证件类型 */
        @Pattern(regexp = "^(PASSPORT|NATIONAL_ID|DRIVING_LICENSE|SOCIAL_SECURITY|TAX_ID|SENIOR_CITIZEN_ID|BIRTH_CERTIFICATE|VILLAGE_ELDER_ID|RESIDENT_CARD|ALIEN_REGISTRATION|PAN_CARD|VOTERS_ID|HEALTH_CARD|EMPLOYER_ID|OTHER)?$", message = "sendingBusiness.representativeIdType 证件类型值无效")
        private String representativeIdType;

        /** 代表人证件签发国家代码 */
        private String representativeIdCountryIsoCode;

        /** 代表人证件号码 */
        private String representativeIdNumber;

        /** 代表人证件签发日期 (ISO 8601 格式) */
        private String representativeIdDeliveryDate;

        /** 代表人证件到期日期 (ISO 8601 格式) */
        private String representativeIdExpirationDate;

        /** 与收款方的业务关系 */
        @Pattern(regexp = "^(EMPLOYEE|CUSTOMER|VENDOR_SERVICE|PROVIDER|BUSINESS_PARTNER|INVESTOR|THIRD_PARTY|AFFILIATED_ENTITY|AFFILIATED_BUSINESS)?$", message = "sendingBusiness.businessRelationship 业务关系值无效，支持的值：EMPLOYEE(员工), CUSTOMER(客户), VENDOR_SERVICE(供应商服务), PROVIDER(提供商), BUSINESS_PARTNER(商业伙伴), INVESTOR(投资者), THIRD_PARTY(第三方), AFFILIATED_ENTITY(关联实体), AFFILIATED_BUSINESS(关联企业)")
        private String businessRelationship;
    }
}
