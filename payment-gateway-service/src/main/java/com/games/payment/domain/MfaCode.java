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
 * MFA代码对象 mfa_code
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("mfa_code")
public class MfaCode  {

private static final long serialVersionUID=1L;


    /** MFA代码ID */
    @TableId(value = "id")
    private Long id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 6位验证码 */
    @Excel(name = "6位验证码")
    private String code;

    /** OTP ASCII */
    @Excel(name = "OTP ASCII")
    private String otpAscii;

    /** OTP十六进制 */
    @Excel(name = "OTP十六进制")
    private String otpHex;

    /** OTP Base32 */
    @Excel(name = "OTP Base32")
    private String otpBase32;

    /** OTP认证URL */
    @Excel(name = "OTP认证URL")
    private String otpAuthUrl;

    /** 过期时间 */
    @Excel(name = "过期时间" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expireAt;

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

    /** 类型(0-默认，1-已删除) */
    @Excel(name = "类型(0-默认，1-已删除)")
    private Long deleted;

    /** 记录描述 */
    @Excel(name = "记录描述")
    private String remark;

}
