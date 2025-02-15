package com.memariyan.optimizer.domain;

import lombok.*;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Location {

    private int index;

    private double latitude;

    private double longitude;

}
