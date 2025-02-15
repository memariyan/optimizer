package com.memariyan.optimizer.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Accessors(chain = true)
@NoArgsConstructor
public class OptimumRoute {

    private Vehicle vehicle;

    private List<Terminal> terminals;

    private int totalDistance; //in meter

    private int totalDuration; //in seconds

    private double totalPackageWeight;

    private double totalPackageVolume;

    public void addTerminal(Terminal terminal) {
        if (terminals == null) {
            terminals = new ArrayList<>();
        }
        terminals.add(terminal);
    }

    public void addDistance(Integer distance) {
        totalDistance += distance;
    }

    public void addDuration(Integer duration) {
        totalDuration += duration;
    }

    public void addPackageWeight(Double packageWeight) {
        totalPackageWeight += packageWeight;
    }

    public void addPackageVolume(Double packageVolume) {
        totalPackageVolume += packageVolume;
    }

}
