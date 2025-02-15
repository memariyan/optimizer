package com.memariyan.optimizer.service.routing.mapper;

import com.memariyan.optimizer.client.osrm.model.response.DistanceMatrixResponse;
import com.memariyan.optimizer.service.base.model.LocationModel;
import com.memariyan.optimizer.service.routing.model.RoadDistance;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OsrmRoutingServiceMapper {

    default String toLocationsQuery(LocationModel[] locations) {
        StringBuilder queryBuilder = new StringBuilder();
        for (int i = 0; i < locations.length; i++) {
            LocationModel location = locations[i];
            queryBuilder.append(location.longitude()).append(",").append(location.latitude());
            if (i < locations.length - 1) {
                queryBuilder.append(";");
            }
        }
        return queryBuilder.toString();
    }

    default RoadDistance[][] toRoadDistanceArray(DistanceMatrixResponse response) {
        List<List<Double>> durationMatrix = response.getDurations();
        List<List<Double>> distanceMatrix = response.getDistances();
        RoadDistance[][] result = new RoadDistance[distanceMatrix.size()][];

        for (int i = 0; i < distanceMatrix.size(); i++) {
            int rowDistanceCount = durationMatrix.get(i).size();
            result[i] = new RoadDistance[rowDistanceCount];
            for (int j = 0; j < rowDistanceCount; j++) {
                result[i][j] = new RoadDistance(distanceMatrix.get(i).get(j), durationMatrix.get(i).get(j));
            }
        }
        return result;
    }
}
