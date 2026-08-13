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
 * 收单机构订单对象 acquirer_order
 *
 * @author Ticker
 * @date 2025-07-08
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("acquirer_order")
public class AcquirerOrder  {

private static final long serialVersionUID=1L;


    /** 订单ID */
    @TableId(value = "id")
    private Long id;

    /** 收单机构ID */
    @Excel(name = "收单机构ID")
    private Long acquirerId;

    /** 内部唯一请求ID（用于对账目的） */
    @Excel(name = "内部唯一请求ID" , readConverterExp = "用=于对账目的")
    private String reconRequestId;

    /** 订单ID */
    @Excel(name = "订单ID")
    private Long orderId;

    /** 订单类型 */
    @Excel(name = "订单类型")
    private Long type;

    /** 法币 */
    @Excel(name = "法币")
    private Long currencyId;

    /** 收单金额 */
    @Excel(name = "收单金额")
    private BigDecimal amount;

    /** 支付拒绝载荷：CheckOut支付拒绝webhook载荷 */
    @Excel(name = "支付拒绝载荷：CheckOut支付拒绝webhook载荷")
    private String paymentDeclinedPayload;

    /** 状态 */
    @Excel(name = "状态")
    private Long status;

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

    /** 是否删除 */
    @Excel(name = "是否删除")
    private Long deleted;

    /** 记录描述 */
    @Excel(name = "记录描述")
    private String remark;

    /** 收单机构返回的支付响应载荷 */
    @Excel(name = "收单机构返回的支付响应载荷")
    private String paymentResponsePayload;

    /** customer_card_token的ID */
    @Excel(name = "customer_card_token的ID")
    private Long tokenId;

    /** 商户响应代码 */
    @Excel(name = "商户响应代码")
    private String responseCode;

    /** 访问控制服务器交易ID */
    @Excel(name = "访问控制服务器交易ID")
    private String acsTransactionId;

}
