package com.games.nuvei.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class QuoteResult {

    private BigDecimal liquidityQuote;
    private String liquidityProvider;
    private String error;

    public QuoteResult(BigDecimal liquidityQuote, String error) {
        this.liquidityQuote = liquidityQuote;
        this.liquidityProvider = "b2c2";
        this.error = error;
    }

    public QuoteResult(BigDecimal liquidityQuote, String liquidityProvider, String error) {
        this.liquidityQuote = liquidityQuote;
        this.liquidityProvider = liquidityProvider;
        this.error = error;
    }

}
