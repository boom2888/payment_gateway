package com.games.api.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@ApiModel("订单退款参数")
public class RefundOrderVo extends ApiBaseReq {

    @ApiModelProperty(value = "本平台单号唯一标识(我们平台单号,后续订单操作传这个值)", example = "8100", required = true)
    @NotNull(message = "{order.info.not.empty}")
    private String orderNo;

    @ApiModelProperty(value = "退款id", example = "8100")
    private String refundSsoId;

    @ApiModelProperty(value = "退款金额", example = "18.66", required = true)
    @NotNull(message = "{refund.amount.not.empty}")
    private BigDecimal refundAmount;

    @ApiModelProperty(value = "退款币种", example = "EUR", required = true)
    @NotNull(message = "{refund.currency.not.empty}")
    private String currency;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    @Override
    public String getSignParams() {
        return orderNo;
    }
}
