package com.games.nuvei;

import cn.hutool.core.util.StrUtil;
import com.games.nuvei.utils.XeUtils;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.xe.xecdApiClient.model.ConvertFromResponse;
import com.xe.xecdApiClient.model.Rate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;

@Slf4j
@Service
public class RateService {
    private static final long LOCAL_CACHE_MAX_SIZE = 1024L;

    @Autowired
    CacheService cacheService;
    @Autowired
    XeConfig xeConfig;

    private final Cache<String, ConvertFromResponse> localRateCache = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofHours(24))
            .maximumSize(LOCAL_CACHE_MAX_SIZE)
            .build();

    public BigDecimal getRate(String currency, String baseCurrency) {
        if (StrUtil.isBlank(currency) || StrUtil.isBlank(baseCurrency)) {
            return BigDecimal.ONE;
        }
        if (StrUtil.equalsIgnoreCase(currency, baseCurrency)) {
            return BigDecimal.ONE;
        }
        String cacheKey = buildCacheKey(currency, baseCurrency);
        try {
            ConvertFromResponse response = XeUtils.getCurrencyRate(currency, baseCurrency, cacheService, xeConfig, localRateCache);
            BigDecimal rate = extractRate(response, baseCurrency);
            if (rate != null) {
                return rate;
            }
        } catch (Exception e) {
            log.warn("Failed to fetch XE rate for {}/{}", currency, baseCurrency, e);
            ConvertFromResponse cachedRes = localRateCache.getIfPresent(cacheKey);
            BigDecimal cachedRate = extractRate(cachedRes, baseCurrency);
            if (cachedRate != null) {
                return cachedRate;
            }
        }
        return BigDecimal.ONE;
    }

    private BigDecimal extractRate(ConvertFromResponse response, String baseCurrency) {
        if (response == null || response.getTo() == null) {
            return null;
        }
        for (Rate rate : response.getTo()) {
            if (StrUtil.equalsIgnoreCase(rate.getQuotecurrency(), baseCurrency)) {
                Double mid = rate.getMid();
                if (mid != null) {
                    return BigDecimal.valueOf(mid);
                }
            }
        }
        return null;
    }

    private String buildCacheKey(String currency, String baseCurrency) {
        return currency + "_" + baseCurrency;
    }
}
