package com.memariyan.optimizer.service.flow.impl.task;

import com.memariyan.optimizer.domain.TaskData;
import com.memariyan.optimizer.domain.TaskStepAttempt;
import com.memariyan.optimizer.domain.repository.OptimizationTaskRepository;
import com.memariyan.optimizer.metric.OptimizationTaskTimeMetricHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

@Slf4j
public abstract class AbstractTask implements Runnable {

    private final TaskData task;

    private final OptimizationTaskRepository repository;

    private final OptimizationTaskTimeMetricHandler metricHandler;

    protected AbstractTask(TaskData task, OptimizationTaskRepository repository, OptimizationTaskTimeMetricHandler metricHandler) {
        this.task = task;
        this.repository = repository;
        this.metricHandler = metricHandler;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        try {
            task.setStep(getStep());
            task.setStepStatus(TaskData.Status.PENDING);
            repository.updateTaskStep(task);

            doTask();

            task.setStepStatus(TaskData.Status.SUCCESS);
            task.getAttempts().add(new TaskStepAttempt(getStep()));

        } catch (Exception e) {
            log.error("error occurred while doing task step : {}", getStep(), e);
            task.getAttempts().add(new TaskStepAttempt(getStep(), e));
            task.setStepStatus(TaskData.Status.FAILED);

        } finally {
            repository.save(task);
            sendMetrics(startTime);
        }
    }

    private void sendMetrics(long startTime) {
        if (CollectionUtils.isEmpty(task.getAttempts())) {
            return;
        }
        metricHandler.sendMetric(startTime, task.getAttempts().getLast());
    }


    protected abstract void doTask();

    public abstract TaskData.Step getStep();

}
