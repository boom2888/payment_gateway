package com.games.api.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
//@ApiModel("创建订单 DTO")
public class CreateOrderDto extends BaseDto{

    @ApiModelProperty(value = "加密货币数量", example = "10.0", required = true)
    private BigDecimal cryptoAmount;

    @ApiModelProperty(value = "加密货币", example = "4", required = true)
    private String crypto;

    @ApiModelProperty(value = "法币", example = "108", required = true)
    private String currency;

    @ApiModelProperty(value = "法币金额", example = "9.1", required = true)
    private BigDecimal fiatAmount;

    @ApiModelProperty(value = "撮合价格", example = "1.0", required = true)
    private BigDecimal liquidityQuote;

    @ApiModelProperty(value = "商户ID", example = "1", required = true)
    private Integer merchantId;

//    @ApiModelProperty(value = "系统订单唯一标识 UUID", example = "973278c5-7ea7-4171-b9b1-dbbda26b3dc2", required = true)
//    private String orderUuid;

    @ApiModelProperty(value = "本平台单号唯一标识(我们平台单号,后续订单操作传这个值)", example = "8100", required = true)
    private String orderNo;

    @ApiModelProperty(value = "手续费", example = "0.9", required = true)
    private BigDecimal processingFee;

    @ApiModelProperty(value = "商户订单号(您平台的单号)", example = "TEST-0c8671", required = true)
    private String ssoOrderId;

    /**
     * 订单状态：
     * 0-创建，1-AML 开始，2-AML 暂停，3-AML 拒绝，
     * 4-待入金，5-处理中，6-入金失败，7-取消，8-已执行，9-完成
     */
    @ApiModelProperty(value = "订单状态", example = "4", required = true)
    private String status;

    /**
     * 订单类型：
     * 0: 买入（buy），1: 卖出（sell），2: 其他（例如 NFT）
     */
    @ApiModelProperty(value = "订单类型：0:买入 1:卖出", example = "2", required = true)
    private String type;
}
