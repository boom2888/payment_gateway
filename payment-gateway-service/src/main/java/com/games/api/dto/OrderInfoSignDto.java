package com.games.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("Order Signature Info")
@Data
public class OrderInfoSignDto extends OrderInfoDto {
    @ApiModelProperty(value = "Merchant order ID (order ID on your platform)", example = "TEST-0c8671", required = true)
    private String ssoOrderId;

    @ApiModelProperty(value = "Signature. Generate by hashing ssoOrderId with merchant private key (SHA-256)", required = true, example = "TEST")
    private String sign;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    public String getSignParams() {
        return ssoOrderId;
    }
}
