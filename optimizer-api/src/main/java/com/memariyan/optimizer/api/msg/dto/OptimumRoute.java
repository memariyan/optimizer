package com.memariyan.optimizer.api.msg.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class OptimumRoute {

    private VehicleCategoryDTO vehicle;

    private List<TerminalDTO> terminals;

    private Integer totalDistance; //in meter

    private Integer totalDuration; //in milliseconds

    private Double totalPackageWeight;

    private Double totalPackageVolume;
}
