package com.games.payment.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.games.common.annotation.Excel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商户资产变更记录对象 merchant_account_change_log
 *
 * @author Ticker
 * @date 2025-11-27
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("merchant_account_change_log")
public class MerchantAccountChangeLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 商户ID */
    @Excel(name = "商户ID")
    private String merchantId;

    /** 币种 */
    @Excel(name = "币种")
    private String currency;

    /** 变更前余额 */
    @Excel(name = "变更前余额")
    private BigDecimal balanceBefore;

    /** 变更后余额 */
    @Excel(name = "变更后余额")
    private BigDecimal balanceAfter;

    /** 变更金额 */
    @Excel(name = "变更金额")
    private BigDecimal changeAmount;

    /** 变更类型 */
    @Excel(name = "变更类型")
    private Integer changeType;

    /** 关联的对账报告ID */
    @Excel(name = "对账报告ID")
    private Long movementImportId;

    /** 备注说明 */
    @Excel(name = "备注")
    private String remark;

    /** 创建时间 */
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
}
