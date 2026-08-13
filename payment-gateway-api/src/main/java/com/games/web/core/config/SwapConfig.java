package com.games.web.core.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class SwapConfig {
    @Value("${swap.shopId:1}")
    private Long swapShopId;

    @Value("${swap.swapPayUrl:http://localhost:8800/api/create}")
    private String swapPayUrl;


}
