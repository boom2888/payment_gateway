package com.games.pay.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class PriceTrend {
    private List<String> dates;
    private List<BigDecimal> values;
}