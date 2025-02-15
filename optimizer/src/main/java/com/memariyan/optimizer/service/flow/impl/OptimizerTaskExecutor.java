package com.memariyan.optimizer.service.flow.impl;

import com.memariyan.optimizer.client.publisher.ResultPublisher;
import com.memariyan.optimizer.config.optimization.OptimizationProperties;
import com.memariyan.optimizer.domain.TaskData;
import com.memariyan.optimizer.domain.repository.OptimizationTaskRepository;
import com.memariyan.optimizer.service.flow.impl.task.OptimizerTask;
import com.memariyan.optimizer.service.flow.mapper.TaskFlowServiceMapper;
import com.memariyan.optimizer.metric.OptimizationTaskTimeMetricHandler;
import com.memariyan.optimizer.service.optimization.TspSolverFactory;
import com.memariyan.optimizer.service.optimization.VrpSolver;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class OptimizerTaskExecutor {

    private final OptimizationTaskRepository repository;

    private final OptimizationTaskTimeMetricHandler metricHandler;

    private final ResultPublisher resultPublisher;

    private final TaskFlowServiceMapper mapper;

    private final TaskExecutor taskExecutor;

    private final VrpSolver vrpSolver;

    private final TspSolverFactory tspSolverFactory;

    private final OptimizationProperties properties;


    public OptimizerTaskExecutor(OptimizationTaskRepository repository,
                                 OptimizationTaskTimeMetricHandler metricHandler,
                                 ResultPublisher resultPublisher,
                                 TaskFlowServiceMapper mapper,
                                 VrpSolver vrpSolver, TspSolverFactory tspSolverFactory,
                                 OptimizationProperties properties,
                                 @Qualifier("optimizerExecutor") TaskExecutor optimizerTaskExecutor) {

        this.repository = repository;
        this.metricHandler = metricHandler;
        this.resultPublisher = resultPublisher;
        this.mapper = mapper;
        this.taskExecutor = optimizerTaskExecutor;
        this.vrpSolver = vrpSolver;
        this.tspSolverFactory = tspSolverFactory;
        this.properties = properties;
    }

    public void execute(TaskData task) {
        taskExecutor.execute(new OptimizerTask(task, repository, metricHandler, vrpSolver,
                tspSolverFactory, resultPublisher, properties, mapper));
    }

}
