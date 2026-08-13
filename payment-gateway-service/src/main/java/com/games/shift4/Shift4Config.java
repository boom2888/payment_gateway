package com.games.shift4;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Shift4 Payment Gateway 配置类
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "shift4")
public class Shift4Config {

    private String url;
    private String merchantId;
    private String signatureKey;
    /**
     * 3DS Method (DFP) 通知回调URL
     * 用于接收设备指纹收集完成的通知
     */
    private String notificationUrl;

    public String getTdsMethodNotificationUrl() {
        return notificationUrl + "/api/callBack/3ds/dfp";
    }
}
