package com.memariyan.optimizer.service.flow.mapper;

import com.memariyan.optimizer.api.msg.response.OptimizationResponse;
import com.memariyan.optimizer.domain.*;
import com.memariyan.optimizer.service.base.model.LocationModel;
import com.memariyan.optimizer.service.flow.model.OptimizationTaskModel;
import com.memariyan.optimizer.service.optimization.model.ShortestPathProblem;
import com.memariyan.optimizer.service.optimization.model.VehicleRoutingProblem;
import com.memariyan.optimizer.service.optimization.model.VehicleRoutingSolution;
import com.memariyan.optimizer.service.routing.model.RoadDistance;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mapper(componentModel = "spring")
public interface TaskFlowServiceMapper {

    TaskData toTask(OptimizationTaskModel model);

    @AfterMapping
    default void setLocationIndexes(@MappingTarget TaskData result, OptimizationTaskModel model) {
        int index = 0;
        for (Terminal terminal : result.getSources()) {
            terminal.getLocation().setIndex(index++);
        }
        for (Terminal terminal : result.getDestinations()) {
            terminal.getLocation().setIndex(index++);
        }
    }

    default LocationModel[] toLocationModels(TaskData task) {
        return Stream.concat(task.getSources().stream(), task.getDestinations().stream())
                .map(l -> toLocationModels(l.getLocation())).toArray(LocationModel[]::new);
    }

    LocationModel toLocationModels(Location location);

    Distance[][] toDistanceModels(RoadDistance[][] model);

    Distance[] toDistanceModel(RoadDistance[] model);

    default VehicleRoutingProblem toVehicleRoutingProblem(TaskData task) {

        int counter = 0;
        Terminal[] terminals = new Terminal[task.getSources().size() + task.getDestinations().size()];
        for (int i = 0; i < task.getSources().size(); i++) {
            terminals[counter++] = task.getSources().get(i);
        }
        for (int j = 0; j < task.getDestinations().size(); j++) {
            terminals[counter++] = task.getDestinations().get(j);
        }
        VehicleRoutingProblem problem = new VehicleRoutingProblem();
        problem.setDistances(task.getDistances());
        problem.setServiceDuration(task.getServiceDuration() == null ? 0 : task.getServiceDuration());
        problem.setVehicleCategories(task.getVehicleCategories());
        problem.setTerminals(terminals);
        problem.setStartTime(task.getStartTime());
        problem.setEndTime(task.getEndTime());
        problem.setReturnToOrigin(Boolean.TRUE.equals(task.getReturnToOrigin()));
        return problem;
    }

    default List<ShortestPathProblem> toShortestPathProblems(VehicleRoutingSolution solution,
                                                             Distance[][] distances, boolean returnToOrigin) {

        return solution.getRoutes().stream().map(r -> {
            ShortestPathProblem problem = new ShortestPathProblem();
            problem.setTerminals(r.getTerminals().toArray(new Terminal[0]));
            problem.setDistances(distances);
            problem.setReturnToOrigin(returnToOrigin);
            return problem;
        }).collect(Collectors.toList());
    }

    @Mapping(target = "status", constant = "SUCCESS")
    OptimizationResponse toSuccessOptimizationResponse(List<OptimumRoute> routes, String trackingId);

    @Mapping(target = "status", constant = "FAILED")
    @Mapping(target = "errorMessages", source = "reason", qualifiedByName = "toErrorMessages")
    OptimizationResponse toFailedOptimizationResponse(String trackingId, String reason);

    @Named("toErrorMessages")
    default List<String> toErrorMessages(String reason) {
        return List.of(reason);
    }

    default List<OptimumRoute> toRoutes(VehicleRoutingProblem problem, VehicleRoutingSolution solution) {
        return solution.getRoutes().stream().map(route -> {
            OptimumRoute optimumRoute = new OptimumRoute();
            optimumRoute.setVehicle(route.getVehicle());

            for (int j = 0; j < route.getTerminals().size(); j++) {
                Terminal currentTerminal = route.getTerminals().get(j);

                optimumRoute.addTerminal(currentTerminal);
                setVisitedTime(currentTerminal, problem, optimumRoute);

                if (j < route.getTerminals().size() - 1) {
                    Terminal nextTerminal = route.getTerminals().get(j + 1);
                    addTransitionData(currentTerminal, nextTerminal, problem, optimumRoute);
                }
            }
            return optimumRoute;
        }).collect(Collectors.toList());
    }

    default void setVisitedTime(Terminal currentTerminal, VehicleRoutingProblem model, OptimumRoute route) {
        if (model.getStartTime() == null) {
            return;
        }
        long startTime = model.getStartTime();
        int currentNodeIndex = currentTerminal.getLocation().getIndex();

        if (currentNodeIndex == 0) {
            return;
        }
        if (currentNodeIndex == 1) {
            currentTerminal.setEstimatedVisitTime(startTime);
            return;
        }
        currentTerminal.setEstimatedVisitTime(startTime + (route.getTotalDuration() * 1000L));
    }

    default void addTransitionData(Terminal currentTerminal, Terminal nextTerminal,
                                   VehicleRoutingProblem model, OptimumRoute route) {
        int currentNodeIndex = currentTerminal.getLocation().getIndex();
        int nextNodeIndex = nextTerminal.getLocation().getIndex();

        Distance distance = model.getDistances()[currentNodeIndex][nextNodeIndex];
        route.addDistance(distance.getDistance().intValue());
        if (currentNodeIndex != 0) {
            route.addPackageWeight(currentTerminal.getPackageWeight());
            route.addPackageVolume(currentTerminal.getPackageVolume());
            route.addDuration(distance.getDuration().intValue() + model.getServiceDuration());
        } else {
            route.addDuration(distance.getDuration().intValue());
        }
    }

}
