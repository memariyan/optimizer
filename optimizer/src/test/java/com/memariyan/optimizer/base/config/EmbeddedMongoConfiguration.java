package com.memariyan.optimizer.base.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;

@TestConfiguration
public class EmbeddedMongoConfiguration {

    public static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("de.flapdoodle.mongodb.embedded.version", () -> "4.0.2");
        registry.add("spring.data.mongodb.host", () -> "127.0.0.1");
        registry.add("spring.data.mongodb.port", () -> "27017");
    }
}
