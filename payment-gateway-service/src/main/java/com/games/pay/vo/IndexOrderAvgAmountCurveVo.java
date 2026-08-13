package com.games.pay.vo;

import lombok.Data;

@Data
public class IndexOrderAvgAmountCurveVo {
    private IndexOrderAvgAmountTrendVo monthly;
    private IndexOrderAvgAmountTrendVo weekly;
    private PriceTrend priceTrend;
}
