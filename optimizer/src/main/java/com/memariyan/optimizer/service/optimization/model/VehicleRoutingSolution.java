package com.memariyan.optimizer.service.optimization.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class VehicleRoutingSolution {

    private List<VehicleOptimumRoute> routes;

    public void addRoute(VehicleOptimumRoute route) {
        if (routes == null) {
            routes = new ArrayList<>();
        }
        routes.add(route);
    }

}
