package com.memariyan.optimizer.service.optimization.model;

import com.memariyan.optimizer.domain.Terminal;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ShortestPathSolution {

    private List<Terminal> terminals;

    public void addTerminal(Terminal terminal) {
        if (terminals == null) {
            terminals = new ArrayList<>();
        }
        terminals.add(terminal);
    }

}
