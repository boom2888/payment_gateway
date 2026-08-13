package com.games.pay.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.games.common.annotation.Excel;
import com.games.payment.domain.Order;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 订单对象 order
 *
 * @author Ticker
 * @date 2025-07-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class OrderWithFeeModelDto extends Order {

    @TableField(value = "fee_model_id")
    private String feeModelId;

    /** 模型名 */
    @Excel(name = "模型名")
    private String feeModelName;

    /** 欧洲美元时的收费比例 */
    @Excel(name = "欧洲美元时的收费比例")
    private BigDecimal processFeeEuUsd;

    /** 欧洲非美元时的收费比例 */
    @Excel(name = "欧洲非美元时的收费比例")
    private BigDecimal processFeeEuNonUsd;

    /** 非欧洲美元时的收费比例 */
    @Excel(name = "非欧洲美元时的收费比例")
    private BigDecimal processFeeNonEuUsd;

    /** 非欧洲非美元时的收费比例 */
    @Excel(name = "非欧洲非美元时的收费比例")
    private BigDecimal processFeeNonEuNonUsd;

    /** 网关费阈值 */
    @Excel(name = "网关费阈值")
    private BigDecimal gatewayFeeThreshold;

    /** 小于网关阈值时收费金额 */
    @Excel(name = "小于网关阈值时收费金额")
    private BigDecimal gatewayFeeBelowThreshold;

    /** 大于等于网关阈值时收费金额 */
    @Excel(name = "大于等于网关阈值时收费金额")
    private BigDecimal gatewayFeeAboveThreshold;

    /** 商户的循环保证金退还比例 */
    @Excel(name = "商户的循环保证金退还比例")
    private BigDecimal rollingReserveRate;

    /** 商户的循环保证金收费天数 */
    @Excel(name = "商户的循环保证金收费天数")
    private Long rollingReserveReleaseDays;

    /** 汇率扣点，例如35表示扣0.35% */
    @Excel(name = "汇率扣点，例如35表示扣0.35%")
    private Long exchangeRateBps;

    /** 退款时,收取的手续费 */
    @Excel(name = "退款时,收取的手续费")
    private BigDecimal refundFee;

    /** 争议交易 */
    @Excel(name = "争议交易")
    private BigDecimal rdrFee;

    /** 拒付 */
    @Excel(name = "拒付")
    private BigDecimal cbFee;

}
