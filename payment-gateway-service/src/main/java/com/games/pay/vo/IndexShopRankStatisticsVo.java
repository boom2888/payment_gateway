package com.games.pay.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class IndexShopRankStatisticsVo {

    private List<ShopOrderNumVo> transactionCountRanking;
    private List<ShopOrderAmountVo> transactionAmountRanking;
}
