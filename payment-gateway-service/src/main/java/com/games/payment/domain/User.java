package com.games.payment.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.games.common.core.domain.entity.SysUser;
import com.games.common.core.domain.model.LoginUser;
import com.games.payment.enums.UserKycStatus;
import com.games.payment.enums.UserType;
import io.swagger.models.auth.In;
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
 * 用户对象 user
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("user")
public class User  {

private static final long serialVersionUID=1L;

    public static User init(String email){
        User user = new User();
        user.setEmail(email);
        user.setType(UserType.PERSONAL.getCode());
        user.setKycStatus(UserKycStatus.NOT_STARTED.getCode());
        user.setCreatedAt(new Date());
        return user;
    }

    public LoginUser toLoginUser(){
        LoginUser loginUser = new LoginUser();
        SysUser sysUser = new SysUser();
        sysUser.setUserId(id);
        sysUser.setEmail(email);
        sysUser.setNickName(displayName);
        loginUser.setUser(sysUser);
        return loginUser;
    }

    /** 用户ID */
    @TableId(value = "id")
    private Long id;

    /** 登录邮箱 */
    @Excel(name = "登录邮箱")
    private String email;

    /** 电话国家代码 */
    @Excel(name = "电话国家代码")
    private String countryCode;

    /** 电话号码 */
    @Excel(name = "电话号码")
    private String phoneNumber;

    /** 登录密码 */
    @Excel(name = "登录密码")
    private String password;

    /** 显示名称，如昵称 */
    @Excel(name = "显示名称，如昵称")
    private String displayName;

    /** 用户类型(0-个人客户，1-企业客户，2-SaaS用户，3-平台用户，4-商户用户) */
    @Excel(name = "用户类型(0-个人客户，1-企业客户，2-SaaS用户，3-平台用户，4-商户用户)")
    private Integer type;

    /** 用户头像文件URL */
    @Excel(name = "用户头像文件URL")
    private String avatar;

    /** MFA设置(0-SMS，1-邮箱，2-认证器) */
    @Excel(name = "MFA设置(0-SMS，1-邮箱，2-认证器)")
    private Long mfaSetting;

    /** 通知设置：3位二进制 */
    @Excel(name = "通知设置：3位二进制")
    private Long notificationSetting;

    /** KYC信息ID */
    @Excel(name = "KYC信息ID")
    private Long kycId;

    /** 传递给Veriff的UUID */
    @Excel(name = "传递给Veriff的UUID")
    private String kycVeriffUuid;

    /** KYC状态(0-未进行，1-已批准，2-身份证待处理，3-AML待处理，4-已拒绝) */
    @Excel(name = "KYC状态(0-未进行，1-已批准，2-身份证待处理，3-AML待处理，4-已拒绝)")
    private Integer kycStatus;

    /** 用户角色默认角色默认客户个人角色 */
    @Excel(name = "用户角色默认角色默认客户个人角色")
    private Long role;

    /** 激活状态(0-待处理，1-已激活，2-已阻止，3-已关闭) */
    @Excel(name = "激活状态(0-待处理，1-已激活，2-已阻止，3-已关闭)")
    private Long activated;

    /** 人类可读ID */
    @Excel(name = "人类可读ID")
    private String idRef;

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
