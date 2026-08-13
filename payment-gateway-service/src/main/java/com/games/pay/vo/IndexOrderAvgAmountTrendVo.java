package com.games.pay.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class IndexOrderAvgAmountTrendVo {
    private BigDecimal avgPrice;
    private BigDecimal trend;
}