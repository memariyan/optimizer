package com.memariyan.optimizer.config.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaTopicConfig {

    private final KafkaProperties properties;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServers());
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic processTopic() {
        return new NewTopic(properties.getProcess().getTopicName(), properties.getProcess().getPartitionCount(), (short) 1);
    }

    @Bean
    public NewTopic registerTopic() {
        return new NewTopic(properties.getRegister().getTopicName(), properties.getRegister().getPartitionCount(), (short) 1);
    }

}
