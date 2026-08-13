package com.games.caratpay;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "caratpay")
public class CaratPayConfig {

    private String apiUrl;
    private Long merchantId;
    private String merchantKey;
    private String gatewayPayUrl;


    public String getCreateOrderUrl() {
        return String.format("%s/api/pay/create_order", apiUrl);
    }

    public String getReturnUrl() {
        return String.format("%s/success", gatewayPayUrl);
    }

    public String getNotifyUrl() {
        return String.format("%s/api/qrcode/callBack/carat_pay/order", gatewayPayUrl);
    }
}
