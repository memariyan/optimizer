package com.memariyan.optimizer.config.optimization;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.VirtualThreadTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@RequiredArgsConstructor
public class TaskExecutorConfig {

    private final OptimizationProperties properties;

    @Bean
    @Qualifier("optimizerExecutor")
    public TaskExecutor optimizerThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize((int) (properties.getProcess().getReadyThreadCountAtMoment()));
        executor.setMaxPoolSize(properties.getProcess().getMaxThreadCountAtMoment());
        executor.setQueueCapacity(0);  // Queue capacity for pending tasks
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.setThreadNamePrefix("OptimizationTaskExecutor");
        executor.initialize();
        return executor;
    }

    @Bean
    @Qualifier("distanceCalculatorExecutor")
    public TaskExecutor distanceCalculatorThreads() {
        return new VirtualThreadTaskExecutor();
    }

}
