package com.games.wynpay.swap.vo;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class NewSwapVo {
    private String side;
    private BigDecimal amount;
    private String crypto;
    private String currency;
    private String address;
    private String chain;
}
