package com.memariyan.optimizer.service.flow;

import com.memariyan.components.metric.annotation.Metric;
import com.memariyan.components.metric.handler.GlobalTaskMetricHandler;
import com.memariyan.optimizer.client.publisher.TaskPublisher;
import com.memariyan.optimizer.config.optimization.OptimizationProperties;
import com.memariyan.optimizer.domain.repository.OptimizationTaskRepository;
import com.memariyan.components.scheduling.annotation.GlobalTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OptimizationTaskEmitter {

    private final OptimizationProperties properties;

    private final OptimizationTaskRepository repository;

    private final TaskPublisher taskPublisher;

    @GlobalTask
    @Metric(handlers = {GlobalTaskMetricHandler.class})
    @Scheduled(fixedDelayString = "${com.memariyan.optimizer.supply.delay}")
    public void sendOptimizationTasks() {
        if (!Boolean.TRUE.equals(properties.getSupply().getEnabled())) {
            log.debug("supply task scheduler is disabled ...");
            return;
        }
        log.debug("going to send optimization tasks for processing ...");

        Page<String> tasks = repository.getReadyToOptimizeTaskIDs(Pageable.ofSize(properties.getSupply().getCount()));
        tasks.forEach(taskPublisher::publish);
        log.info("{} optimization tasks sent to process. remained count :{}",
                tasks.getContent().size(), tasks.getTotalElements() - tasks.getContent().size());
    }

    //TODO: write an scheduler for failed distance calculation tasks

}
