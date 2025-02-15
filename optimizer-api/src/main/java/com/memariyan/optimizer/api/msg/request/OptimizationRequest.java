package com.memariyan.optimizer.api.msg.request;

import com.memariyan.optimizer.api.msg.dto.VehicleCategoryDTO;
import com.memariyan.optimizer.api.msg.dto.TerminalDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class OptimizationRequest {

    public static final String REPLY_TOPIC_HEADER_NAME = "REPLY_TOPIC_HEADER";

    @NotBlank(message = "trackingId.required")
    private String trackingId;

    @Valid
    @NotNull(message = "sources.are.empty")
    private List<TerminalDTO> sources;

    @Valid
    @NotNull(message = "destinations.are.empty")
    private List<TerminalDTO> destinations;

    private List<VehicleCategoryDTO> vehicleCategories;

    private Boolean returnToOrigin;

    private Integer serviceDuration; // in seconds

    private Long startTime; // in epoc

    private Long endTime; // in epoc

}
