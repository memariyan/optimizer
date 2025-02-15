package com.memariyan.optimizer.base;

import com.memariyan.optimizer.base.config.EmbeddedKafkaConfiguration;
import com.memariyan.optimizer.base.config.EmbeddedMongoConfiguration;
import com.memariyan.optimizer.base.config.EmbeddedRedisConfiguration;
import com.memariyan.components.test.annotation.EnableTestExtensions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@EnableTestExtensions
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {
                EmbeddedKafkaConfiguration.class,
                EmbeddedMongoConfiguration.class,
                EmbeddedRedisConfiguration.class
        })
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
public class EmbeddedIT {

    @DynamicPropertySource
    public static void dynamicProperties(DynamicPropertyRegistry registry) {
        EmbeddedKafkaConfiguration.dynamicProperties(registry);
        EmbeddedMongoConfiguration.dynamicProperties(registry);
        EmbeddedRedisConfiguration.dynamicProperties(registry);
    }

}
