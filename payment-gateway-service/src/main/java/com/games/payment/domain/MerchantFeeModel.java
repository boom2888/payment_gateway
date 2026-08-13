package com.games.payment.domain;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import com.games.common.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

/**
 * 商户收费模型对象 merchant_fee_model
 *
 * @author Ticker
 * @date 2025-07-16
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("merchant_fee_model")
public class MerchantFeeModel  {


    /** 编号 */
    @TableId(value = "id")
    private Long id;

    /** 模型名 */
    @Excel(name = "模型名", langKey = "merchant.feeModel.name")
    private String name;

    /** 欧洲美元时的收费比例 */
    @Excel(name = "欧洲美元时的收费比例", langKey = "merchant.feeModel.processFeeEuUsd")
    private BigDecimal processFeeEuUsd;

    /** 欧洲非美元时的收费比例 */
    @Excel(name = "欧洲非美元时的收费比例", langKey = "merchant.feeModel.processFeeEuNonUsd")
    private BigDecimal processFeeEuNonUsd;

    /** 非欧洲美元时的收费比例 */
    @Excel(name = "非欧洲美元时的收费比例", langKey = "merchant.feeModel.processFeeNonEuUsd")
    private BigDecimal processFeeNonEuUsd;

    /** 非欧洲非美元时的收费比例 */
    @Excel(name = "非欧洲非美元时的收费比例", langKey = "merchant.feeModel.processFeeNonEuNonUsd")
    private BigDecimal processFeeNonEuNonUsd;

    /** 网关费阈值 */
    @Excel(name = "网关费阈值", langKey = "merchant.feeModel.gatewayFeeThreshold")
    private BigDecimal gatewayFeeThreshold;

    /** 小于网关阈值时收费金额 */
    @Excel(name = "小于网关阈值时收费金额", langKey = "merchant.feeModel.gatewayFeeBelowThreshold")
    private BigDecimal gatewayFeeBelowThreshold;

    /** 大于等于网关阈值时收费金额 */
    @Excel(name = "大于等于网关阈值时收费金额", langKey = "merchant.feeModel.gatewayFeeAboveThreshold")
    private BigDecimal gatewayFeeAboveThreshold;

    /** 商户的循环保证金退还比例 */
    @Excel(name = "商户的循环保证金退还比例", langKey = "merchant.feeModel.rollingReserveRate")
    private BigDecimal rollingReserveRate;

    /** 商户的循环保证金收费天数 */
    @Excel(name = "商户的循环保证金收费天数", langKey = "merchant.feeModel.rollingReserveReleaseDays")
    private Integer rollingReserveReleaseDays;

    /** 汇率扣点，例如35表示扣0.35% */
    @Excel(name = "汇率扣点，例如35表示扣0.35%", langKey = "merchant.feeModel.exchangeRateBps")
    private Long exchangeRateBps;

    /** 退款时,收取的手续费 */
    @Excel(name = "退款时,收取的手续费", langKey = "merchant.feeModel.refundFee")
    private BigDecimal refundFee;

    /** 争议交易 */
    @Excel(name = "争议交易", langKey = "merchant.feeModel.rdrFee")
    private BigDecimal rdrFee;

    /** 拒付 */
    @Excel(name = "拒付", langKey = "merchant.feeModel.cbFee")
    private BigDecimal cbFee;


    /** 创建人id */
    private Long createdBy;

    /** 创建时间 */
    @Excel(name = "创建时间", langKey = "merchant.feeModel.createdAt", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /** 更新人id */
    private Long updateBy;

    /** 更新时间 */
    private Date updateAt;

    /** 删除时间 */
    private Date deletedAt;

    private String remark;

    /** 是否删除(0-未删除，1-已删除) */
    @Excel(name = "是否删除(0-未删除，1-已删除)", langKey = "merchant.feeModel.deleted")
    @TableLogic
    private Long deleted;

    public BigDecimal calculateRollingFee(BigDecimal orderAmount) {
        return orderAmount.multiply(rollingReserveRate).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
    }

    public BigDecimal calculateGatewayFee(BigDecimal orderAmount) {
        if(orderAmount.compareTo(gatewayFeeThreshold) < 0){
            return gatewayFeeBelowThreshold;
        }
        return gatewayFeeAboveThreshold;
    }

    @TableField(exist = false)
    private Integer shopNum;
}
