package com.memariyan.optimizer.service.optimization.custom;

import com.memariyan.optimizer.domain.Distance;
import com.memariyan.optimizer.domain.Terminal;
import com.memariyan.optimizer.service.optimization.TspSolver;
import com.memariyan.optimizer.service.optimization.custom.mapper.CustomOptimizationServiceMapper;
import com.memariyan.optimizer.service.optimization.custom.model.Path;
import com.memariyan.optimizer.service.optimization.model.ShortestPathProblem;
import com.memariyan.optimizer.service.optimization.model.ShortestPathSolution;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;

// time complexity is O(n * (2*n))
@Component
@RequiredArgsConstructor
public class DynamicProgrammingTspSolver implements TspSolver {

    private final CustomOptimizationServiceMapper mapper;

    @Override
    public ShortestPathSolution solve(ShortestPathProblem model) {
        Path result = new Solution(model.getTerminals(), model.getDistances()).solve();
        return mapper.toSolution(result, model);
    }

    public static class Solution {
        private final Terminal[] terminals;
        private final Distance[][] distances;
        private final HashMap<Integer, Path> pathMemory;

        public Solution(Terminal[] terminals, Distance[][] distances) {
            this.distances = distances;
            this.terminals = terminals;
            this.pathMemory = new HashMap<>();
        }

        public Path solve() {
            int depotIndex = 0;
            Path result = tsp(depotIndex, 1); // start point is depot index that is equals to 0
            result.getVisitOrders().add(depotIndex);
            result.setVisitOrders(result.getVisitOrders().reversed());
            return result;
        }

        private Path tsp(int from, int mask) {
            int key = getMemoryKey(from, mask);

            if (pathMemory.containsKey(key)) {
                return pathMemory.get(key).clone();
            }
            Path shortestPath = new Path(Integer.MAX_VALUE, new ArrayList<>());

            for (int to = 0; to < terminals.length; to++) {
                if ((mask & (1 << to)) == 0) {
                    Path subnetPath = tsp(to, mask | (1 << to));
                    int transitDistance = distances
                            [terminals[from].getLocation().getIndex()]
                            [terminals[to].getLocation().getIndex()].getDistance().intValue();

                    if (transitDistance + subnetPath.getDistance() < shortestPath.getDistance()) {
                        shortestPath = subnetPath;
                        shortestPath.getVisitOrders().add(to);
                        shortestPath.setDistance(transitDistance + subnetPath.getDistance());
                    }
                }
            }
            pathMemory.put(key, shortestPath);

            return shortestPath.clone();
        }

        private int getMemoryKey(int startNodeIndex, int subNetMask) {
            return startNodeIndex * 100_000_000 + subNetMask;
        }
    }


}
