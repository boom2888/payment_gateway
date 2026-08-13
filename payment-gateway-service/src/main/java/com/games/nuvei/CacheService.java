package com.games.nuvei;

import com.games.common.core.redis.RedisCache;
import com.xe.xecdApiClient.model.ConvertFromResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class CacheService {

    @Autowired
    private RedisCache redisCache;
    private static final String RATE_INFO = "RATE_INFO";
    private static final Integer RATE_CACHE_TTL_HOURS = 6;

    public ConvertFromResponse getRateCache(String currency){
        String cacheKey = RATE_INFO  + ":"+ currency;
        ConvertFromResponse accountCache = redisCache.getCacheObject(cacheKey);
        return accountCache;
    }

    public void setRateCache(String currency, ConvertFromResponse response){
        String cacheKey = RATE_INFO  + ":" + currency;
        redisCache.setCacheObject(cacheKey, response, RATE_CACHE_TTL_HOURS, TimeUnit.HOURS);
    }


}
