package com.memariyan.optimizer.service.optimization.google.mapper;

import com.google.ortools.constraintsolver.Assignment;
import com.google.ortools.constraintsolver.RoutingIndexManager;
import com.google.ortools.constraintsolver.RoutingModel;
import com.memariyan.optimizer.domain.Distance;
import com.memariyan.optimizer.domain.Terminal;
import com.memariyan.optimizer.domain.VehicleCategory;
import com.memariyan.optimizer.service.optimization.model.*;
import com.memariyan.optimizer.service.optimization.VehicleCountCalculator;
import com.memariyan.optimizer.domain.Vehicle;
import com.memariyan.optimizer.utils.SparseList;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GoogleOrOptimizerMapper {

    public SparseList<Vehicle> getVehicles(VehicleRoutingProblem model, VehicleCountCalculator countCalculator) {
        if (CollectionUtils.isEmpty(model.getVehicleCategories())) {
            return new SparseList<>(List.of());
        }

        double totalVolume = 0;
        double totalWeight = 0;

        for (Terminal terminal : model.getTerminals()) {
            totalVolume += terminal.getPackageVolume();
            totalWeight += terminal.getPackageWeight();
        }

        int index = 0;
        List<Vehicle> vehicles = new ArrayList<>();
        for (VehicleCategory category : model.getVehicleCategories()) {
            int vehicleCount = (category.getCount() == null || category.getCount() == 0) ?
                    countCalculator.getCount(category, totalVolume, totalWeight) : category.getCount();
            Vehicle data = new Vehicle();
            data.setCategoryId(category.getCategoryId());
            data.setCost(category.getCost());
            data.setWeightCapacity(category.getWeightCapacity());
            data.setVolumeCapacity(category.getVolumeCapacity());
            data.setFromIndex(index);
            data.setToIndex(index + vehicleCount - 1);
            vehicles.add(data);
            index += category.getCount();
        }
        return new SparseList<>(vehicles);
    }

    public VehicleRoutingSolution toResult(VehicleRoutingProblem model, SparseList<Vehicle> vehicles, RoutingModel routing,
                                           RoutingIndexManager manager, Assignment solution) {

        VehicleRoutingSolution result = new VehicleRoutingSolution();
        for (int i = 0; i < vehicles.size(); i++) {
            VehicleOptimumRoute route = new VehicleOptimumRoute(vehicles.get(i));
            long index = routing.start(i);
            while (!routing.isEnd(index)) {
                long nextIndex = solution.value(routing.nextVar(index));
                int currentNodeIndex = manager.indexToNode(index);
                Terminal terminal = model.getTerminals()[currentNodeIndex];
                route.addTerminal(terminal);
                index = nextIndex;
            }
            if (CollectionUtils.isNotEmpty(route.getTerminals()) && route.getTerminals().size() > 1) {
                result.addRoute(route);
            }
        }
        return result;
    }

    public ShortestPathSolution toResult(ShortestPathProblem model, RoutingModel routing,
                                         RoutingIndexManager manager, Assignment solution) {

        ShortestPathSolution result = new ShortestPathSolution();

        long index = routing.start(0);
        while (!routing.isEnd(index)) {
            long nextIndex = solution.value(routing.nextVar(index));
            int currentNodeIndex = manager.indexToNode(index);
            Terminal terminal = model.getTerminals()[currentNodeIndex];
            result.addTerminal(terminal);
            index = nextIndex;
        }
        return result;
    }

    public Distance[][] toDistanceMatrix(ShortestPathProblem model) {
        int size = model.getTerminals().length;
        if (model.getDistances().length == size) {
            return model.getDistances();
        }

        Terminal[] terminals = model.getTerminals();
        Distance[][] distances = model.getDistances();
        Distance[][] result = new Distance[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result[i][j] = distances[terminals[i].getLocation().getIndex()][terminals[j].getLocation().getIndex()];
            }
        }
        return result;
    }
}
