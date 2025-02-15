package com.memariyan.optimizer.service.optimization.model;

import com.memariyan.optimizer.domain.Distance;
import com.memariyan.optimizer.domain.Terminal;
import com.memariyan.optimizer.domain.VehicleCategory;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VehicleRoutingProblem {

    private boolean returnToOrigin;

    private int serviceDuration; // in seconds

    private Long startTime; // in epoc

    private Long endTime; // in epoc

    private Terminal[] terminals;

    private Distance[][] distances;

    private List<VehicleCategory> vehicleCategories;

}
