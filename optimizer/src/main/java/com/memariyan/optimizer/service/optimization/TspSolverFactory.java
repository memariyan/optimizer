package com.memariyan.optimizer.service.optimization;

import com.memariyan.optimizer.service.optimization.custom.DynamicProgrammingTspSolver;
import com.memariyan.optimizer.service.optimization.model.ShortestPathProblem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TspSolverFactory {

    private final List<TspSolver> solvers;

    public Optional<TspSolver> getTspSolver(ShortestPathProblem model) {
        if (model.getTerminals().length > 10 || Boolean.TRUE.equals(model.isReturnToOrigin())) {
            return Optional.empty();
        }
        return solvers.stream().filter(s -> s.getClass() == DynamicProgrammingTspSolver.class).findFirst();
    }

}
