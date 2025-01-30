package com.example.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RateLimiterService {
    private final StringRedisTemplate redisTemplate;
    private static final String RATE_LIMIT_KEY = "global_rate_limit";
    private static final int LIMIT = 5;  // Allow only 5 requests
    private static final int WINDOW_SECONDS = 60; // Time window (60s)

    public boolean isRateLimited() {
        // Increment counter
        Long requestCount = redisTemplate.opsForValue().increment(RATE_LIMIT_KEY);

        // Set TTL if new key
        if (requestCount != null && requestCount == 1) {
            redisTemplate.expire(RATE_LIMIT_KEY, Duration.ofSeconds(WINDOW_SECONDS));
        }

        return requestCount != null && requestCount > LIMIT;
    }
}
