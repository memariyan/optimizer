package com.memariyan.optimizer.service.flow.impl.task;

import com.memariyan.optimizer.base.AbstractIT;
import com.memariyan.optimizer.domain.Distance;
import com.memariyan.optimizer.domain.TaskData;
import com.memariyan.optimizer.domain.TaskStepAttempt;
import com.memariyan.optimizer.domain.repository.OptimizationTaskRepository;
import com.memariyan.optimizer.service.flow.fixture.OptimizationTaskFixture;
import com.memariyan.optimizer.service.flow.mapper.TaskFlowServiceMapper;
import com.memariyan.optimizer.metric.OptimizationTaskTimeMetricHandler;
import com.memariyan.optimizer.service.routing.RoutingServiceFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class DistanceCalculatorTaskIT extends AbstractIT {

    @Autowired
    private OptimizationTaskRepository repository;

    @Autowired
    private RoutingServiceFactory routingServiceFactory;

    @Autowired
    private TaskFlowServiceMapper mapper;

    @Autowired
    private OptimizationTaskTimeMetricHandler metricHandler;

    @AfterEach
    public void clean() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("calculateDistances - " +
            "given-> a task with 1 source and 4 destinations = 5 points, " +
            "when -> distance calculator task is called, " +
            "then-> a distance matrix 5*5 is returned and data is updated ")
    public void calculateDistances_0() {

        //given ->
        TaskData optimizationTask = OptimizationTaskFixture.createTaskForDistanceCalculation();
        repository.save(optimizationTask);
        int locationCount = optimizationTask.getSources().size() + optimizationTask.getDestinations().size();

        // when->
        new DistanceCalculatorTask(optimizationTask, repository, routingServiceFactory, mapper, metricHandler).run();

        // then ->
        Optional<TaskData> preparedTaskOp = repository.findByTaskId(optimizationTask.getTaskId());
        Assertions.assertTrue(preparedTaskOp.isPresent());

        TaskData preparedTask = preparedTaskOp.get();
        Assertions.assertEquals(preparedTask.getStep(), TaskData.Step.DISTANCES_CALCULATION);
        Assertions.assertEquals(preparedTask.getStepStatus(), TaskData.Status.SUCCESS);
        Assertions.assertNotNull(preparedTask.getDistances());
        Assertions.assertEquals(preparedTask.getDistances().length, locationCount);
        Assertions.assertEquals(preparedTask.getAttempts().size(), 1);

        TaskStepAttempt attempt = preparedTask.getAttempts().get(0);
        Assertions.assertEquals(attempt.getStep(), TaskData.Step.DISTANCES_CALCULATION);
        Assertions.assertEquals(attempt.getStatus(), TaskData.Status.SUCCESS);
        Assertions.assertNotEquals(attempt.getTimestamp(), 0);

        for (Distance[] distanceRow : preparedTask.getDistances()) {
            Assertions.assertEquals(distanceRow.length, locationCount);
            for (Distance distance : distanceRow) {
                Assertions.assertNotNull(distance.getDistance());
                Assertions.assertNotNull(distance.getDuration());
            }
        }
    }

    @Test
    @DisplayName("calculateDistances - " +
            "given-> a task with some 0 source and 0 destinations = 0 points, " +
            "when -> distance calculator task is called, " +
            "then-> task result is failed ")
    public void calculateDistances_1() {

        //given ->
        TaskData optimizationTask = OptimizationTaskFixture.createTaskForDistanceCalculation();
        optimizationTask.setSources(List.of());
        optimizationTask.setDestinations(List.of());
        repository.save(optimizationTask);

        // when->
        new DistanceCalculatorTask(optimizationTask, repository, routingServiceFactory, mapper, metricHandler).run();

        // then ->
        Optional<TaskData> preparedTaskOp = repository.findByTaskId(optimizationTask.getTaskId());
        Assertions.assertTrue(preparedTaskOp.isPresent());

        TaskData updatedTask = preparedTaskOp.get();
        Assertions.assertEquals(updatedTask.getStep(), TaskData.Step.DISTANCES_CALCULATION);
        Assertions.assertEquals(updatedTask.getStepStatus(), TaskData.Status.FAILED);
        Assertions.assertNull(updatedTask.getDistances());
        Assertions.assertEquals(updatedTask.getAttempts().size(), 1);

        TaskStepAttempt attempt = updatedTask.getAttempts().get(0);
        Assertions.assertEquals(attempt.getStep(), TaskData.Step.DISTANCES_CALCULATION);
        Assertions.assertEquals(attempt.getStatus(), TaskData.Status.FAILED);
        Assertions.assertNotEquals(attempt.getTimestamp(), 0);
        Assertions.assertTrue(attempt.getReason().contains("there is no locations to calculate distances"));
    }
}
