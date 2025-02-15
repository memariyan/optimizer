package com.memariyan.optimizer.base.config;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import redis.embedded.RedisServer;

import java.io.IOException;

@TestConfiguration
public class EmbeddedRedisConfiguration {

    private static final String HOST = "localhost";
    private static final int PORT = 6379;

    private static RedisServer redisServer;

    @PostConstruct
    public void postConstruct() throws IOException {
        if (redisServer == null) {
            redisServer = RedisServer.newRedisServer().build();
            redisServer.start();
        }
    }

    public static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", () -> HOST);
        registry.add("spring.data.redis.port", () -> PORT);
        registry.add("spring.data.redis.timeout", () -> 30000);
    }

}
