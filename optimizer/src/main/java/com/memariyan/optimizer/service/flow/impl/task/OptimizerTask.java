package com.memariyan.optimizer.service.flow.impl.task;

import com.memariyan.optimizer.api.msg.response.OptimizationResponse;
import com.memariyan.optimizer.client.publisher.ResultPublisher;
import com.memariyan.optimizer.config.optimization.OptimizationProperties;
import com.memariyan.optimizer.domain.TaskData;
import com.memariyan.optimizer.domain.repository.OptimizationTaskRepository;
import com.memariyan.optimizer.domain.OptimumRoute;
import com.memariyan.optimizer.service.flow.mapper.TaskFlowServiceMapper;
import com.memariyan.optimizer.metric.OptimizationTaskTimeMetricHandler;
import com.memariyan.optimizer.service.optimization.TspSolver;
import com.memariyan.optimizer.service.optimization.TspSolverFactory;
import com.memariyan.optimizer.service.optimization.VrpSolver;
import com.memariyan.optimizer.service.optimization.model.ShortestPathProblem;
import com.memariyan.optimizer.service.optimization.model.ShortestPathSolution;
import com.memariyan.optimizer.service.optimization.model.VehicleRoutingProblem;
import com.memariyan.optimizer.service.optimization.model.VehicleRoutingSolution;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Slf4j
public class OptimizerTask extends AbstractTask {

    private final TaskData task;

    private final VrpSolver vrpSolver;

    private final TspSolverFactory tspSolverFactory;

    private final ResultPublisher resultPublisher;

    private final OptimizationProperties properties;

    private final TaskFlowServiceMapper mapper;

    public OptimizerTask(TaskData task,
                         OptimizationTaskRepository repository,
                         OptimizationTaskTimeMetricHandler metricHandler,
                         VrpSolver vrpSolver, TspSolverFactory tspSolverFactory,
                         ResultPublisher resultPublisher, OptimizationProperties properties,
                         TaskFlowServiceMapper mapper) {

        super(task, repository, metricHandler);
        this.task = task;
        this.vrpSolver = vrpSolver;
        this.tspSolverFactory = tspSolverFactory;
        this.resultPublisher = resultPublisher;
        this.properties = properties;
        this.mapper = mapper;
    }

    @Override
    public void doTask() {
        List<OptimumRoute> routes = findSolution();
        task.setRoutes(routes);

        OptimizationResponse response = CollectionUtils.isNotEmpty(routes) ?
                mapper.toSuccessOptimizationResponse(routes, task.getTrackingId()) :
                mapper.toFailedOptimizationResponse(task.getTrackingId(), "not found any optimum solution");

        resultPublisher.publish(response, task.getReplyReceiverId());
        log.info("task with trackingId: {} completed with status :{} and reply was sent to topic :{}",
                task.getTrackingId(), response.getStatus(), task.getReplyReceiverId());
    }

    public List<OptimumRoute> findSolution() {
        VehicleRoutingProblem problem = mapper.toVehicleRoutingProblem(task);

        return vrpSolver.solve(problem).map(vrpSolution -> {
            optimizeCalculatedPathIfNeed(problem, vrpSolution);
            return mapper.toRoutes(problem, vrpSolution);

        }).orElseGet(List::of);
    }

    private void optimizeCalculatedPathIfNeed(VehicleRoutingProblem problem, VehicleRoutingSolution vrpSolution) {
        if (properties.getSolver().isApplyTspSolver()) {
            List<ShortestPathProblem> models = mapper.toShortestPathProblems(
                    vrpSolution, problem.getDistances(), problem.isReturnToOrigin());

            for (int i = 0; i < models.size(); i++) {
                ShortestPathProblem model = models.get(i);
                Optional<TspSolver> tspSolver = tspSolverFactory.getTspSolver(model);
                if (tspSolver.isPresent()) {
                    ShortestPathSolution tspSolution = tspSolver.get().solve(model);
                    vrpSolution.getRoutes().get(i).setTerminals(tspSolution.getTerminals());
                }
            }
        }
    }

    @Override
    public TaskData.Step getStep() {
        return TaskData.Step.OPTIMIZATION;
    }

}
