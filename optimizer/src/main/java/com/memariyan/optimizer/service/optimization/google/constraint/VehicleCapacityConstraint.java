package com.memariyan.optimizer.service.optimization.google.constraint;

import com.google.ortools.constraintsolver.RoutingIndexManager;
import com.google.ortools.constraintsolver.RoutingModel;
import com.memariyan.optimizer.domain.Vehicle;
import com.memariyan.optimizer.service.optimization.model.VehicleRoutingProblem;
import com.memariyan.optimizer.utils.SparseList;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Component
public class VehicleCapacityConstraint implements Constraint {

    @Override
    public void apply(VehicleRoutingProblem model, SparseList<Vehicle> vehicles, RoutingModel routing, RoutingIndexManager manager) {
        if (vehicles.isEmpty()) {
            return;
        }

        int weightCallbackIndex = routing.registerUnaryTransitCallback((long fromIndex) -> {
            int fromNode = manager.indexToNode(fromIndex);
            return fromNode == 0 ? 0 : (int) (model.getTerminals()[fromNode].getPackageWeight());
        });
        long[] weightCapacities = getCapacities(vehicles, Vehicle::getWeightCapacity);
        routing.addDimensionWithVehicleCapacity(weightCallbackIndex, 0, weightCapacities, true, "WeightLoad");

        int volumeCallbackIndex = routing.registerUnaryTransitCallback((long fromIndex) -> {
            int fromNode = manager.indexToNode(fromIndex);
            return fromNode == 0 ? 0 : (int) (model.getTerminals()[fromNode].getPackageVolume());
        });
        long[] volumeCapacities = getCapacities(vehicles, Vehicle::getVolumeCapacity);
        routing.addDimensionWithVehicleCapacity(volumeCallbackIndex, 0, volumeCapacities, true, "VolumeLoad");
    }

    private long[] getCapacities(SparseList<Vehicle> vehicles, Function<Vehicle, Double> capacityFactor) {
        List<Long> capacities = new ArrayList<>();
        for (int i = 0; i < vehicles.size(); i++) {
            Vehicle vehicle = vehicles.get(i);
            Double value = capacityFactor.apply(vehicle);
            capacities.add(value != null ? value.longValue() : Long.MAX_VALUE);
        }
        return capacities.stream().mapToLong(v -> v).toArray();
    }
}
