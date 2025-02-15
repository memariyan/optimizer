package com.memariyan.optimizer.service.optimization.google.cost;

import com.google.ortools.constraintsolver.RoutingIndexManager;
import com.google.ortools.constraintsolver.RoutingModel;
import com.memariyan.optimizer.domain.Distance;
import com.memariyan.optimizer.domain.Vehicle;
import com.memariyan.optimizer.utils.SparseList;

public interface TransitionCost {

    void apply(Distance[][] distances,
               Boolean returnToOrigin,
               SparseList<Vehicle> vehicles,
               RoutingModel routing,
               RoutingIndexManager manager);

}
