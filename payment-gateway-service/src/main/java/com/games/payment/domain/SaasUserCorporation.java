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
 * SaaS用户公司对象 saas_user_corporation
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("saas_user_corporation")
public class SaasUserCorporation  {

private static final long serialVersionUID=1L;


    /** SaaS用户公司ID */
    @TableId(value = "id")
    private Long id;

    /** 收单机构配置表中的收单机构ID */
    @Excel(name = "收单机构配置表中的收单机构ID")
    private Long acquirerId;

    /** SaaS用户公司代码 */
    @Excel(name = "SaaS用户公司代码")
    private String code;

    /** SaaS用户公司名称 */
    @Excel(name = "SaaS用户公司名称")
    private String name;

    /** SaaS用户公司域名 */
    @Excel(name = "SaaS用户公司域名")
    private String domain;

    /** SaaS用户平台主题配置文件URL */
    @Excel(name = "SaaS用户平台主题配置文件URL")
    private String themeUrl;

    /** SaaS用户合作银行 - MOCK, CLEAR_BANK, BANKING_CIRCLE */
    @Excel(name = "SaaS用户合作银行 - MOCK, CLEAR_BANK, BANKING_CIRCLE")
    private String partnerBank;

    /** SaaS用户合作银行排序代码 */
    @Excel(name = "SaaS用户合作银行排序代码")
    private String partnerBankSortCode;

    /** 验证码通知邮件的模板ID */
    @Excel(name = "验证码通知邮件的模板ID")
    private String emailIdVerifyCode;

    /** Veriff通知邮件的模板ID */
    @Excel(name = "Veriff通知邮件的模板ID")
    private String emailIdVeriff;

    /** Veriff地址通知邮件的模板ID */
    @Excel(name = "Veriff地址通知邮件的模板ID")
    private String emailIdVeriffAddress;

    /** 银行通知邮件的模板ID */
    @Excel(name = "银行通知邮件的模板ID")
    private String emailIdBank;

    /** 重置密码通知邮件的模板ID */
    @Excel(name = "重置密码通知邮件的模板ID")
    private String emailIdForgotPwd;

    /** Veriff KYC邀请邮件的模板ID */
    @Excel(name = "Veriff KYC邀请邮件的模板ID")
    private String emailIdVeriffKyc;

    /** 每个商户的收入分成模式 */
    @Excel(name = "每个商户的收入分成模式")
    private String revenueShare;

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

    /** 地址ID */
    @Excel(name = "地址ID")
    private Long addressId;

    /** Notabene VASP DID */
    @Excel(name = "Notabene VASP DID")
    private String notabeneVaspDid;

    /** Notabene客户端ID */
    @Excel(name = "Notabene客户端ID")
    private String notabeneClientId;

    /** Notabene客户端密钥 */
    @Excel(name = "Notabene客户端密钥")
    private String notabeneClientSecret;

    /** Notabene客户端凭据 */
    @Excel(name = "Notabene客户端凭据")
    private String notabeneClientCredentials;

    /** 邮件模板发送方 */
    @Excel(name = "邮件模板发送方")
    private String emailTemplateSendfrom;

    /** 邮箱地址验证邮件的模板ID */
    @Excel(name = "邮箱地址验证邮件的模板ID")
    private String emailIdEmailAddressVerification;

    /** 法人姓名 */
    @Excel(name = "法人姓名")
    private String legalPersonName;

    /** 订单完成通知邮件的模板ID */
    @Excel(name = "订单完成通知邮件的模板ID")
    private String emailIdOrderCompletionNotification;

    /** 平台钱包提供商 */
    @Excel(name = "平台钱包提供商")
    private String houseWalletProvider;

    /** 旅行规则检查限额 */
    @Excel(name = "旅行规则检查限额")
    private Long travelRuleCheckLimit;

    /** 旅行规则限额货币ID */
    @Excel(name = "旅行规则限额货币ID")
    private Long travelRuleLimitCurrencyId;

    /** Fireblocks提现账户ID */
    @Excel(name = "Fireblocks提现账户ID")
    private Long fireblocksWithdrawalAccountId;

    /** Fireblocks运营账户ID */
    @Excel(name = "Fireblocks运营账户ID")
    private Long fireblocksOperationalAccountId;

    /** Fireblocks在UI中隐藏 */
    @Excel(name = "Fireblocks在UI中隐藏")
    private Long fireblocksHiddenOnUi;

    /** Fireblocks自动加油 */
    @Excel(name = "Fireblocks自动加油")
    private Long fireblocksAutoFuel;

    /** 出金订单交易完成通知邮件的模板ID */
    @Excel(name = "出金订单交易完成通知邮件的模板ID")
    private String emailIdOffOrderCompletionNotification;

    /** 出金订单交易失败通知邮件的模板ID */
    @Excel(name = "出金订单交易失败通知邮件的模板ID")
    private String emailIdOffOrderFailedNotification;

}
