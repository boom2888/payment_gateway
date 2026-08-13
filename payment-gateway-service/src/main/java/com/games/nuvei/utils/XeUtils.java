package com.games.nuvei.utils;

import cn.hutool.core.util.StrUtil;
import com.games.nuvei.CacheService;
import com.games.nuvei.XeConfig;
import com.github.benmanes.caffeine.cache.Cache;
import com.xe.xecdApiClient.config.XecdApiConfigBean;
import com.xe.xecdApiClient.exception.XecdApiException;
import com.xe.xecdApiClient.model.ConvertFromResponse;
import com.xe.xecdApiClient.model.HistoricRateResponse;
import com.xe.xecdApiClient.service.XecdApiService;
import com.xe.xecdApiClient.service.XecdApiServiceFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class XeUtils {

    public static ConvertFromResponse getCurrencyRate(String fromCurrency, String toCurrency, CacheService cacheService,
                                                      XeConfig xeConfig, Cache<String, ConvertFromResponse> cache) {
        String currency = fromCurrency + "_" + toCurrency;
        ConvertFromResponse response = cacheService.getRateCache(currency);
        if(response == null){
            String accountId = xeConfig.getAccountId();
            String apiKey = xeConfig.getApiKey();

            if (StrUtil.isBlank(accountId) || StrUtil.isBlank(apiKey)) {
                System.err.println("法币与法币之间的转换，XE账户ID或API密钥未设置");
                return null;
            }
            XecdApiConfigBean config = new XecdApiConfigBean();
            config.setAccountId(accountId);
            config.setApiKey(apiKey);
            XecdApiService service = XecdApiServiceFactory.createXecdAPIService(config);
            try {
                response = service.convertFrom(fromCurrency, toCurrency, 1.0, null);
                cacheService.setRateCache(currency, response);
                cache.put(currency, response);
            } catch (XecdApiException e) {
                throw new RuntimeException(e);
            }
        }
        return response;
    }



    public void change(){
        XecdApiConfigBean config = new XecdApiConfigBean();
        config.setAccountId("<YOUR_ACCOUNT_ID>");
        config.setApiKey("<YOUR_API_KEY>");

        XecdApiService apiService = XecdApiServiceFactory.createXecdAPIService(config);

        try {
            HistoricRateResponse response = apiService.historicRate(
                    "USD",
                    "EUR,GBP",
                    "2025-07-29",
                    null,
                    100.00,
                    false,
                    false
            );
            System.out.println(response);
        } catch (XecdApiException e) {
            log.error("查询 XE 历史汇率失败", e);
        }
    }
}
