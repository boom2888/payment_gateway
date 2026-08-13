package com.games.payment.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import com.games.common.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

import java.util.Date;

/**
 * 【请填写功能名称】对象 saas_user_corporation_supported_currency
 *
 * @author Ticker
 * @date 2025-07-06
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("saas_user_corporation_supported_currency")
public class SaasUserCorporationSupportedCurrency  {

private static final long serialVersionUID=1L;


    /** SaaS User Supported Currency ID */
    @TableId(value = "id")
    private Long id;

    /** SaaS User ID */
    @Excel(name = "SaaS User ID")
    private Long saasUserCorporationId;

    /** Currency ID */
    @Excel(name = "Currency ID")
    private Long currencyId;

    /** Currency ID */
    @Excel(name = "Currency ID")
    private Integer isBasicCurrency;

    /** Datetime when record is created */
    @Excel(name = "Datetime when record is created" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** User ID who created this record */
    @Excel(name = "User ID who created this record")
    private Long createdBy;

    /** Datetime when record is deleted */
    @Excel(name = "Datetime when record is deleted" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deletedAt;

    /** User ID who deleted this record at the last time */
    @Excel(name = "User ID who deleted this record at the last time")
    private Long deletedBy;

    /** Type: 0-DEFAULT, 1-DELETED */
    @Excel(name = "Type: 0-DEFAULT, 1-DELETED")
    private Long deleted;

    /** Record description */
    @Excel(name = "Record description")
    private String remark;

    /**  checkout currency account */
    @Excel(name = " checkout currency account")
    private String currencyAccountCheckout;
}
