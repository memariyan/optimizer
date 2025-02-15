package com.memariyan.optimizer.api.msg.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class TaskProcessRequest {

    private String taskId;

}
