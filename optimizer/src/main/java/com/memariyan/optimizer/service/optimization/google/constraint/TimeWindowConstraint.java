package com.memariyan.optimizer.service.optimization.google.constraint;

import com.google.ortools.constraintsolver.RoutingDimension;
import com.google.ortools.constraintsolver.RoutingIndexManager;
import com.google.ortools.constraintsolver.RoutingModel;
import com.memariyan.optimizer.domain.Terminal;
import com.memariyan.optimizer.domain.Vehicle;
import com.memariyan.optimizer.service.optimization.model.VehicleRoutingProblem;
import com.memariyan.optimizer.utils.SparseList;
import org.springframework.stereotype.Component;

@Component
public class TimeWindowConstraint implements Constraint {

    @Override
    public void apply(VehicleRoutingProblem model, SparseList<Vehicle> vehicles, RoutingModel routing, RoutingIndexManager manager) {
        if (model.getStartTime() == null || model.getEndTime() == null) {
            return;
        }

        int timeCallbackIndex = routing.registerTransitCallback((long fromIndex, long toIndex) -> {
            int fromNode = manager.indexToNode(fromIndex);
            int toNode = manager.indexToNode(toIndex);
            if (fromNode == 0 || toNode == 0) {
                return 0;
            }
            return model.getDistances()[fromNode][toNode].getDuration().intValue() + model.getServiceDuration();
        });
        routing.addDimension(timeCallbackIndex, Long.MAX_VALUE, Long.MAX_VALUE, false, "Time");

        RoutingDimension timeDimension = routing.getMutableDimension("Time");
        Terminal[] terminals = model.getTerminals();
        for (int i = 1; i < terminals.length; i++) {
            long index = manager.nodeToIndex(i);
            timeDimension.cumulVar(index).setRange(0, (model.getEndTime() - model.getStartTime()) / 1000L);
        }
    }

}
