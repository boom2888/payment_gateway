package com.games.payment.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import com.games.common.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

/**
 * 企业客户公司成员对象 business_customer_corporation_member
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("business_customer_corporation_member")
public class BusinessCustomerCorporationMember  {

private static final long serialVersionUID=1L;


    /** 用户ID */
    @TableId(value = "id")
    private Long id;

    /** 企业客户公司ID */
    @Excel(name = "企业客户公司ID", langKey = "payment.businessCustomerCorporationMember.excel.businessCustomerCorporationId")
    private Long businessCustomerCorporationId;

    /** 名字 */
    @Excel(name = "名字", langKey = "payment.businessCustomerCorporationMember.excel.firstName")
    private String firstName;

    /** 中间名 */
    @Excel(name = "中间名", langKey = "payment.businessCustomerCorporationMember.excel.middleName")
    private String middleName;

    /** 姓氏 */
    @Excel(name = "姓氏", langKey = "payment.businessCustomerCorporationMember.excel.lastName")
    private String lastName;

    /** 生日 */
    @Excel(name = "生日", langKey = "payment.businessCustomerCorporationMember.excel.birthday", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date birthday;

    /** 国籍 */
    @Excel(name = "国籍", langKey = "payment.businessCustomerCorporationMember.excel.nationality")
    private String nationality;

    /** 身份证件类型(0-护照，1-驾驶证，2-身份证，3-居留证) */
    @Excel(name = "身份证件类型(0-护照，1-驾驶证，2-身份证，3-居留证)", langKey = "payment.businessCustomerCorporationMember.excel.documentType")
    private Long documentType;

    /** 身份证件号码 */
    @Excel(name = "身份证件号码", langKey = "payment.businessCustomerCorporationMember.excel.documentNo")
    private String documentNo;

    /** 身份证件正面URL */
    @Excel(name = "身份证件正面URL", langKey = "payment.businessCustomerCorporationMember.excel.documentFrontUrl")
    private String documentFrontUrl;

    /** 身份证件背面URL */
    @Excel(name = "身份证件背面URL", langKey = "payment.businessCustomerCorporationMember.excel.documentBackUrl")
    private String documentBackUrl;

    /** 手持身份证件URL */
    @Excel(name = "手持身份证件URL", langKey = "payment.businessCustomerCorporationMember.excel.documentHandyUrl")
    private String documentHandyUrl;

    /** 企业客户类型(0-董事，1-股东) */
    @Excel(name = "企业客户类型(0-董事，1-股东)", langKey = "payment.businessCustomerCorporationMember.excel.memberType")
    private Long memberType;

    /** 企业客户邮箱地址 */
    @Excel(name = "企业客户邮箱地址", langKey = "payment.businessCustomerCorporationMember.excel.email")
    private String email;

    /** 国家代码 */
    @Excel(name = "国家代码", langKey = "payment.businessCustomerCorporationMember.excel.countryCode")
    private String countryCode;

    /** 企业客户电话号码 */
    @Excel(name = "企业客户电话号码", langKey = "payment.businessCustomerCorporationMember.excel.phoneNumber")
    private String phoneNumber;

    /** 企业客户地址 */
    @Excel(name = "企业客户地址", langKey = "payment.businessCustomerCorporationMember.excel.address")
    private String address;

    /** 企业客户城市 */
    @Excel(name = "企业客户城市", langKey = "payment.businessCustomerCorporationMember.excel.city")
    private String city;

    /** 企业客户邮编 */
    @Excel(name = "企业客户邮编", langKey = "payment.businessCustomerCorporationMember.excel.postCode")
    private String postCode;

    /** 企业客户国家 */
    @Excel(name = "企业客户国家", langKey = "payment.businessCustomerCorporationMember.excel.country")
    private String country;

    /** 企业客户KYC Veriff UUID */
    @Excel(name = "企业客户KYC Veriff UUID", langKey = "payment.businessCustomerCorporationMember.excel.kycVeriffUuid")
    private String kycVeriffUuid;

    /** 企业客户KYC状态 */
    @Excel(name = "企业客户KYC状态", langKey = "payment.businessCustomerCorporationMember.excel.kycStatus")
    private Long kycStatus;

    /** 记录创建时间 */
    @Excel(name = "记录创建时间", langKey = "payment.businessCustomerCorporationMember.excel.createdAt", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 创建记录的用户ID */
    @Excel(name = "创建记录的用户ID", langKey = "payment.businessCustomerCorporationMember.excel.createdBy")
    private Long createdBy;

    /** 记录删除时间 */
    @Excel(name = "记录删除时间", langKey = "payment.businessCustomerCorporationMember.excel.deletedAt", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deletedAt;

    /** 最后删除记录的用户ID */
    @Excel(name = "最后删除记录的用户ID", langKey = "payment.businessCustomerCorporationMember.excel.deletedBy")
    private Long deletedBy;

    /** 记录描述 */
    @Excel(name = "记录描述", langKey = "payment.businessCustomerCorporationMember.excel.remark")
    private String remark;

    /** 类型(0-默认，1-已删除) */
    @Excel(name = "类型(0-默认，1-已删除)", langKey = "payment.businessCustomerCorporationMember.excel.deleted")
    private Long deleted;

    /** 企业名称 */
    @Excel(name = "企业名称", langKey = "payment.businessCustomerCorporationMember.excel.businessName")
    private String businessName;

    /** 企业注册号 */
    @Excel(name = "企业注册号", langKey = "payment.businessCustomerCorporationMember.excel.businessRegistrationNo")
    private String businessRegistrationNo;

}
