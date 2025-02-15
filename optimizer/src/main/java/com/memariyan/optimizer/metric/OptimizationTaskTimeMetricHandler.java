package com.memariyan.optimizer.metric;

import com.memariyan.optimizer.domain.TaskStepAttempt;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class OptimizationTaskTimeMetricHandler {

    private final MeterRegistry meterRegistry;

    private static final String OPTIMIZATION_TASK_TIME_METRIC = "optimization_task_time_metric";

    public void sendMetric(long startTime, TaskStepAttempt attempt) {
        try {
            long durationMillis = attempt.getTimestamp() - startTime;
            String errorStr = StringUtils.isBlank(attempt.getReason()) ? "null" : attempt.getReason();
            Timer.builder(OPTIMIZATION_TASK_TIME_METRIC)
                    .description("Time taken to execute doTask method")
                    .tags("step", attempt.getStep().name(), "status", attempt.getStatus().name(),
                            "error", errorStr)
                    .publishPercentileHistogram()
                    .register(meterRegistry)
                    .record(Duration.ofMillis(durationMillis));
        } catch (Exception e) {
            log.error("exception occurred while sending task metrics : {}", attempt.getStep(), e);
        }
    }

}
