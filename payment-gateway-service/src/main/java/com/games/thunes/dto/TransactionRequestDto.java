package com.games.thunes.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Thunes 交易请求数据传输对象
 *
 * @author System
 * @date 2025-01-08
 */
@Data
public class TransactionRequestDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 外部订单号 */
    @JsonProperty("external_id")
    private String externalId;

    /** 收款方标识信息 */
    @JsonProperty("credit_party_identifier")
    private CreditPartyIdentifierDto creditPartyIdentifier;

    /** 发送方信息 */
    private SenderDto sender;

    /** 发送方企业信息 - B2C/B2B 交易必需 */
    @JsonProperty("sending_business")
    private SendingBusinessDto sendingBusiness;

    /** 收款方企业信息 - C2B/B2B 交易必需 */
    @JsonProperty("receiving_business")
    private ReceivingBusinessDto receivingBusiness;

    /** 收款方信息 */
    private BeneficiaryDto beneficiary;

    /** 回调URL */
    @JsonProperty("callback_url")
    private String callbackUrl;

    /** 零售汇率 */
    @JsonProperty("retail_rate")
    private BigDecimal retailRate;

    /** 零售手续费 */
    @JsonProperty("retail_fee")
    private BigDecimal retailFee;

    /** 零售手续费货币 */
    @JsonProperty("retail_fee_currency")
    private String retailFeeCurrency;

    /** 汇款目的 */
    @JsonProperty("purpose_of_remittance")
    private String purposeOfRemittance;

    /** 文档参考号 */
    @JsonProperty("document_reference_number")
    private String documentReferenceNumber;

    /** 附加信息1 */
    @JsonProperty("additional_information_1")
    private String additionalInformation1;

    /** 附加信息2 */
    @JsonProperty("additional_information_2")
    private String additionalInformation2;

    /** 附加信息3 */
    @JsonProperty("additional_information_3")
    private String additionalInformation3;

    /** 参考信息 */
    private String reference;

    @Data
    public static class CreditPartyIdentifierDto implements Serializable {
        private static final long serialVersionUID = 1L;

        /** 手机号 */
        private String msisdn;

        /** 银行账号 */
        @JsonProperty("bank_account_number")
        private String bankAccountNumber;

        /** ABA 路由号（美国） */
        @JsonProperty("aba_routing_number")
        private String abaRoutingNumber;

        /** 分行号 */
        @JsonProperty("branch_number")
        private String branchNumber;

        /** 账户类型 */
        @JsonProperty("account_type")
        private String accountType;

        /** SWIFT BIC 代码 */
        @JsonProperty("swift_bic_code")
        private String swiftBicCode;

        /** 排序代码（英国） */
        @JsonProperty("sort_code")
        private String sortCode;

        /** 账号（英国） */
        @JsonProperty("account_number")
        private String accountNumber;

        /** IBAN（欧洲） */
        private String iban;

        /** CLABE（墨西哥） */
        private String clabe;
    }

    @Data
    public static class SenderDto implements Serializable {
        private static final long serialVersionUID = 1L;

        private String lastname;

        @JsonProperty("lastname2")
        private String lastname2;

        @JsonProperty("middlename")
        private String middlename;

        private String firstname;

        @JsonProperty("nativename")
        private String nativename;

        @JsonProperty("nationality_country_iso_code")
        private String nationalityCountryIsoCode;

        @JsonProperty("date_of_birth")
        private String dateOfBirth;

        @JsonProperty("country_of_birth_iso_code")
        private String countryOfBirthIsoCode;

        private String gender;
        private String address;

        @JsonProperty("postal_code")
        private String postalCode;

        private String city;

        @JsonProperty("country_iso_code")
        private String countryIsoCode;

        private String msisdn;
        private String email;

        @JsonProperty("id_type")
        private String idType;

        @JsonProperty("id_country_iso_code")
        private String idCountryIsoCode;

        @JsonProperty("id_number")
        private String idNumber;

        @JsonProperty("id_delivery_date")
        private String idDeliveryDate;

        @JsonProperty("id_expiration_date")
        private String idExpirationDate;

        private String occupation;

        @JsonProperty("bank_account_number")
        private String bankAccountNumber;

        @JsonProperty("province_state")
        private String provinceState;

        @JsonProperty("beneficiary_relationship")
        private String beneficiaryRelationship;

        @JsonProperty("source_of_funds")
        private String sourceOfFunds;

        // B2C/B2B 交易所需的实体字段
        /** 注册名称（公司名称） - B2C/B2B 必需 */
        @JsonProperty("registered_name")
        private String registeredName;

        /** 实体代码 - B2C/B2B 必需 */
        private String code;

        /** 注册号码（营业执照号等） - B2C/B2B 必需 */
        @JsonProperty("registration_number")
        private String registrationNumber;

        /** 税务ID - 可选 */
        @JsonProperty("tax_id")
        private String taxId;
    }

    @Data
    public static class BeneficiaryDto implements Serializable {
        private static final long serialVersionUID = 1L;

        private String lastname;

        @JsonProperty("lastname2")
        private String lastname2;

        @JsonProperty("middlename")
        private String middlename;

        private String firstname;

        @JsonProperty("nativename")
        private String nativename;

        @JsonProperty("nationality_country_iso_code")
        private String nationalityCountryIsoCode;

        private String code;

        @JsonProperty("date_of_birth")
        private String dateOfBirth;

        @JsonProperty("country_of_birth_iso_code")
        private String countryOfBirthIsoCode;

        private String gender;
        private String address;

        @JsonProperty("postal_code")
        private String postalCode;

        private String city;

        @JsonProperty("country_iso_code")
        private String countryIsoCode;

        private String msisdn;
        private String email;

        @JsonProperty("id_type")
        private String idType;

        @JsonProperty("id_country_iso_code")
        private String idCountryIsoCode;

        @JsonProperty("id_number")
        private String idNumber;

        @JsonProperty("id_delivery_date")
        private String idDeliveryDate;

        @JsonProperty("id_expiration_date")
        private String idExpirationDate;

        private String occupation;

        @JsonProperty("bank_account_holder_name")
        private String bankAccountHolderName;

        @JsonProperty("province_state")
        private String provinceState;
    }

    @Data
    public static class SendingBusinessDto implements Serializable {
        private static final long serialVersionUID = 1L;

        /** 注册名称（公司名称） */
        @JsonProperty("registered_name")
        private String registeredName;

        /** 交易名称（品牌名称） */
        @JsonProperty("trading_name")
        private String tradingName;

        /** 地址 */
        private String address;

        /** 邮政编码 */
        @JsonProperty("postal_code")
        private String postalCode;

        /** 城市 */
        private String city;

        /** 省份/州 */
        @JsonProperty("province_state")
        private String provinceState;

        /** 国家代码 */
        @JsonProperty("country_iso_code")
        private String countryIsoCode;

        /** 手机号（国际格式） */
        private String msisdn;

        /** 邮箱地址 */
        private String email;

        /** 注册号码（营业执照号等） */
        @JsonProperty("registration_number")
        private String registrationNumber;

        /** 发送方企业识别代码 */
        private String code;

        /** 税务标识符 */
        @JsonProperty("tax_id")
        private String taxId;

        /** 企业成立日期 (ISO 8601 格式: YYYY-MM-DD) */
        @JsonProperty("date_of_incorporation")
        private String dateOfIncorporation;

        /** 代表人姓氏 */
        @JsonProperty("representative_lastname")
        private String representativeLastname;

        /** 代表人附加姓氏 */
        @JsonProperty("representative_lastname2")
        private String representativeLastname2;

        /** 代表人名字 */
        @JsonProperty("representative_firstname")
        private String representativeFirstname;

        /** 代表人中间名 */
        @JsonProperty("representative_middlename")
        private String representativeMiddlename;

        /** 代表人本地全名 */
        @JsonProperty("representative_nativename")
        private String representativeNativename;

        /** 代表人证件类型 */
        @JsonProperty("representative_id_type")
        private String representativeIdType;

        /** 代表人证件签发国家代码 */
        @JsonProperty("representative_id_country_iso_code")
        private String representativeIdCountryIsoCode;

        /** 代表人证件号码 */
        @JsonProperty("representative_id_number")
        private String representativeIdNumber;

        /** 代表人证件签发日期 (ISO 8601 格式) */
        @JsonProperty("representative_id_delivery_date")
        private String representativeIdDeliveryDate;

        /** 代表人证件到期日期 (ISO 8601 格式) */
        @JsonProperty("representative_id_expiration_date")
        private String representativeIdExpirationDate;

        /** 与收款方的业务关系 */
        @JsonProperty("business_relationship")
        private String businessRelationship;
    }

    /**
     * 收款方企业信息 - C2B/B2B 交易必需
     */
    @Data
    public static class ReceivingBusinessDto implements Serializable {
        private static final long serialVersionUID = 1L;

        /** 注册名称（公司名称） */
        @JsonProperty("registered_name")
        private String registeredName;

        /** 交易名称（品牌名称） */
        @JsonProperty("trading_name")
        private String tradingName;

        /** 地址 */
        private String address;

        /** 邮政编码 */
        @JsonProperty("postal_code")
        private String postalCode;

        /** 城市 */
        private String city;

        /** 省份/州 */
        @JsonProperty("province_state")
        private String provinceState;

        /** 国家代码 */
        @JsonProperty("country_iso_code")
        private String countryIsoCode;

        /** 手机号 */
        private String msisdn;

        /** 邮箱 */
        private String email;

        /** 注册号码（营业执照号等） */
        @JsonProperty("registration_number")
        private String registrationNumber;

        /** 税务ID */
        @JsonProperty("tax_id")
        private String taxId;

        /** 成立日期 */
        @JsonProperty("date_of_incorporation")
        private String dateOfIncorporation;

        /** 代表人姓氏 */
        @JsonProperty("representative_lastname")
        private String representativeLastname;

        /** 代表人附加姓氏 */
        @JsonProperty("representative_lastname2")
        private String representativeLastname2;

        /** 代表人名字 */
        @JsonProperty("representative_firstname")
        private String representativeFirstname;

        /** 代表人中间名 */
        @JsonProperty("representative_middlename")
        private String representativeMiddlename;

        /** 代表人本地全名 */
        @JsonProperty("representative_nativename")
        private String representativeNativename;

        /** 代表人证件类型 */
        @JsonProperty("representative_id_type")
        private String representativeIdType;

        /** 代表人证件国家代码 */
        @JsonProperty("representative_id_country_iso_code")
        private String representativeIdCountryIsoCode;

        /** 代表人证件号码 */
        @JsonProperty("representative_id_number")
        private String representativeIdNumber;

        /** 代表人证件签发日期 */
        @JsonProperty("representative_id_delivery_date")
        private String representativeIdDeliveryDate;

        /** 代表人证件到期日期 */
        @JsonProperty("representative_id_expiration_date")
        private String representativeIdExpirationDate;
    }
}
