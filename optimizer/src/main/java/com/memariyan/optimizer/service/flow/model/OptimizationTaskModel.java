package com.memariyan.optimizer.service.flow.model;

import java.util.List;

public record OptimizationTaskModel(String trackingId,
                                    String replyReceiverId,
                                    Integer serviceDuration, // in seconds
                                    Long startTime, // in epoc
                                    Long endTime, // in epoc
                                    List<TerminalModel> sources,
                                    List<TerminalModel> destinations,
                                    List<VehicleCategoryModel> vehicleCategories) {
}
