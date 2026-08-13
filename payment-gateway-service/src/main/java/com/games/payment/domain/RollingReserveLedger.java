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
 * 滚动准备金分类账对象 rolling_reserve_ledger
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("rolling_reserve_ledger")
public class RollingReserveLedger  {

private static final long serialVersionUID=1L;


    /** 主键ID */
    @TableId(value = "id")
    private Long id;

    /** 结算ID */
    @Excel(name = "结算ID")
    private String settlementId;

    /** 商户ID */
    @Excel(name = "商户ID")
    private Long merchantId;

    /** 交易日期 */
    @Excel(name = "交易日期" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date transactionDate;

    /** 交易类型 */
    @Excel(name = "交易类型")
    private Long transactionType;

    /** 参考号 */
    @Excel(name = "参考号")
    private Long reference;

    /** 滚动余额 */
    @Excel(name = "滚动余额")
    private BigDecimal rollingBalance;

    /** 支付日期 */
    @Excel(name = "支付日期" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payoutDate;

}
