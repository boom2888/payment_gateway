package com.games.payment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "binderr")
public class BinderrConfig {

    private String url;
    private String gatewayUrl;
    private String clientId;
    private String clientSecret;
    private String webhookSecret;

    private Long statusId;
    private Integer managedById;
    private String managedByName;

    public String getCreateEntityUrl() {
        return String.format("%s/v1/entity", url);
    }

    public String getSubscribeUrl() {
        return String.format("%s/v1/rest/webhook/VERIFICATION_STATUS_CHANGED/subscribe", url);
    }

    public String getRemoveSubscribeUrl(String webhookId) {
        return String.format("%s/v1/rest/webhook/VERIFICATION_STATUS_CHANGED/unsubscribe/%s", url, webhookId);
    }

    public String getNotifyUrl() {
        return String.format("%s/api/payment/businessCustomerCorporation/webhook", gatewayUrl);
    }

    public String getTokenUrl() {
        return String.format("%s/public/v1/oauth/access-token", url);
    }

    public String getRefreshUrl() {
        return String.format("%s/public/v1/oauth/refresh-token", url);
    }


    public String getScreeningUrl() {
        return String.format("%s/v1/verification/screening/entity/", url);
    }
}
