package com.memariyan.optimizer.metric;

import com.memariyan.components.metric.handler.MonitoringMetricHandler;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OptimizationTasksMetricHandler extends MonitoringMetricHandler {

    private static final String OPTIMIZATION_TASK_METRIC_NAME = "optimization_remaining_tasks_metric";

    public OptimizationTasksMetricHandler(MeterRegistry meterRegistry) {
        super(meterRegistry);
    }

    @Override
    public void handle(String methodName, Object[] inputs, Object output, Long startTime, Long endTime) {

        Page<String> page = (Page<String>) output;
        if (page == null || !page.hasContent()) {
            return;
        }
        log.debug("metric(Gauge)/REMAINING-TASKS , {}", page.getTotalElements());
        Gauge.builder(OPTIMIZATION_TASK_METRIC_NAME, page::getTotalElements)
                .tag("status", "success")
                .register(this.meterRegistry);
    }

    @Override
    public void handleError(String methodName, Object[] input, Throwable error, Long startTime, Long endTime) {
    }

}
