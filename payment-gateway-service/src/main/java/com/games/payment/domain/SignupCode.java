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
 * 注册码对象 signup_code
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("signup_code")
public class SignupCode  {

private static final long serialVersionUID=1L;


    /** 注册码ID */
    @TableId(value = "id")
    private Long id;

    /** 用户注册标识符，可以是邮箱或电话号码 */
    @Excel(name = "用户注册标识符，可以是邮箱或电话号码")
    private String identifier;

    /** SaaS用户ID */
    @Excel(name = "SaaS用户ID")
    private Long saasUserCorporationId;

    /** 6位注册码 */
    @Excel(name = "6位注册码")
    private String code;

    /** 注册密码 */
    @Excel(name = "注册密码")
    private String password;

    /** 注册用户类型 */
    @Excel(name = "注册用户类型")
    private String type;

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

    /** 商户ID */
    @Excel(name = "商户ID")
    private Long merchantId;

    /** 推荐码 */
    @Excel(name = "推荐码")
    private String referralCode;

}
