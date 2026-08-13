package com.games.pay.vo;

import lombok.Data;

import java.util.List;

@Data
public class IndexShopRankStatisticsRequest {

    private String transactionCountStartDate;
    private String transactionCountEndDate;

    private String transactionAmountStartDate;
    private String transactionAmountEndDate;

    private Long shopId;
}
