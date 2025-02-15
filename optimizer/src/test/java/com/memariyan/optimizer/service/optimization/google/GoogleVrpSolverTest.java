package com.memariyan.optimizer.service.optimization.google;

import com.memariyan.optimizer.config.optimization.OptimizationProperties;
import com.memariyan.optimizer.domain.Vehicle;
import com.memariyan.optimizer.domain.VehicleCategory;
import com.memariyan.optimizer.service.optimization.fixture.VrpFixture;
import com.memariyan.optimizer.service.optimization.model.VehicleOptimumRoute;
import com.memariyan.optimizer.service.optimization.model.VehicleRoutingProblem;
import com.memariyan.optimizer.service.optimization.model.VehicleRoutingSolution;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@Import({GoogleVrpSolver.class})
@ComponentScan(basePackages = "com.memariyan.optimizer.service.optimization.google")
public class GoogleVrpSolverTest {

    @Autowired
    private GoogleVrpSolver service;

    @MockBean
    private OptimizationProperties properties;

    @BeforeEach
    public void before() {
        var solver = getSolverConfig();
        Mockito.when(properties.getSolver()).thenReturn(solver);
    }

    private OptimizationProperties.SolverConfig getSolverConfig() {
        var solver = new OptimizationProperties.SolverConfig();
        solver.setDefaultTimeout(10);
        solver.setImprovementDeadline(25000);
        return solver;
    }

    @Test
    @DisplayName("solve - " +
            "given-> 2 vehicle category with different capacities and costs, 3 hours time window, " +
            "when -> solve method is called , " +
            "then -> solution is returned and all constraint are passed")
    public void test() throws Exception {

        // given ->
        Long startTime = 1727263800000L;
        Long endTime = 1727267400000L;

        VehicleCategory van = VrpFixture.getVehicleCategory("van", 2, 30, 300, 3000);
        VehicleCategory car = VrpFixture.getVehicleCategory("car", 8, 10, 200, 1500);

        // when ->
        VehicleRoutingProblem vrpModel = VrpFixture.createVrpModel(List.of(van, car), startTime, endTime, true);

        // then ->
        Optional<VehicleRoutingSolution> result = service.solve(vrpModel);
        Assertions.assertTrue(result.isPresent());
        assertResult(result.get(), vrpModel);
    }

    @Test
    @DisplayName("solve - " +
            "given-> 1 vehicle category without any forced count, without time window, " +
            "when -> solve method is called , " +
            "then -> solution is returned and all constraint are passed")
    public void test_2() throws Exception {
        // given ->
        VehicleCategory motorcycle = VrpFixture.getVehicleCategory("motorcycle", 0, 0, 100, 1000);

        // when ->
        VehicleRoutingProblem vrpModel = VrpFixture.createVrpModel(List.of(motorcycle), null, null, false);

        // then ->
        Optional<VehicleRoutingSolution> result = service.solve(vrpModel);
        Assertions.assertTrue(result.isPresent());
        assertResult(result.get(), vrpModel);
    }

    public void assertResult(VehicleRoutingSolution result, VehicleRoutingProblem vrpModel) {
        Assertions.assertFalse(result.getRoutes().isEmpty());

        for (VehicleOptimumRoute route : result.getRoutes()) {
            Vehicle vehicle = route.getVehicle();
            Assertions.assertNotNull(vehicle);
            Assertions.assertFalse(route.getTerminals().isEmpty());
        }
    }
}
