package com.games.pay.dto;

import lombok.Data;
import org.apache.poi.hpsf.Decimal;

import java.math.BigDecimal;

@Data
public class DailySummaryDto {

    private String businessName;
    private String movementImportId;
    private Integer payoutId;
    private BigDecimal fxRate;

    private String day;

    private BigDecimal settlement;
    private BigDecimal orderAmount;

    private BigDecimal orderAmountUsd;
    private BigDecimal orderAmountEur;
    private BigDecimal rollingReserveUsd;
    private BigDecimal rollingReserveEur;
    private BigDecimal rollingReserve;
    private BigDecimal chargebackRefunds;
    private BigDecimal nuvieChargebackRefunds;
    private BigDecimal fees;
    private BigDecimal rollingReservePayout;
    private BigDecimal settlementInBank;
    private BigDecimal nuvieFees;
    private BigDecimal fxOnOrderAmounts;
    private BigDecimal gain;
    private BigDecimal b2c2FxRate;
}
