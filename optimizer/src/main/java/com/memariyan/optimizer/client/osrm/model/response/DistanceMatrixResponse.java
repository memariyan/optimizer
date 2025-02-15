package com.memariyan.optimizer.client.osrm.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
public class DistanceMatrixResponse {

    private List<List<Double>> distances;

    private List<List<Double>> durations;

}
