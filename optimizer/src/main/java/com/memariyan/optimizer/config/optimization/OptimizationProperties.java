package com.memariyan.optimizer.config.optimization;

import com.memariyan.optimizer.domain.enums.RoutingType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "com.memariyan.optimizer")
public class OptimizationProperties {

    private SupplyConfig supply;

    private ProcessConfig process;

    private RoutingConfig routing;

    private SolverConfig solver;

    @Getter
    @Setter
    public static class SupplyConfig {

        private Integer delay;

        private Integer count;

        private Boolean enabled;

    }

    @Getter
    @Setter
    public static class ProcessConfig {

        private Integer readyThreadCountAtMoment;

        private Integer maxThreadCountAtMoment;

    }

    @Getter
    @Setter
    public static class RoutingConfig {

        private RoutingType type;

        private Integer osrmThreshold;

        private Double meterDurationInSecond;

    }

    @Getter
    @Setter
    public static class SolverConfig {
        private int defaultTimeout;
        private boolean applyTspSolver;
        private Integer improvementDeadline;
    }

}
