package com.memariyan.optimizer.client.osrm.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BaseOsrmResponse {

    private String code;

    private String message;

}
