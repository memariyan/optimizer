package com.memariyan.optimizer.domain;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class TaskStepAttempt {

    public TaskStepAttempt(TaskData.Step step) {
        this.step = step;
        this.timestamp = System.currentTimeMillis();
        this.status = TaskData.Status.SUCCESS;
    }

    public TaskStepAttempt(TaskData.Step step, String reason) {
        this.step = step;
        this.timestamp = System.currentTimeMillis();
        this.reason = reason;
        this.status = TaskData.Status.FAILED;
    }

    public TaskStepAttempt(TaskData.Step step, Throwable e) {
        this(step, e.getClass() + " : " + e.getMessage());
    }

    private TaskData.Step step;

    private long timestamp;

    private TaskData.Status status;

    private String reason;

}
