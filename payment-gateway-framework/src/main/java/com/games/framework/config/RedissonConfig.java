package com.games.framework.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.core.env.Environment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RedissonConfig {
    @Bean
    public RedissonClient redissonClient(Environment environment) {
        Config config = new Config();
        SingleServerConfig single = config.useSingleServer()
                                          .setAddress("redis://" + environment.getProperty("spring.data.redis.host", "localhost") + ":" + environment.getProperty("spring.data.redis.port", "6379"))
                                          .setPassword(environment.getProperty("spring.data.redis.password"))
                                          .setDatabase(environment.getProperty("spring.data.redis.database", Integer.class, 0));

        Duration timeout = environment.getProperty("spring.data.redis.timeout", Duration.class, Duration.ZERO);
        if (!timeout.isZero() && !timeout.isNegative()) {
            int timeoutMillis = (int) timeout.toMillis();
            single.setTimeout(timeoutMillis).setConnectTimeout(timeoutMillis);
        }

        if (environment.containsProperty("spring.data.redis.lettuce.pool.max-active")) {
            int maxActive = environment.getProperty("spring.data.redis.lettuce.pool.max-active", Integer.class, 8);
            int minIdle = environment.getProperty("spring.data.redis.lettuce.pool.min-idle", Integer.class, 0);
            int poolSize = maxActive > 0 ? maxActive : 8;
            int minIdleSize = Math.max(0, minIdle);
            if (minIdleSize > poolSize) {
                minIdleSize = poolSize;
            }
            single.setConnectionPoolSize(poolSize).setConnectionMinimumIdleSize(minIdleSize);
        }
        return Redisson.create(config);
    }
}
