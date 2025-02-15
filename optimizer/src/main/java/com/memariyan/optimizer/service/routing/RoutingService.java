package com.memariyan.optimizer.service.routing;

import com.memariyan.optimizer.domain.enums.RoutingType;
import com.memariyan.optimizer.service.base.model.LocationModel;
import com.memariyan.optimizer.service.routing.exception.RoutingException;
import com.memariyan.optimizer.service.routing.model.RoadDistance;

public interface RoutingService {

    RoadDistance[][] calculateDistances(LocationModel[] locations) throws RoutingException;

    RoutingType getServiceType();
}
