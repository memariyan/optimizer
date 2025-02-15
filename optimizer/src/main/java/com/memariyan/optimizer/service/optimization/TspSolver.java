package com.memariyan.optimizer.service.optimization;

import com.memariyan.optimizer.service.optimization.model.ShortestPathProblem;
import com.memariyan.optimizer.service.optimization.model.ShortestPathSolution;

public interface TspSolver {

    ShortestPathSolution solve(ShortestPathProblem route);

}
