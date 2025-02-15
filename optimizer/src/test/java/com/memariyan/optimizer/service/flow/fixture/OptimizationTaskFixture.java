package com.memariyan.optimizer.service.flow.fixture;

import com.memariyan.optimizer.domain.Location;
import com.memariyan.optimizer.domain.TaskData;
import com.memariyan.optimizer.domain.Terminal;
import com.memariyan.optimizer.domain.VehicleCategory;
import com.memariyan.optimizer.service.optimization.fixture.VrpFixture;
import com.memariyan.optimizer.service.optimization.model.VehicleRoutingProblem;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class OptimizationTaskFixture {

    public static final String TASK_TRACKING_ID = "tracking-id";
    public static final String REPLY_RECEIVER_ID = "reply-topic";
    public static final Double DEFAULT_PACKAGE_VOLUME = 1.00D;
    public static final Double DEFAULT_PACKAGE_WEIGHT = 1.50D;
    public static final Integer DEFAULT_SERVICE_DURATION = 5 * 60;
    public static final Long DEFAULT_START_TIME = 1727263800000L;
    public static final Long DEFAULT_END_TIME = 1727267400000L;


    public static TaskData createTaskForDistanceCalculation() {
        TaskData task = new TaskData();
        task.setTaskId(UUID.randomUUID().toString());
        task.setTrackingId(TASK_TRACKING_ID);
        task.setCreationDate(System.currentTimeMillis());
        task.setReplyReceiverId(REPLY_RECEIVER_ID);
        task.setSources(List.of(createTerminal(35.80688257136062, 51.42883503579884)));
        task.setDestinations(List.of(createTerminal(35.7062193425591D, 51.43298253183742D),
                createTerminal(35.6894904878096, 51.3950453675195),
                createTerminal(35.71151608155559, 51.34766682746636),
                createTerminal(35.675287821828036, 51.41663356639147)));
        return task;
    }

    public static TaskData createTaskForOptimization() throws Exception {
        VehicleCategory van = VrpFixture.getVehicleCategory("van", 2, 30, 300, 3000);
        VehicleCategory car = VrpFixture.getVehicleCategory("car", 8, 10, 200, 1500);

        TaskData task = new TaskData();
        task.setTaskId(UUID.randomUUID().toString());
        task.setTrackingId(TASK_TRACKING_ID);
        task.setCreationDate(System.currentTimeMillis());
        task.setReplyReceiverId(REPLY_RECEIVER_ID);
        task.setStep(TaskData.Step.DISTANCES_CALCULATION);
        task.setStepStatus(TaskData.Status.SUCCESS);
        task.setVehicleCategories(List.of(van, car));
        task.setStartTime(DEFAULT_START_TIME);
        task.setEndTime(DEFAULT_END_TIME);

        VehicleRoutingProblem problem = VrpFixture.createVrpModel(
                task.getVehicleCategories(),
                task.getStartTime(),
                task.getEndTime(),
                false);

        task.setSources(List.of(problem.getTerminals()[0]));
        task.setDestinations(Arrays.asList(problem.getTerminals()).subList(1, problem.getTerminals().length));
        task.setDistances(problem.getDistances());
        task.setServiceDuration(DEFAULT_SERVICE_DURATION);
        return task;
    }

    private static Terminal createTerminal(double latitude, double longitude) {
        Terminal terminal = new Terminal();
        terminal.setLocation(createLocation(0, latitude, longitude));
        terminal.setTerminalId(UUID.randomUUID().toString());
        terminal.setPackageVolume(DEFAULT_PACKAGE_VOLUME);
        terminal.setPackageWeight(DEFAULT_PACKAGE_WEIGHT);
        return terminal;
    }

    private static Location createLocation(int index, double latitude, double longitude) {
        Location location = new Location();
        location.setIndex(index);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return location;
    }
}
