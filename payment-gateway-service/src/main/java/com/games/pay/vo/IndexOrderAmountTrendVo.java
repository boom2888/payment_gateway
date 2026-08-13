package com.games.pay.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class IndexOrderAmountTrendVo {
    private BigDecimal orderAmount;
    private BigDecimal trend;
}
