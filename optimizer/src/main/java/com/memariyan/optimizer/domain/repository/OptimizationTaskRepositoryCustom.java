package com.memariyan.optimizer.domain.repository;

import com.memariyan.optimizer.domain.TaskData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OptimizationTaskRepositoryCustom {

    //TODO: add gauge metric
    Page<String> getReadyToOptimizeTaskIDs(Pageable pageable);

    void updateTaskStep(TaskData task);

}
