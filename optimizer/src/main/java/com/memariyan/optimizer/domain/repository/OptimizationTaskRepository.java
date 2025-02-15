package com.memariyan.optimizer.domain.repository;

import com.memariyan.optimizer.domain.TaskData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OptimizationTaskRepository extends MongoRepository<TaskData, String>, OptimizationTaskRepositoryCustom {

    Optional<TaskData> findByTaskId(String taskId);

    long countByTrackingId(String trackingId);

}
