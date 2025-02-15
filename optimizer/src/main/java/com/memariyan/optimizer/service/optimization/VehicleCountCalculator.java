package com.memariyan.optimizer.service.optimization;

import com.memariyan.optimizer.domain.VehicleCategory;

@FunctionalInterface
public interface VehicleCountCalculator {

    int getCount(VehicleCategory category, double totalVolume, double totalWeight);

}
