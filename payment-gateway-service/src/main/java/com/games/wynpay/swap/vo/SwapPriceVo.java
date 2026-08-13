package com.games.wynpay.swap.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SwapPriceVo {
    @ApiModelProperty(value = "买价")
    private BigDecimal buyPrice;
    @ApiModelProperty(value = "卖价")
    private BigDecimal sellPrice;
}
