package com.games.payment.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.games.common.annotation.Excel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("`refund_order`")
public class RefundOrder {

    /** 退款id */
    @TableId(value = "id")
    private Long id;

    /** 商户 id */
    @Excel(name = "商户 ID", langKey = "payment.refundOrder.excel.merchantId")
    private Long merchantId;

    /** 商户名称 */
    @TableField(exist = false)
    @Excel(name = "商户名称", langKey = "payment.refundOrder.excel.merchantName")
    private String merchantName;

    /** 订单id */
    @Excel(name = "订单 ID", langKey = "payment.refundOrder.excel.orderId")
    private Long orderId;

    /** 商户退款订单 */
    @Excel(name = "商户退款订单", langKey = "payment.refundOrder.excel.refundSsoId")
    private String refundSsoId;

    /** 银行交易流水号 */
    @Excel(name = "银行交易流水号", langKey = "payment.refundOrder.excel.transactionId")
    private String transactionId;

    /** 订单退款金额 */
    @Excel(name = "订单退款金额", langKey = "payment.refundOrder.excel.refundAmount")
    private BigDecimal refundAmount;

    /** 可退款金额 */
    @Excel(name = "可退款金额", langKey = "payment.refundOrder.excel.remainingRefundableAmount")
    private BigDecimal remainingRefundableAmount;

    /** 订单退款币种 */
    @Excel(name = "订单退款币种", langKey = "payment.refundOrder.excel.refundCurrency", dictType = "CURRENCY_ID")
    private Long refundCurrencyId;

    /** 退款状态-0:success,1:fail,2:processing */
    @Excel(name = "退款状态", langKey = "payment.refundOrder.excel.status", dictType = "REFUND_ORDER_STATUS")
    private Integer status;

    /** 创建时间 */
    @Excel(name = "退款时间", langKey = "payment.refundOrder.excel.refundTime", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createAt;

    /** 失败原因 */
    @Excel(name = "失败原因", langKey = "payment.refundOrder.excel.failReason")
    private String failReason;

    /** 查询条件：创建时间开始（按天） */
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date beginTime;

    /** 查询条件：创建时间结束（按天） */
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    /** 查询条件：导出选中ID列表 */
    @TableField(exist = false)
    private List<Long> ids;

    /** 支付时间 */
    @TableField(exist = false)
    @Excel(name = "支付时间", langKey = "payment.refundOrder.excel.payTime", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;

    /** 支付法币金额 */
    @TableField(exist = false)
    @Excel(name = "支付金额", langKey = "payment.refundOrder.excel.fiatAmount")
    private BigDecimal fiatAmount;

}
