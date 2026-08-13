package com.games.pay.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TotalFiatAmountVo {

    private BigDecimal totalAmount;

    private String currencyCode;
}
