package com.games.api.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

@ApiModel("Order Basic Info")
@Data
public class OrderInfoDto {

    @ApiModelProperty("Cryptocurrency amount")
    private BigDecimal cryptoAmount;

    @ApiModelProperty("Fiat amount (2 decimal places)")
    private BigDecimal fiatAmount;

    @ApiModelProperty("Crypto symbol, e.g. BTC, ETH")
    private String crypto;

    @ApiModelProperty("Receiving wallet address for cryptocurrency")
    private String walletAddress;

    @ApiModelProperty("Fiat currency code, e.g. USD, EUR")
    private String currency;

    @ApiModelProperty("Order status")
    private String status;

    @ApiModelProperty("Order type")
    private String type;

    @ApiModelProperty("Payment channel")
    private String source;

    private String productName;
    private String orderNo;

    /** 失败原因 */
    @ApiModelProperty(value = "Failure reason")
    private String failReasons;

    public void format(){
        if(cryptoAmount != null)cryptoAmount = cryptoAmount.setScale(8, RoundingMode.DOWN).stripTrailingZeros();
        if(fiatAmount != null)fiatAmount = fiatAmount.setScale(2, RoundingMode.DOWN);
    }
}
