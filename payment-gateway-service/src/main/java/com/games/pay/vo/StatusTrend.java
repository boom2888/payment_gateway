package com.games.pay.vo;

import lombok.Data;

import java.util.List;

@Data
public class StatusTrend {
    private List<String> dates;
    private List<Integer> completed;
    private List<Integer> processing;
    private List<Integer> failed;
    private List<Integer> cancelled;
    private List<Integer> refunded;
    private List<Integer> chargeback;
    private List<Integer> chargebackCancelled;
    private List<Integer> dispute;
}
