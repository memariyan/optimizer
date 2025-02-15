package com.memariyan.optimizer.api.msg.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class VehicleCategoryDTO {

    @NotBlank(message = "category.id.required")
    private String categoryId;

    @NotNull(message = "weight.capacity.required")
    private Double weightCapacity;

    @NotNull(message = "volume.capacity.required")
    private Double volumeCapacity;

    private Integer count;

    private Integer cost;

}
