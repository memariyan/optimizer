package com.memariyan.optimizer.domain;

import com.memariyan.components.mongo.entity.MongoBaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
@Document(collection = "tasks")
@CompoundIndex(name = "creationDate_status", def = "{'creationDate': 1, 'status' : 1}")
public class TaskData extends MongoBaseEntity {

    @Version
    private Long version;

    @Indexed(unique = true)
    private String taskId;

    @Indexed(unique = true)
    private String trackingId;

    private Long creationDate;

    private Step step;

    private Status stepStatus;

    private Long startTime;

    private Long endTime;

    private Boolean returnToOrigin;

    private Integer serviceDuration;

    private List<VehicleCategory> vehicleCategories;

    private List<Terminal> sources;

    private List<Terminal> destinations;

    private Distance[][] distances;

    private List<OptimumRoute> routes;

    private String replyReceiverId;

    private List<TaskStepAttempt> attempts;

    public enum Step {

        DISTANCES_CALCULATION, OPTIMIZATION;

    }

    public enum Status {

        PENDING, SUCCESS, FAILED;

    }

    public List<TaskStepAttempt> getAttempts() {
        if (attempts == null) {
            attempts = new ArrayList<>();
        }
        return attempts;
    }

    public int getTerminalsCount() {
        return CollectionUtils.size(sources) + CollectionUtils.size(destinations);
    }
}
