package com.games.payment.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ApiToNftOrderVo {

    private String merchantId;

    private BigDecimal amount;

    private String address;

    private String ssoOrderId;
}
