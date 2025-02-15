package com.memariyan.optimizer.service.optimization.google.cost;

import com.google.ortools.constraintsolver.RoutingIndexManager;
import com.google.ortools.constraintsolver.RoutingModel;
import com.memariyan.optimizer.domain.Distance;
import com.memariyan.optimizer.domain.Vehicle;
import com.memariyan.optimizer.utils.SparseList;
import org.springframework.stereotype.Component;

@Component
public class DistanceCost implements TransitionCost {

    @Override
    public void apply(Distance[][] distances,
                      Boolean returnToOrigin, SparseList<Vehicle> vehicles,
                      RoutingModel routing, RoutingIndexManager manager) {

        int transitCallbackIndex = routing.registerTransitCallback((long fromIndex, long toIndex) -> {
            int fromNode = manager.indexToNode(fromIndex);
            int toNode = manager.indexToNode(toIndex);
            if (toNode == 0 && !Boolean.TRUE.equals(returnToOrigin)) {
                return 0;
            }
            return distances[fromNode][toNode].getDistance().intValue();
        });
        routing.setArcCostEvaluatorOfAllVehicles(transitCallbackIndex);
    }

}
