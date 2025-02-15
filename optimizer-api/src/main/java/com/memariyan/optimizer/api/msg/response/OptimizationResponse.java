package com.memariyan.optimizer.api.msg.response;

import com.memariyan.optimizer.api.msg.dto.OptimumRoute;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class OptimizationResponse {

    private String trackingId;

    private Status status;

    private List<String> errorMessages;

    private List<OptimumRoute> routes;

    public enum Status {
        SUCCESS, FAILED;
    }

}
