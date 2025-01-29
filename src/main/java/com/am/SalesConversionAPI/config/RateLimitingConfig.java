package com.am.SalesConversionAPI.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.Duration;

/**
 * Configuration class to set up rate limiting.
 * This class is used to set up rate limiting for the API.
 * It is shared across track endpoints.
 * The rate limit is set to 10 requests per minute for demoing purposes.
 * In real live this should be set to a more reasonable value.
 *
 * @TODO Params should be configurable and generally this should be implemented through Redis or similar
 */
@Configuration
public class RateLimitingConfig {

    @Bean
    public Bucket sharedBucket() {
        return Bucket.builder()
                .addLimit(Bandwidth.classic(10, Refill.greedy(10, Duration.ofMinutes(1)))) // 10 requests per minute for demo purposes
                .build();
    }
}