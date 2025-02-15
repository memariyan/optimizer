package com.memariyan.optimizer.service.flow.model;

import com.memariyan.optimizer.service.base.model.LocationModel;

public record TerminalModel(String terminalId, LocationModel location, double packageWeight,
                            double packageVolume, Long startTime, Long endTime) {

}
