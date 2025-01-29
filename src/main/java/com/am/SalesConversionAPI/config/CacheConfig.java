package com.am.SalesConversionAPI.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.TimeUnit;

/**
 * Cache configuration for caching the response of the API.
 * The cache will be stored in memory and will be evicted after 1 minute.
 * Used for metrics endpoint to save some trees from being cut down.
 *
 * @TODO Params should be configurable.
 */
@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager metricsCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("metricsCache"); // Cache name
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.MINUTES) // Cache  response for 1 minute
                .maximumSize(100));  // Max 100 items in cache
        return cacheManager;
    }
}