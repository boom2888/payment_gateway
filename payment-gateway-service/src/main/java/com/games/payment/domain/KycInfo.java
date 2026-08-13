package com.games.payment.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import com.games.common.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;


import java.math.BigDecimal;
import java.util.Date;

/**
 * KYC信息对象 kyc_info
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("kyc_info")
public class KycInfo  {

private static final long serialVersionUID=1L;


    /** KYC个人信息ID */
    @TableId(value = "id")
    private Long id;

    /** 用户ID主键 */
    @Excel(name = "用户ID主键")
    private Long userId;

    /** 名字 */
    @Excel(name = "名字")
    private String firstName;

    /** 中间名 */
    @Excel(name = "中间名")
    private String middleName;

    /** 姓氏 */
    @Excel(name = "姓氏")
    private String lastName;

    /** 生日 */
    @Excel(name = "生日" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date birthday;

    /** 国籍 */
    @Excel(name = "国籍")
    private String nationality;

    /** 身份证件类型(0-护照，1-驾驶证，2-身份证，3-居留证) */
    @Excel(name = "身份证件类型(0-护照，1-驾驶证，2-身份证，3-居留证)")
    private Long documentType;

    /** 身份证件号码 */
    @Excel(name = "身份证件号码")
    private String documentNo;

    /** 身份证件正面URL */
    @Excel(name = "身份证件正面URL")
    private String documentFrontUrl;

    /** 身份证件背面URL */
    @Excel(name = "身份证件背面URL")
    private String documentBackUrl;

    /** 手持身份证件URL */
    @Excel(name = "手持身份证件URL")
    private String documentHandyUrl;

    /** KYC决策webhook载荷 */
    @Excel(name = "KYC决策webhook载荷")
    private String kycPayload;

    /** 监控名单筛查webhook载荷 */
    @Excel(name = "监控名单筛查webhook载荷")
    private String kycAmlPayload;

    /** 记录创建时间 */
    @Excel(name = "记录创建时间" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 创建记录的用户ID */
    @Excel(name = "创建记录的用户ID")
    private Long createdBy;

    /** 记录删除时间 */
    @Excel(name = "记录删除时间" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deletedAt;

    /** 最后删除记录的用户ID */
    @Excel(name = "最后删除记录的用户ID")
    private Long deletedBy;

    /** 类型：0-假，1-真 */
    @Excel(name = "类型：0-假，1-真")
    private Long deleted;

    /** 记录描述 */
    @Excel(name = "记录描述")
    private String remark;

    /** SSO KYC默认为0-假，然后设置SSO KYC为1(0-假，1-真) */
    @Excel(name = "SSO KYC默认为0-假，然后设置SSO KYC为1(0-假，1-真)")
    private Long ssoKyc;

}
