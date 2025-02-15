package com.memariyan.optimizer.service.flow.impl.task;

import com.memariyan.optimizer.base.AbstractIT;
import com.memariyan.optimizer.client.publisher.ResultPublisher;
import com.memariyan.optimizer.config.optimization.OptimizationProperties;
import com.memariyan.optimizer.domain.*;
import com.memariyan.optimizer.domain.repository.OptimizationTaskRepository;
import com.memariyan.optimizer.service.flow.fixture.OptimizationTaskFixture;
import com.memariyan.optimizer.service.flow.mapper.TaskFlowServiceMapper;
import com.memariyan.optimizer.metric.OptimizationTaskTimeMetricHandler;
import com.memariyan.optimizer.service.optimization.TspSolverFactory;
import com.memariyan.optimizer.service.optimization.VrpSolver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class OptimizerTaskIT extends AbstractIT {

    @Autowired
    private OptimizationTaskRepository repository;

    @Autowired
    private OptimizationTaskTimeMetricHandler metricHandler;

    @Autowired
    private ResultPublisher resultPublisher;

    @Autowired
    private TaskFlowServiceMapper mapper;

    @Autowired
    private VrpSolver vrpSolver;

    @Autowired
    private TspSolverFactory tspSolverFactory;

    @Autowired
    private OptimizationProperties properties;


    @AfterEach
    public void clean() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("optimize - " +
            "given-> a task with 1 source and 30 destinations + calculated distances, " +
            "when -> optimize task is called, " +
            "then-> optimum routes are returned ")
    public void optimize_0() throws Exception {

        //given ->
        TaskData optimizationTask = OptimizationTaskFixture.createTaskForOptimization();
        repository.save(optimizationTask);

        // when->
        new OptimizerTask(optimizationTask, repository, metricHandler, vrpSolver,
                tspSolverFactory, resultPublisher, properties, mapper).run();

        // then ->
        Optional<TaskData> preparedTaskOp = repository.findByTaskId(optimizationTask.getTaskId());
        Assertions.assertTrue(preparedTaskOp.isPresent());

        TaskData preparedTask = preparedTaskOp.get();
        Assertions.assertEquals(preparedTask.getStep(), TaskData.Step.OPTIMIZATION);
        Assertions.assertEquals(preparedTask.getStepStatus(), TaskData.Status.SUCCESS);
        Assertions.assertNotNull(preparedTask.getRoutes());

        TaskStepAttempt attempt = preparedTask.getAttempts().get(0);
        Assertions.assertEquals(attempt.getStep(), TaskData.Step.OPTIMIZATION);
        Assertions.assertEquals(attempt.getStatus(), TaskData.Status.SUCCESS);
        Assertions.assertNotEquals(attempt.getTimestamp(), 0);

        for (OptimumRoute route : preparedTask.getRoutes()) {
            Vehicle vehicle = route.getVehicle();
            Assertions.assertTrue(route.getTotalPackageVolume() > 0);
            Assertions.assertTrue(route.getTotalPackageVolume() - vehicle.getVolumeCapacity() <= 0);
            Assertions.assertTrue(route.getTotalPackageWeight() > 0);
            Assertions.assertTrue(route.getTotalPackageWeight() - vehicle.getWeightCapacity() <= 0);
            Assertions.assertTrue(route.getTotalDuration() > 0);
            Assertions.assertTrue(route.getTotalDistance() > 0);
            Assertions.assertFalse(route.getTerminals().isEmpty());

            if (optimizationTask.getStartTime() != null && optimizationTask.getEndTime() != null) {
                for (int i = 1; i < route.getTerminals().size(); i++) {
                    Terminal terminal = route.getTerminals().get(i);
                    Assertions.assertTrue(terminal.getEstimatedVisitTime() >= optimizationTask.getStartTime() &&
                            terminal.getEstimatedVisitTime() <= optimizationTask.getEndTime());
                }
            }
        }
    }

}
