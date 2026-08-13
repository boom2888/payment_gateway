package com.games.api.dto;


import com.games.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("Pay Order DTO")
public class PayOrderDto extends BaseDto{

    @ApiModelProperty(value = "Cryptocurrency amount", example = "10.0", required = true)
    private BigDecimal cryptoAmount;

    @ApiModelProperty(value = "Cryptocurrency ID", example = "4", required = true)
    private String crypto;

    @ApiModelProperty(value = "Fiat currency ID", example = "108", required = true)
    private String currency;

    @ApiModelProperty(value = "Fiat amount", example = "9.1", required = true)
    private BigDecimal fiatAmount;

    @ApiModelProperty(value = "Liquidity quote", example = "1.0", required = true)
    private BigDecimal liquidityQuote;

    @ApiModelProperty(value = "Merchant ID", example = "1", required = true)
    private Integer merchantId;

    @ApiModelProperty(value = "Unique system order UUID", example = "973278c5-7ea7-4171-b9b1-dbbda26b3dc2", required = true)
    private String orderNo;
//    private String orderUuid;

    @ApiModelProperty(value = "Processing fee", example = "0.9", required = true)
    private BigDecimal processingFee;

    @ApiModelProperty(value = "Merchant order ID", example = "TEST-0c8671", required = true)
    private String ssoOrderId;

    /**
     * Order status：
     * 0-创建，1-AML 开始，2-AML 暂停，3-AML 拒绝，
     * 4-待入金，5-处理中，6-入金失败，7-取消，8-已执行，9-完成
     */
    @ApiModelProperty(value = "Order status", example = "4", required = true)
    private String status;

    /**
     * 订单类型：
     * 0: 买入（buy），1: 卖出（sell），2: 其他（例如 NFT）
     */
    @ApiModelProperty(value = "Order type: 0=buy, 1=sell", example = "2", required = true)
    private String type;

    /** Failure reason */
    @ApiModelProperty(value = "Failure reason")
    private String failReasons;
}
