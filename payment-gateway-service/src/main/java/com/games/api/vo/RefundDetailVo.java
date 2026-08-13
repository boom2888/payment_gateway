package com.games.api.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("查询退款订单参数")
public class RefundDetailVo extends ApiBaseReq {

    @ApiModelProperty(value = "本平台单号唯一标识(我们平台单号,后续订单操作传这个值)", example = "8100", required = true)
    @NotNull(message = "{order.info.not.empty}")
    private String orderNo;

    @ApiModelProperty(value = "退款订单id", required = true, example = "8100")
    private String refundSsoId;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    @Override
    public String getSignParams() {
        return orderNo;
    }
}
