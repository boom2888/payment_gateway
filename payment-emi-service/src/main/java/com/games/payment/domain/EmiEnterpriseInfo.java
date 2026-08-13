package com.games.payment.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.games.common.annotation.Excel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

/**
 * 企业基本信息及出口业务信息对象 emi_enterprise_info
 *
 * @author Ticker
 * @date 2025-10-16
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("emi_enterprise_info")
public class EmiEnterpriseInfo  {

private static final long serialVersionUID=1L;


    /** 主键ID，自增长 */
    @TableId(value = "id")
    private Long id;

    /** 企业中文名称 */
    @Excel(name = "企业中文名称")
    private String companyNameChinese;

    /** 企业英文名称 */
    @Excel(name = "企业英文名称")
    private String companyNameEnglish;

    /** 商业登记证编号 */
    @Excel(name = "商业登记证编号")
    private String businessRegistrationNumber;

    /** 公司注册代码 */
    @Excel(name = "公司注册代码")
    private String companyRegistrationCode;

    /** 企业注册国家 */
    @Excel(name = "企业注册国家")
    private String registeredCountry;

    /** 企业注册地址 */
    @Excel(name = "企业注册地址")
    private String registeredAddress;

    /** 实际经营地址 */
    @Excel(name = "实际经营地址")
    private String actualOperationAddress;

    /** 企业人数 */
    @Excel(name = "企业人数")
    private Long employeeCount;

    /** 企业成立日期 */
    @Excel(name = "企业成立日期" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd" , timezone = "GMT+8")
    private LocalDate establishmentDate;

    /** 企业到期日期 */
    @Excel(name = "企业到期日期" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd" , timezone = "GMT+8")
    private LocalDate expirationDate;

    /** 行业类型 */
    @Excel(name = "行业类型")
    private String industryType;

    /** 出口类型 */
    @Excel(name = "出口类型")
    private String exportType;

    /** 出口国家和地区 */
    @Excel(name = "出口国家和地区")
    private String exportCountries;

    /** 出口商品名称 */
    @Excel(name = "出口商品名称")
    private String exportProductName;

    /** 历史年出口额 */
    @Excel(name = "历史年出口额")
    private BigDecimal historicalAnnualExport;

    /** 预估年出口额 */
    @Excel(name = "预估年出口额")
    private BigDecimal estimatedAnnualExport;

    /** 企业网站(选填) */
    @Excel(name = "企业网站(选填)")
    private String website;

    /** 记录创建时间 */
    @Excel(name = "记录创建时间" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 记录最后更新时间 */
    @Excel(name = "记录最后更新时间" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;

    /** 国籍 */
    @Excel(name = "国籍")
    private String nationality;

    /** 邮箱账号（必填） */
    @Excel(name = "邮箱账号" , readConverterExp = "必=填")
    private String email;

    /** 证件类型（必填） */
    @Excel(name = "证件类型" , readConverterExp = "必=填")
    private String idType;

    /** 证件照片存储路径 */
    @Excel(name = "证件照片存储路径")
    private String idPhoto;

    /** 姓名（必填） */
    @Excel(name = "姓名" , readConverterExp = "必=填")
    private String fullName;

    /** 证件号（必填） */
    @Excel(name = "证件号" , readConverterExp = "必=填")
    private String idNumber;

    /** 商业登记证书（必填） */

    private String businessRegistration;

    /** 公司注册证书（必填） */

    private String companyRegistration;

    /** 法团成立表格 */

    private String incorporation;

    /** 办公场景（必填） */

    private String officeScene;

    /** 状态 */
    private Integer status;

    /** binner实体id */
    private Integer entityId;

    private Integer enabledStatus;

    private String cardNumber;

    private String bank;

    @TableField(exist = false)
    private String account;

}
