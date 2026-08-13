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
 * 平台钱包对象 house_wallet
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("house_wallet")
public class HouseWallet  {

private static final long serialVersionUID=1L;


    /** 订单ID */
    @TableId(value = "id")
    private Long id;

    /** 加密货币ID */
    @Excel(name = "加密货币ID")
    private Long cryptoId;

    /** 钱包地址 */
    @Excel(name = "钱包地址")
    private String walletAddress;

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

    /** 钱包名称 */
    @Excel(name = "钱包名称")
    private String walletName;

    /** SaaS用户公司ID */
    @Excel(name = "SaaS用户公司ID")
    private Long saasUserCorporationId;

    /** 是否默认(0-否，1-是) */
    @Excel(name = "是否默认(0-否，1-是)")
    private Long isDefault;

}
