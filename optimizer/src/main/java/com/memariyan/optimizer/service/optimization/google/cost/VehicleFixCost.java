package com.memariyan.optimizer.service.optimization.google.cost;

import com.google.ortools.constraintsolver.RoutingIndexManager;
import com.google.ortools.constraintsolver.RoutingModel;
import com.memariyan.optimizer.domain.Distance;
import com.memariyan.optimizer.domain.Vehicle;
import com.memariyan.optimizer.utils.SparseList;
import org.springframework.stereotype.Component;

@Component
public class VehicleFixCost implements TransitionCost {

    @Override
    public void apply(Distance[][] distances,
                      Boolean returnToOrigin, SparseList<Vehicle> vehicles,
                      RoutingModel routing, RoutingIndexManager manager) {

        if (vehicles.isEmpty()) {
            return;
        }
        for (int i = 0; i < vehicles.size(); i++) {
            Vehicle vehicle = vehicles.get(i);
            routing.setFixedCostOfVehicle(vehicle.getCost(), i);
        }
    }
}
