package com.memariyan.optimizer.service.optimization.model;

import com.memariyan.optimizer.domain.Distance;
import com.memariyan.optimizer.domain.Terminal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShortestPathProblem {

    private boolean returnToOrigin;

    private Terminal[] terminals;

    private Distance[][] distances;

}
