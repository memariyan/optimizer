package com.memariyan.optimizer.service.routing;

import com.memariyan.optimizer.config.optimization.OptimizationProperties;
import com.memariyan.optimizer.domain.enums.RoutingType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoutingServiceFactory {

    private final List<RoutingService> routingServices;

    private final OptimizationProperties properties;

    private Map<RoutingType, RoutingService> serviceMap;

    private RoutingService getService(RoutingType type) {
        if (serviceMap == null) {
            serviceMap = routingServices.stream().collect(Collectors.toMap(
                    RoutingService::getServiceType,
                    routingService -> routingService
            ));
        }
        return serviceMap.get(type);
    }

    public RoutingService strategyService(int count) {
        if (properties.getRouting().getType() != null) {
            return getService(properties.getRouting().getType());
        }
        if (properties.getRouting().getOsrmThreshold() != null) {
            int threshold = properties.getRouting().getOsrmThreshold();
            if (count > threshold) {
                return getService(RoutingType.HAVERSINE);
            }
            return getService(RoutingType.OSRM);
        }
        return getService(RoutingType.HAVERSINE);
    }

}
