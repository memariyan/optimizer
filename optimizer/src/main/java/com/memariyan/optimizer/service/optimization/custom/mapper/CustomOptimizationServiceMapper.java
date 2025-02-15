package com.memariyan.optimizer.service.optimization.custom.mapper;

import com.memariyan.optimizer.domain.Terminal;
import com.memariyan.optimizer.service.optimization.custom.model.Path;
import com.memariyan.optimizer.service.optimization.model.ShortestPathProblem;
import com.memariyan.optimizer.service.optimization.model.ShortestPathSolution;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomOptimizationServiceMapper {

    public ShortestPathSolution toSolution(Path result, ShortestPathProblem model) {
        ShortestPathSolution solution = new ShortestPathSolution();

        List<Terminal> terminals = new ArrayList<>();
        for (int i = 0; i < result.getVisitOrders().size(); i++) {
            terminals.add(model.getTerminals()[result.getVisitOrders().get(i)]);
        }
        solution.setTerminals(terminals);
        return solution;
    }

}
