package com.games.nuvei;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "xe")
public class XeConfig {
    private String accountId;
    private String apiKey;
}
