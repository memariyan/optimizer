package com.memariyan.optimizer.base.config;

import org.junit.ClassRule;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.test.context.DynamicPropertyRegistry;

@TestConfiguration
public abstract class EmbeddedKafkaConfiguration {

    @ClassRule
    public static EmbeddedKafkaRule embeddedKafka = new EmbeddedKafkaRule(1, true, 0);

    public static void dynamicProperties(DynamicPropertyRegistry registry) {
    }
}
