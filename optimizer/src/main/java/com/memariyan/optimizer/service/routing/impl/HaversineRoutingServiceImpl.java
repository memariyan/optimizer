package com.memariyan.optimizer.service.routing.impl;

import com.jillesvangurp.geo.GeoGeometry;
import com.memariyan.optimizer.config.optimization.OptimizationProperties;
import com.memariyan.optimizer.domain.enums.RoutingType;
import com.memariyan.optimizer.service.base.model.LocationModel;
import com.memariyan.optimizer.service.routing.RoutingService;
import com.memariyan.optimizer.service.routing.exception.RoutingException;
import com.memariyan.optimizer.service.routing.model.RoadDistance;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("HaversineRoutingServiceImpl")
@RequiredArgsConstructor
public class HaversineRoutingServiceImpl implements RoutingService {

    private final OptimizationProperties properties;

    @Override
    public RoadDistance[][] calculateDistances(LocationModel[] locations) throws RoutingException {
        try {
            if (locations == null || locations.length == 0) {
                throw new IllegalArgumentException("there is no locations to calculate distances");
            }
            RoadDistance[][] distances = new RoadDistance[locations.length][locations.length];
            for (int i = 0; i < locations.length; i++) {
                for (int j = 0; j < locations.length; j++) {
                    distances[i][j] = getHaversineRoadDistance(locations[i], locations[j]);
                }
            }
            return distances;
        } catch (Exception e) {
            log.error("location distance matrix calculation failed", e);
            throw new RoutingException(e.getMessage());
        }
    }

    @Override
    public RoutingType getServiceType() {
        return RoutingType.HAVERSINE;
    }

    private RoadDistance getHaversineRoadDistance(LocationModel from, LocationModel to) {
        double distanceInMeter = getHaversineDistanceInMeter(from, to);
        double durationInSec = distanceInMeter * properties.getRouting().getMeterDurationInSecond();
        return new RoadDistance(distanceInMeter, durationInSec);
    }

    private double getHaversineDistanceInMeter(LocationModel from, LocationModel to) {
        return GeoGeometry.distance(
                from.latitude(), from.longitude(),
                to.latitude(), to.longitude());
    }

}
