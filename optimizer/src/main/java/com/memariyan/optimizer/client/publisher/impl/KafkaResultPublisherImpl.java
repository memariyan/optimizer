package com.memariyan.optimizer.client.publisher.impl;

import com.memariyan.optimizer.client.publisher.ResultPublisher;
import com.memariyan.optimizer.api.msg.response.OptimizationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaResultPublisherImpl implements ResultPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publish(OptimizationResponse result, String receiverId) {
        kafkaTemplate.send(receiverId, result);
    }

}
