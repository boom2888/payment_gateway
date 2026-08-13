package com.games.api.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MerchantBalanceDto {

    /**
     * 商户余额
     */
    private BigDecimal totalBalance;

    /**
     *  可提取余额
     */
    private BigDecimal withdrawableBalance;


    /**
     * 冻结余额
     */
    private BigDecimal frozenBalance;

    /**
     * 循环保证金
     */
    private BigDecimal marginBalance;


    private String currency;
}
