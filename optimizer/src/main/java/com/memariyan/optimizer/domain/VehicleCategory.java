package com.memariyan.optimizer.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class VehicleCategory {

    private String categoryId;

    private Integer count;

    private Double weightCapacity;

    private Double volumeCapacity;

    private int cost;

}
