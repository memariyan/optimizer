package com.memariyan.optimizer.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Terminal {

    private String terminalId;

    private Location location;

    private double packageWeight;

    private double packageVolume;

    private Long estimatedVisitTime;

}
