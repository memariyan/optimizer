package com.memariyan.optimizer.client.publisher.impl;

import com.memariyan.optimizer.client.publisher.TaskPublisher;
import com.memariyan.optimizer.config.kafka.KafkaProperties;
import com.memariyan.optimizer.api.msg.request.TaskProcessRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaTaskPublisherImpl implements TaskPublisher {

    private final KafkaProperties properties;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publish(String taskId) {
        kafkaTemplate.send(properties.getProcess().getTopicName(), new TaskProcessRequest().setTaskId(taskId));
    }

}
