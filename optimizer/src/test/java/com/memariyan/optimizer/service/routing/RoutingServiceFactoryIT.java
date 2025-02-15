package com.memariyan.optimizer.service.routing;

import com.memariyan.optimizer.base.AbstractIT;
import com.memariyan.optimizer.domain.enums.RoutingType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RoutingServiceFactoryIT extends AbstractIT {

    @Autowired
    private RoutingServiceFactory routingServiceFactory;

    @Test
    @DisplayName("routing strategy service - " +
            "given-> count = threshold, " +
            "when -> strategyService is called , " +
            "then -> use osrm strategy")
    public void strategyService_0() {
        RoutingService service = routingServiceFactory.strategyService(20);
        Assertions.assertNotNull(service);
        Assertions.assertNotNull(service.getServiceType());
        Assertions.assertEquals(service.getServiceType(), RoutingType.OSRM);
    }

    @Test
    @DisplayName("routing strategy service - " +
            "given-> count < threshold, " +
            "when -> strategyService is called , " +
            "then -> use osrm strategy")
    public void strategyService_1() {
        RoutingService service = routingServiceFactory.strategyService(10);
        Assertions.assertNotNull(service);
        Assertions.assertNotNull(service.getServiceType());
        Assertions.assertEquals(service.getServiceType(), RoutingType.OSRM);
    }

    @Test
    @DisplayName("routing strategy service - " +
            "given-> count > threshold, " +
            "when -> strategyService is called , " +
            "then -> use haversine strategy")
    public void strategyService_2() {
        RoutingService service = routingServiceFactory.strategyService(30);
        Assertions.assertNotNull(service);
        Assertions.assertNotNull(service.getServiceType());
        Assertions.assertEquals(service.getServiceType(), RoutingType.HAVERSINE);
    }

}
