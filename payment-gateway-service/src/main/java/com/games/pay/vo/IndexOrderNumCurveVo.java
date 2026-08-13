package com.games.pay.vo;

import lombok.Data;

@Data
public class IndexOrderNumCurveVo {
        private IndexOrderNumTrendVo monthly;
        private IndexOrderNumTrendVo weekly;
        private StatusTrend statusTrend;

}

