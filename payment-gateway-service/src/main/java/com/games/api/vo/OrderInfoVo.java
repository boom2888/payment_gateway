package com.games.api.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Data
@ApiModel("Order Query Request")
public class OrderInfoVo extends ApiBaseReq {

    @ApiModelProperty(value = "Unique order ID on our platform (use this value for subsequent order operations)", example = "8100", required = true)
    @NotNull(message = "{order.info.not.empty}")
    private String orderNo;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    @Override
    public String getSignParams() {
        return orderNo;
    }
}
