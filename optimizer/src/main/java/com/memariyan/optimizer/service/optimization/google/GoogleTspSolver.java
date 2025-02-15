package com.memariyan.optimizer.service.optimization.google;

import com.google.ortools.constraintsolver.*;
import com.memariyan.optimizer.domain.Distance;
import com.memariyan.optimizer.service.optimization.TspSolver;
import com.memariyan.optimizer.service.optimization.google.cost.DistanceCost;
import com.memariyan.optimizer.service.optimization.google.mapper.GoogleOrOptimizerMapper;
import com.memariyan.optimizer.service.optimization.model.ShortestPathProblem;
import com.memariyan.optimizer.service.optimization.model.ShortestPathSolution;
import com.memariyan.optimizer.utils.SparseList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GoogleTspSolver implements TspSolver {

    private final DistanceCost distanceCost;

    private final GoogleOrOptimizerMapper mapper;

    @Override
    public ShortestPathSolution solve(ShortestPathProblem model) {
        Distance[][] distances = getDistanceMatrix(model);
        RoutingIndexManager manager = new RoutingIndexManager(model.getTerminals().length, 1, 0);
        RoutingModel routing = new RoutingModel(manager);
        distanceCost.apply(distances, model.isReturnToOrigin(), new SparseList<>(List.of()), routing, manager);

        RoutingSearchParameters searchParameters =
                main.defaultRoutingSearchParameters()
                        .toBuilder()
                        .setFirstSolutionStrategy(FirstSolutionStrategy.Value.PATH_CHEAPEST_ARC)
                        .build();

        Assignment solution = routing.solveWithParameters(searchParameters);
        return mapper.toResult(model, routing, manager, solution);
    }

    private Distance[][] getDistanceMatrix(ShortestPathProblem model) {
        if (model.getDistances().length < model.getTerminals().length) {
            throw new IllegalArgumentException(" distance matrix size is invalid! ");
        }
        return mapper.toDistanceMatrix(model);
    }

}
