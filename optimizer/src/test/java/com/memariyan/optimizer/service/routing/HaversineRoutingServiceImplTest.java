package com.memariyan.optimizer.service.routing;

import com.memariyan.optimizer.config.optimization.OptimizationProperties;
import com.memariyan.optimizer.service.base.model.LocationModel;
import com.memariyan.optimizer.service.routing.impl.HaversineRoutingServiceImpl;
import com.memariyan.optimizer.service.routing.model.RoadDistance;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@Import(HaversineRoutingServiceImpl.class)
public class HaversineRoutingServiceImplTest {

    @Autowired
    private HaversineRoutingServiceImpl haversineService;

    @MockBean
    private OptimizationProperties properties;

    @Test
    @DisplayName("calculateDistances - " +
            "given-> two location, " +
            "when -> calculateDistances is called , " +
            "then -> successfully calculate")
    public void haversineCalculateDistance() {
        Mockito.when(properties.getRouting()).thenReturn(getRoutingConfig());
        LocationModel[] locations = {
                new LocationModel(35.80688257136062, 51.42883503579884),
                new LocationModel(35.71151608155559, 51.34766682746636)
        };
        RoadDistance[][] result = haversineService.calculateDistances(locations);
        Assertions.assertEquals(Double.valueOf(12887.653199920689), result[0][1].distance());
        Assertions.assertEquals(Double.valueOf(12887.653199920689), result[1][0].distance());
    }

    private OptimizationProperties.RoutingConfig getRoutingConfig() {
        OptimizationProperties.RoutingConfig config = new OptimizationProperties.RoutingConfig();
        config.setMeterDurationInSecond(0.03);
        return config;
    }

}
