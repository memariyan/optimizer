package com.memariyan.optimizer.service.flow;

import com.memariyan.optimizer.service.flow.model.OptimizationTaskModel;

public interface TaskFlowService {

    void register(OptimizationTaskModel model);

    void run(String taskId);

}
