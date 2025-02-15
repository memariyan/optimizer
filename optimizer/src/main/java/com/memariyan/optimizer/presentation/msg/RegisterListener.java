package com.memariyan.optimizer.presentation.msg;

import com.memariyan.components.common.exception.ValidationException;
import com.memariyan.optimizer.client.publisher.ResultPublisher;
import com.memariyan.optimizer.presentation.msg.exception.InvalidRequestException;
import com.memariyan.optimizer.presentation.msg.mapper.RegisterListenerMapper;
import com.memariyan.optimizer.service.flow.TaskFlowService;
import com.memariyan.optimizer.api.msg.request.OptimizationRequest;
import com.memariyan.optimizer.api.msg.response.OptimizationResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "com.memariyan.optimizer.kafka.register-listener.enabled", havingValue = "true")
public class RegisterListener {

    private final Validator validator;

    private final ResultPublisher resultPublisher;

    private final RegisterListenerMapper mapper;

    private final TaskFlowService service;

    @KafkaListener(topics = "${com.memariyan.optimizer.kafka.register.topic-name}",
            groupId = "${com.memariyan.optimizer.kafka.register.consumer-group-id}",
            properties = {"spring.json.value.default.type=com.memariyan.optimizer.api.msg.request.OptimizationRequest"})
    public void register(@Payload OptimizationRequest request,
                         @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                         @Header(KafkaHeaders.OFFSET) int offset,
                         @Header(OptimizationRequest.REPLY_TOPIC_HEADER_NAME) String replyTopic,
                         Acknowledgment acknowledgment) {

        if (StringUtils.isBlank(replyTopic)) {
            log.warn("received an optimization request with empty reply topic value. going to ignore it!, request: {}", request);
            acknowledgment.acknowledge();
        }

        try {
            validate(request);
            log.info("going to register optimization request with " +
                    "trackingId: {}, partition :{}, offset:{}", request.getTrackingId(), partition, offset);

            service.register(mapper.toModel(request, replyTopic));

        } catch (InvalidRequestException ex) {
            log.error("optimization request is invalid: {}", request, ex);
            sendFailedResult(request.getTrackingId(), ex.getErrorMessages(), replyTopic);

        } catch (Exception ex) {
            log.error("can not register optimization request: {}", request, ex);
            sendFailedResult(request.getTrackingId(), List.of(ex.getMessage()), replyTopic);

        } finally {
            acknowledgment.acknowledge();
        }
    }

    //TODO: validate total package and volume capacity are consistent with total terminal capacities
    private void validate(OptimizationRequest request) throws ValidationException {
        Set<ConstraintViolation<OptimizationRequest>> errors = validator.validate(request);
        if (!errors.isEmpty()) {
            throw new InvalidRequestException("request validation failed",
                    errors.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList()));
        }
    }

    private void sendFailedResult(String trackingId, List<String> errorMessages, String target) {
        resultPublisher.publish(new OptimizationResponse()
                .setTrackingId(trackingId)
                .setStatus(OptimizationResponse.Status.FAILED)
                .setErrorMessages(errorMessages), target);
    }

}
