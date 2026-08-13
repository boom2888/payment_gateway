package com.games.wynpay;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class WynPayConfig {
    @Value("${wynpay.apiUrl}")
    private String apiUrl;
    @Value("${wynpay.merchantId}")
    private String merchantId;
    @Value("${wynpay.merchantKey}")
    private String merchantKey;

    @Value("${wynpay.cacheEnable}")
    private Boolean cacheEnabled;
}
