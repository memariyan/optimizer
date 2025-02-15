package com.memariyan.optimizer.service.flow.impl;

import com.memariyan.components.common.exception.DuplicateException;
import com.memariyan.components.common.exception.NotFoundException;
import com.memariyan.components.common.exception.ValidationException;
import com.memariyan.optimizer.domain.TaskData;
import com.memariyan.optimizer.domain.repository.OptimizationTaskRepository;
import com.memariyan.optimizer.service.flow.TaskFlowService;
import com.memariyan.optimizer.service.flow.mapper.TaskFlowServiceMapper;
import com.memariyan.optimizer.service.flow.model.OptimizationTaskModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskFlowServiceImpl implements TaskFlowService {

    private final DistanceCalculatorTaskExecutor distanceCalculatorTaskExecutor;

    private final OptimizerTaskExecutor optimizerTaskExecutor;

    private final OptimizationTaskRepository repository;

    private final TaskFlowServiceMapper mapper;

    @Override
    public void register(OptimizationTaskModel model) {

        if (repository.countByTrackingId(model.trackingId()) > 0) {
            throw new DuplicateException("there is a task with same tracking id : " + model.trackingId());
        }
        TaskData task = mapper.toTask(model);
        task.setTaskId(UUID.randomUUID().toString());
        repository.save(task);
        distanceCalculatorTaskExecutor.execute(task);
    }

    @Override
    public void run(String taskId) {
        TaskData task = repository.findByTaskId(taskId)
                .orElseThrow(() -> new NotFoundException("No optimization task found for taskId: " + taskId));

        if (task.getStep() != TaskData.Step.DISTANCES_CALCULATION && task.getStepStatus() != TaskData.Status.SUCCESS) {
            throw new ValidationException("Task is not valid for optimization - " +
                    "step : " + task.getStep() + " , stepStatus: " + task.getStepStatus() + " , taskId: " + taskId);
        }
        optimizerTaskExecutor.execute(task);
    }

}
