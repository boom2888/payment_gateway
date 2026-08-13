package com.games.pay.dto;

import com.games.common.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ReconReportMerchantDto {
    @Excel(name = "Merchant id", sort = 16)
    private String merchantId;

    @Excel(name = "Order id", sort = 17)
    private String orderId;

    @Excel(name = "Currency id", sort = 18)
    private String currencyId;

    @Excel(name = "Order amount", sort = 19)
    private BigDecimal orderAmount;

    @Excel(name = "Order amount USD", sort = 20)
    private BigDecimal transferAmount;

    @Excel(name = "crypto id", sort = 21)
    private String cryptoId;

    @Excel(name = "crypto amount", sort = 22)
    private BigDecimal cryptoAmount;

    @Excel(name = "Refund amount", sort = 23)
    private BigDecimal refundAmount;

    @Excel(name = "RDR amount", sort = 24)
    private BigDecimal rdrAmount;

    @Excel(name = "CB amount", sort = 25)
    private BigDecimal cbAmount;

    @Excel(name = "CB Cancelled amount", sort = 26)
    private BigDecimal cbCancelledAmount;

    @Excel(name = "Refund fee", sort = 27)
    private BigDecimal refundFee;

    @Excel(name = "RDR fee", sort = 28)
    private BigDecimal rdrFee;

    @Excel(name = "Chargeback fee", sort = 29)
    private BigDecimal chargebackFee;

    @Excel(name = "processing fee", sort = 30)
    private BigDecimal processingFee;

    @Excel(name = "Rolling Reserve", sort = 31)
    private String rollingReserve;

    @Excel(name = "gateway fee USD", sort = 32)
    private BigDecimal gatewayFeeUsd;

    @Excel(name = "fee type", sort = 33)
    private String feeType;

    @Excel(name = "order status", sort = 34)
    private String orderStatus;

    @Excel(name = "created at", dateFormat = "yyyy-MM-dd HH:mm:ss", sort = 35)
    private Date createdAt;

    @Excel(name = "sso order id", sort = 36)
    private String ssoOrderId;

    @Excel(name = "Fx Rate", sort = 37)
    private BigDecimal fxRate;

    @Excel(name = "settlement", sort = 38)
    private BigDecimal settlement;

    @Excel(name = "EUR/USD", sort = 39)
    private BigDecimal eurUsd;

    @Excel(name = "Issuing Country", sort = 40)
    private String issuingCountry;

    @Excel(name = "Region", sort = 41)
    private String region;

}
