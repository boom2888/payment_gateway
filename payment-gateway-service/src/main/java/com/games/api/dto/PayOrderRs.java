package com.games.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@ApiModel("3DS Authentication")
@AllArgsConstructor
public class PayOrderRs extends BaseDto{
    @ApiModelProperty(value = "Whether 3DS modal is required", example = "true", required = true)
    private boolean need3ds;
    private TdsDto auth3ds;
    private PayOrderDto order;
}
