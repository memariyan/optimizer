package com.memariyan.optimizer.service.optimization.google.constraint;

import com.google.ortools.constraintsolver.RoutingIndexManager;
import com.google.ortools.constraintsolver.RoutingModel;
import com.memariyan.optimizer.domain.Vehicle;
import com.memariyan.optimizer.service.optimization.model.VehicleRoutingProblem;
import com.memariyan.optimizer.utils.SparseList;

public interface Constraint {

    void apply(VehicleRoutingProblem model, SparseList<Vehicle> vehicles, RoutingModel routing, RoutingIndexManager manager);

}
