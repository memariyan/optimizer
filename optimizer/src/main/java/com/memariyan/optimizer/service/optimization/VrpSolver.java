package com.memariyan.optimizer.service.optimization;

import com.memariyan.optimizer.service.optimization.model.VehicleRoutingProblem;
import com.memariyan.optimizer.service.optimization.model.VehicleRoutingSolution;

import java.util.Optional;

public interface VrpSolver {

    Optional<VehicleRoutingSolution> solve(VehicleRoutingProblem problem);

}
