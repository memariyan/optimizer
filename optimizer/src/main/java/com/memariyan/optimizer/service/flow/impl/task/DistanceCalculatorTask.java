package com.memariyan.optimizer.service.flow.impl.task;

import com.memariyan.optimizer.domain.TaskData;
import com.memariyan.optimizer.domain.repository.OptimizationTaskRepository;
import com.memariyan.optimizer.service.flow.mapper.TaskFlowServiceMapper;
import com.memariyan.optimizer.metric.OptimizationTaskTimeMetricHandler;
import com.memariyan.optimizer.service.routing.RoutingServiceFactory;
import com.memariyan.optimizer.service.routing.model.RoadDistance;

public class DistanceCalculatorTask extends AbstractTask {

    private final TaskData task;

    private final RoutingServiceFactory routingServiceFactory;

    private final TaskFlowServiceMapper mapper;

    public DistanceCalculatorTask(TaskData task,
                                  OptimizationTaskRepository repository,
                                  RoutingServiceFactory routingServiceFactory,
                                  TaskFlowServiceMapper mapper,
                                  OptimizationTaskTimeMetricHandler metricHandler) {
        super(task, repository, metricHandler);
        this.task = task;
        this.routingServiceFactory = routingServiceFactory;
        this.mapper = mapper;
    }

    @Override
    public void doTask() {
        RoadDistance[][] distances = routingServiceFactory.strategyService(task.getTerminalsCount())
                .calculateDistances(mapper.toLocationModels(task));
        task.setDistances(mapper.toDistanceModels(distances));
    }

    @Override
    public TaskData.Step getStep() {
        return TaskData.Step.DISTANCES_CALCULATION;
    }

}
