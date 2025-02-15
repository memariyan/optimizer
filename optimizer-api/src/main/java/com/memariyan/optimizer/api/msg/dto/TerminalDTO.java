package com.memariyan.optimizer.api.msg.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TerminalDTO {

    @NotBlank(message = "terminal.id.required")
    private String terminalId;

    @Valid
    @NotNull(message = "location.required")
    private LocationDTO location;

    private Double packageWeight;

    private Double packageVolume;

    private Long estimatedVisitTime; // in epoc

}
