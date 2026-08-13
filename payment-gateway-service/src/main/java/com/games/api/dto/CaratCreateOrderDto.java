package com.games.api.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
//@ApiModel("创建订单 DTO")
public class CaratCreateOrderDto extends BaseDto{

    @ApiModelProperty(value = "法币", example = "108", required = true)
    private String currency;

    @ApiModelProperty(value = "法币金额", example = "9.1", required = true)
    private BigDecimal fiatAmount;

    @ApiModelProperty(value = "本平台单号唯一标识(我们平台单号,后续订单操作传这个值)", example = "8100", required = true)
    private String orderNo;

    @ApiModelProperty(value = "商户订单号(您平台的单号)", example = "TEST-0c8671", required = true)
    private String ssoOrderId;

    /**
     * 订单状态：
     * 0-创建，1-AML 开始，2-AML 暂停，3-AML 拒绝，
     * 4-待入金，5-处理中，6-入金失败，7-取消，8-已执行，9-完成
     */
    @ApiModelProperty(value = "订单状态", example = "4", required = true)
    private String status;

    @ApiModelProperty(value = "xxxx", example = "https://xd.audcom98.com/pay/pay.html?token=7df45e3bbbb84da38a4647c8e686faa7")
    private String payUrl;
}
