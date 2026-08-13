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
 * 结算分类账对象 settlement_ledger
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("settlement_ledger")
public class SettlementLedger  {

private static final long serialVersionUID=1L;


    /** 主键ID */
    @TableId(value = "id")
    private Long id;

    /** 对账ID */
    @Excel(name = "对账ID")
    private Long reconId;

    /** 商户ID */
    @Excel(name = "商户ID")
    private Long merchantId;

    /** 结算金额 */
    @Excel(name = "结算金额")
    private BigDecimal settlementAmount;

    /** 拒付退款金额 */
    @Excel(name = "拒付退款金额")
    private BigDecimal chargebacksRefundsAmount;

    /** 滚动准备金扣除 */
    @Excel(name = "滚动准备金扣除")
    private BigDecimal rollingReserveDeducted;

    /** 滚动准备金支付 */
    @Excel(name = "滚动准备金支付")
    private BigDecimal rollingReservePayout;

    /** 支付金额 */
    @Excel(name = "支付金额")
    private BigDecimal payout;

    /** 备注 */
    @Excel(name = "备注")
    private String notes;

    /** 创建时间 */
    @Excel(name = "创建时间" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 创建者ID */
    @Excel(name = "创建者ID")
    private Long createdBy;

    /** 删除时间 */
    @Excel(name = "删除时间" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deletedAt;

    /** 删除者ID */
    @Excel(name = "删除者ID")
    private Long deletedBy;

    /** 记录描述 */
    @Excel(name = "记录描述")
    private String remark;

}
