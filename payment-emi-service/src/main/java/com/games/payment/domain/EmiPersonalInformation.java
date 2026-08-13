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
import java.time.LocalDate;
import java.util.Date;

/**
 * 个人信息对象 emi_personal_information
 *
 * @author Ticker
 * @date 2025-10-16
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("emi_personal_information")
public class EmiPersonalInformation  {

private static final long serialVersionUID=1L;


    /** 主键ID，自增长 */
    @TableId(value = "id")
    private Long id;

    /** 账户类型（必填） */
    @Excel(name = "账户类型" , readConverterExp = "必=填")
    private String accountType;

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

    /** 性别 */
    @Excel(name = "性别")
    private String gender;

    /** 出生日期 */
    @Excel(name = "出生日期" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate birthDate;

    /** 居住国家 */
    @Excel(name = "居住国家")
    private String residenceCountry;

    /** 居住省市 */
    @Excel(name = "居住省市")
    private String residenceProvinceCity;

    /** 居住地址 */
    @Excel(name = "居住地址")
    private String residenceAddress;

    /** 居住邮编 */
    @Excel(name = "居住邮编")
    private String residencePostalCode;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String contactPhone;

    /** 资金来源 */
    @Excel(name = "资金来源")
    private String fundSource;

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

    /** 状态 */
    private Integer status;

    private Integer enabledStatus;

    private String cardNumber;

    private String bank;

    @TableField(exist = false)
    private String account;
}
