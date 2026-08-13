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
 * SaaS用户公司支持货币对象 saas_corp_currency
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("saas_corp_currency")
public class SaasCorpCurrency  {

private static final long serialVersionUID=1L;


    /** SaaS用户支持货币ID */
    @TableId(value = "id")
    private Long id;

    /** SaaS用户ID */
    @Excel(name = "SaaS用户ID")
    private Long saasUserCorporationId;

    /** 货币ID */
    @Excel(name = "货币ID")
    private Long currencyId;

    /** 是否为基础货币 */
    @Excel(name = "是否为基础货币")
    private Integer isBasicCurrency;

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

    /** Checkout货币账户 */
    @Excel(name = "Checkout货币账户")
    private String currencyAccountCheckout;

}
