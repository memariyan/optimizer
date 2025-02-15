package com.memariyan.optimizer.service.optimization.google;

import com.google.ortools.Loader;
import com.google.ortools.constraintsolver.*;
import com.google.protobuf.Duration;
import com.memariyan.optimizer.config.optimization.OptimizationProperties;
import com.memariyan.optimizer.domain.Vehicle;
import com.memariyan.optimizer.service.optimization.VrpSolver;
import com.memariyan.optimizer.service.optimization.VehicleCountCalculator;
import com.memariyan.optimizer.service.optimization.google.constraint.Constraint;
import com.memariyan.optimizer.service.optimization.google.cost.TransitionCost;
import com.memariyan.optimizer.service.optimization.google.mapper.GoogleOrOptimizerMapper;
import com.memariyan.optimizer.service.optimization.model.VehicleRoutingProblem;
import com.memariyan.optimizer.service.optimization.model.VehicleRoutingSolution;
import com.memariyan.optimizer.utils.SparseList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GoogleVrpSolver implements VrpSolver {

    static {
        Loader.loadNativeLibraries();
    }

    private final List<TransitionCost> transitionCosts;

    private final List<Constraint> constraints;

    private final GoogleOrOptimizerMapper mapper;

    private final OptimizationProperties properties;

    private final VehicleCountCalculator calculator = (category, totalVolume, totalWeight) ->
            (int) (Math.max((totalVolume / category.getVolumeCapacity()), (totalWeight / category.getWeightCapacity()))) * 2;

    @Override
    public Optional<VehicleRoutingSolution> solve(VehicleRoutingProblem model) {
        SparseList<Vehicle> vehicles = mapper.getVehicles(model, calculator);
        RoutingIndexManager manager = new RoutingIndexManager(model.getDistances().length, vehicles.size(), 0);
        RoutingModel routing = new RoutingModel(manager);
        transitionCosts.forEach(cost -> cost.apply(model.getDistances(), model.isReturnToOrigin(), vehicles, routing, manager));
        constraints.forEach(constraint -> constraint.apply(model, vehicles, routing, manager));

        ObjectiveMonitoringCallback objectiveCallback = new ObjectiveMonitoringCallback(routing, properties);
        routing.addAtSolutionCallback(objectiveCallback);

        RoutingSearchParameters searchParameters =
                main.defaultRoutingSearchParameters()
                        .toBuilder()
                        .setFirstSolutionStrategy(FirstSolutionStrategy.Value.PATH_CHEAPEST_ARC)
                        .setLocalSearchMetaheuristic(LocalSearchMetaheuristic.Value.GUIDED_LOCAL_SEARCH)
                        .setTimeLimit(Duration.newBuilder().setSeconds(properties.getSolver().getDefaultTimeout()))
                        .build();

        Assignment solution = routing.solveWithParameters(searchParameters);
        return Optional.ofNullable(solution).map(s -> mapper.toResult(model, vehicles, routing, manager, s));
    }

    private static class ObjectiveMonitoringCallback implements Runnable {
        private final RoutingModel routing;
        private Long lastObjectiveValue = Long.MAX_VALUE;
        private Instant startTimer = null;
        private final OptimizationProperties properties;

        protected ObjectiveMonitoringCallback(RoutingModel routing, OptimizationProperties properties) {
            this.routing = routing;
            this.properties = properties;
        }

        @Override
        public void run() {
            var currentObjectiveValue = routing.costVar().value();

            if (currentObjectiveValue < lastObjectiveValue) {
                restartTimer();
                lastObjectiveValue = currentObjectiveValue;
                return;
            }

            if (improvementDeadlineExpired()) {
                routing.solver().finishCurrentSearch();
            }
        }

        private boolean improvementDeadlineExpired() {
            return Instant.now().toEpochMilli() - startTimer.toEpochMilli() > properties.getSolver().getImprovementDeadline();
        }

        private void restartTimer() {
            startTimer = Instant.now();
        }
    }
}