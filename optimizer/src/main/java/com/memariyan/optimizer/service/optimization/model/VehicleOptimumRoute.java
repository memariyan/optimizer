package com.memariyan.optimizer.service.optimization.model;

import com.memariyan.optimizer.domain.Terminal;
import com.memariyan.optimizer.domain.Vehicle;
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
public class VehicleOptimumRoute {

    public VehicleOptimumRoute(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    private Vehicle vehicle;

    private List<Terminal> terminals;

    public void addTerminal(Terminal terminal) {
        if (terminals == null) {
            terminals = new ArrayList<>();
        }
        terminals.add(terminal);
    }

}
