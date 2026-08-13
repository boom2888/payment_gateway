package com.games.pay.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class AmountTrend {
    private List<String> dates;
    private List<BigDecimal> completed;
    private List<BigDecimal> cancelled;
    private List<BigDecimal> processing;
    private List<BigDecimal> failed;
    private List<BigDecimal> refunded;
    private List<BigDecimal> chargeback;
    private List<BigDecimal> chargebackCancelled;
    private List<BigDecimal> dispute;
}
