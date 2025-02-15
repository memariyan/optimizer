package com.memariyan.optimizer.presentation.msg;

import com.memariyan.components.common.exception.NotFoundException;
import com.memariyan.components.common.exception.ValidationException;
import com.memariyan.optimizer.service.flow.TaskFlowService;
import com.memariyan.optimizer.api.msg.request.TaskProcessRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "com.memariyan.optimizer.kafka.process-listener.enabled", havingValue = "true")
public class ProcessListener {

    private final TaskFlowService service;

    @KafkaListener(topics = "${com.memariyan.optimizer.kafka.process.topic-name}",
            groupId = "${com.memariyan.optimizer.kafka.process.consumer-group-id}",
            properties = {"spring.json.value.default.type=com.memariyan.optimizer.api.msg.request.TaskProcessRequest"})
    public void process(@Payload TaskProcessRequest request,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                        @Header(KafkaHeaders.OFFSET) int offset,
                        Acknowledgment acknowledgment) {
        try {
            log.info("going to optimize task with request: {}, partition: {}, offset: {}", request, partition, offset);
            service.run(request.getTaskId());

        } catch (NotFoundException | ValidationException e) {
            log.error(e.getMessage(), e);

        } finally {
            acknowledgment.acknowledge();
        }
    }
}
