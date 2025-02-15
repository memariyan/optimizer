package com.memariyan.optimizer.service.routing.impl;

import com.memariyan.optimizer.client.osrm.OsrmClient;
import com.memariyan.optimizer.client.osrm.model.response.DistanceMatrixResponse;
import com.memariyan.optimizer.domain.enums.RoutingType;
import com.memariyan.optimizer.service.base.model.LocationModel;
import com.memariyan.optimizer.service.routing.RoutingService;
import com.memariyan.optimizer.service.routing.exception.RoutingException;
import com.memariyan.optimizer.service.routing.mapper.OsrmRoutingServiceMapper;
import com.memariyan.optimizer.service.routing.model.RoadDistance;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("OsrmRoutingServiceImpl")
@RequiredArgsConstructor
public class OsrmRoutingServiceImpl implements RoutingService {

    private final OsrmClient client;

    private final OsrmRoutingServiceMapper mapper;

    @Override
    public RoadDistance[][] calculateDistances(LocationModel[] locations) throws RoutingException {
        try {
            if (locations == null || locations.length == 0) {
                throw  new IllegalArgumentException("there is no locations to calculate distances");
            }
            String query = mapper.toLocationsQuery(locations);
            DistanceMatrixResponse response = client.getDistanceMatrix(query, "distance,duration");
            return mapper.toRoadDistanceArray(response);

        } catch (Exception e) {
            log.error("location distance matrix calculation failed", e);
            throw new RoutingException(e.getMessage());
        }
    }

    @Override
    public RoutingType getServiceType() {
        return RoutingType.OSRM;
    }

}
