package com.memariyan.optimizer.config.kafka;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "com.memariyan.optimizer.kafka")
public class KafkaProperties {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    private TopicConfig register;

    private TopicConfig process;

    @Getter
    @Setter
    public static class TopicConfig {

        private Boolean listenerEnabled;

        private String consumerGroupId;

        private String topicName;

        private Integer partitionCount;
    }

}
