package com.memariyan.optimizer.domain.repository.impl;

import com.mongodb.client.result.UpdateResult;
import com.memariyan.components.metric.annotation.Metric;
import com.memariyan.optimizer.domain.TaskData;
import com.memariyan.optimizer.domain.repository.OptimizationTaskRepositoryCustom;
import com.memariyan.optimizer.metric.OptimizationTasksMetricHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OptimizationTaskRepositoryCustomImpl implements OptimizationTaskRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    @Metric(handlers = {OptimizationTasksMetricHandler.class})
    public Page<String> getReadyToOptimizeTaskIDs(Pageable pageable) {
        Criteria dateCriteria = Criteria.where("step").is(TaskData.Step.DISTANCES_CALCULATION)
                .and("stepStatus").is(TaskData.Status.SUCCESS);
        Query query = new Query(dateCriteria).with(Sort.by(Sort.Direction.ASC, "creationDate")).with(pageable);
        query.fields().include("taskId");

        List<TaskData> result = mongoTemplate.find(query, TaskData.class);
        long count = mongoTemplate.count(query, TaskData.class);
        return new PageImpl<>(result.stream().map(TaskData::getTaskId).collect(Collectors.toList()), pageable, count);
    }

    @Override
    public void updateTaskStep(TaskData task) {
        Query query = new Query();
        query.addCriteria(Criteria.where("taskId").is(task.getTaskId()));
        Update update = new Update();
        update.set("step", task.getStep());
        update.set("stepStatus", task.getStepStatus());
        update.set("version", task.getVersion() + 1);
        UpdateResult result = mongoTemplate.updateFirst(query, update, TaskData.class);
        boolean isSuccess = result.getModifiedCount() > 0;
        if (isSuccess) {
            task.setVersion(task.getVersion() + 1);
        }
    }
}
