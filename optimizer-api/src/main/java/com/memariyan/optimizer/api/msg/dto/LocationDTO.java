package com.memariyan.optimizer.api.msg.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationDTO {

    @NotNull(message = "latitude.required")
    private Double latitude;

    @NotNull(message = "longitude.required")
    private Double longitude;

}
