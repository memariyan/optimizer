package com.memariyan.optimizer.service.optimization.google;

import com.memariyan.optimizer.config.optimization.OptimizationProperties;
import com.memariyan.optimizer.domain.Terminal;
import com.memariyan.optimizer.service.optimization.fixture.VrpFixture;
import com.memariyan.optimizer.service.optimization.model.ShortestPathProblem;
import com.memariyan.optimizer.service.optimization.model.ShortestPathSolution;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

@Slf4j
@ExtendWith(SpringExtension.class)
@Import(GoogleTspSolver.class)
@ComponentScan(basePackages = "com.memariyan.optimizer.service.optimization.google")
public class GoogleTspSolverTest {

    @Autowired
    private GoogleTspSolver solver;

    @MockBean
    private OptimizationProperties properties;

    @Test
    @DisplayName("solve - " +
            "given-> an array of terminals with a distance matrix" +
            "when -> solve method is called , " +
            "then -> solution is returned and optimum path is returned")
    public void test() throws Exception {

        // given ->
        ShortestPathProblem model = VrpFixture.createTspModel(15);

        // when ->
        ShortestPathSolution result = solver.solve(model);

        // then ->
        Assertions.assertFalse(result.getTerminals().isEmpty());
        Assertions.assertEquals(result.getTerminals().size(), model.getTerminals().length);

        String inputTerminalSequences = getTerminalSequences(Arrays.asList(model.getTerminals()));
        String resultTerminalSequences = getTerminalSequences(result.getTerminals());
        Assertions.assertNotEquals(inputTerminalSequences, resultTerminalSequences);
        log.info("the result terminal sequences is : {}", resultTerminalSequences);
    }

    public String getTerminalSequences(List<Terminal> terminals) {
        return terminals.stream().map(Terminal::getTerminalId).reduce((a, b) -> a + " -> " + b).get();
    }
}
