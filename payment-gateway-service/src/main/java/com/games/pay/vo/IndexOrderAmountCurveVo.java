package com.games.pay.vo;

import lombok.Data;

@Data
public class IndexOrderAmountCurveVo {
        private IndexOrderAmountTrendVo monthly;
        private IndexOrderAmountTrendVo weekly;
        private AmountTrend amountTrend;
}

