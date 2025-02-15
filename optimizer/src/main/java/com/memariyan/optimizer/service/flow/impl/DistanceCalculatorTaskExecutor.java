package com.memariyan.optimizer.service.flow.impl;

import com.memariyan.optimizer.domain.TaskData;
import com.memariyan.optimizer.domain.repository.OptimizationTaskRepository;
import com.memariyan.optimizer.service.flow.impl.task.DistanceCalculatorTask;
import com.memariyan.optimizer.service.flow.mapper.TaskFlowServiceMapper;
import com.memariyan.optimizer.metric.OptimizationTaskTimeMetricHandler;
import com.memariyan.optimizer.service.routing.RoutingServiceFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class DistanceCalculatorTaskExecutor {

    private final RoutingServiceFactory routingServiceFactory;

    private final TaskFlowServiceMapper mapper;

    private final OptimizationTaskRepository repository;

    private final OptimizationTaskTimeMetricHandler metricHandler;

    private final TaskExecutor taskExecutor;

    public DistanceCalculatorTaskExecutor(
                                          RoutingServiceFactory routingServiceFactory,
                                          TaskFlowServiceMapper mapper,
                                          OptimizationTaskRepository repository,
                                          OptimizationTaskTimeMetricHandler metricHandler,
                                          @Qualifier("distanceCalculatorExecutor") TaskExecutor distanceCalculatorTaskExecutor) {

        this.routingServiceFactory = routingServiceFactory;
        this.mapper = mapper;
        this.repository = repository;
        this.metricHandler = metricHandler;
        this.taskExecutor = distanceCalculatorTaskExecutor;
    }


    public void execute(TaskData task) {
        taskExecutor.execute(new DistanceCalculatorTask(task, repository, routingServiceFactory, mapper, metricHandler));
    }


}
