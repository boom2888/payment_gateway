package com.games.nuvei.vo;

import lombok.Data;

@Data
public class OpenOrderVo {
    private String sessionToken;
    private String merchantSiteId;
    private String merchantId;
    private String amount;
    private String currency;
}
