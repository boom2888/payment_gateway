package com.games.pay.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class RatesTrend {
    private List<String> dates;
    private List<BigDecimal> refundRate;
    private List<BigDecimal> chargebackRate;
    private List<BigDecimal> cancelRate;
    private List<BigDecimal> successRate;
}
