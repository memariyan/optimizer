package com.memariyan.optimizer.service.optimization.fixture;

import com.opencsv.CSVReader;
import com.memariyan.optimizer.domain.Distance;
import com.memariyan.optimizer.domain.Location;
import com.memariyan.optimizer.domain.Terminal;
import com.memariyan.optimizer.domain.VehicleCategory;
import com.memariyan.optimizer.service.optimization.model.ShortestPathProblem;
import com.memariyan.optimizer.service.optimization.model.VehicleRoutingProblem;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VrpFixture {

    public static final String TERMINALS_DATASET_FILE_ADDRESS = "src/test/resources/dataset/terminals.csv";

    public static final int DEFAULT_SERVICE_DURATION = 5 * 60;


    public static VehicleRoutingProblem createVrpModel(List<VehicleCategory> categories,
                                                       Long startTime,
                                                       Long endTime,
                                                       boolean returnToOrigin) throws Exception {

        List<Terminal> terminals = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(TERMINALS_DATASET_FILE_ADDRESS))) {
            String[] data = reader.readNext();
            int counter = 0;
            while ((data = reader.readNext()) != null) {
                terminals.add(toTerminal(data, counter++));
            }
        }
        VehicleRoutingProblem vrpModel = new VehicleRoutingProblem();
        vrpModel.setTerminals(terminals.toArray(new Terminal[0]));
        vrpModel.setVehicleCategories(categories);
        vrpModel.setDistances(calculateDistanceMatrix(vrpModel.getTerminals()));
        vrpModel.setStartTime(startTime);
        vrpModel.setEndTime(endTime);
        vrpModel.setServiceDuration(DEFAULT_SERVICE_DURATION);
        vrpModel.setReturnToOrigin(returnToOrigin);
        return vrpModel;
    }

    public static ShortestPathProblem createTspModel(int terminalCount) throws Exception {

        List<Terminal> terminals = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(TERMINALS_DATASET_FILE_ADDRESS))) {
            String[] data = reader.readNext();
            int counter = 0;
            while ((data = reader.readNext()) != null) {
                terminals.add(toTerminal(data, counter++));
            }
        }
        ShortestPathProblem tspProblem = new ShortestPathProblem();
        tspProblem.setTerminals(Arrays.copyOf(terminals.toArray(new Terminal[0]), terminalCount));
        tspProblem.setDistances(calculateDistanceMatrix(tspProblem.getTerminals()));
        return tspProblem;
    }

    private static Terminal toTerminal(String[] data, int index) {
        Terminal terminal = new Terminal();
        terminal.setTerminalId(String.valueOf(index + 1));
        terminal.setPackageWeight(Double.parseDouble(data[2]));
        terminal.setPackageVolume(Double.parseDouble(data[3]));
        terminal.setLocation(new Location()
                .setIndex(index)
                .setLongitude(Double.parseDouble(data[6]))
                .setLatitude(Double.parseDouble(data[7])));
        return terminal;
    }

    private static Distance[][] calculateDistanceMatrix(Terminal[] terminals) {
        Distance[][] distances = new Distance[terminals.length][terminals.length];
        for (int i = 0; i < terminals.length; i++) {
            distances[i] = new Distance[terminals.length];
            for (int j = 0; j < terminals.length; j++) {
                Distance distance = new Distance();
                double distanceInMeters = (getHaversineDistance(terminals[i].getLocation(), terminals[j].getLocation()));
                double durationInSeconds = distanceInMeters * 0.072; // average speed : 50km/h
                distance.setDistance(distanceInMeters);
                distance.setDuration(durationInSeconds);
                distances[i][j] = distance;
            }
        }
        return distances;
    }

    public static VehicleCategory getVehicleCategory(String categoryId, int count, int cost, double weightCapacity, double volumeCapacity) {
        VehicleCategory category = new VehicleCategory();
        category.setCategoryId(categoryId);
        category.setCount(count);
        category.setCost(cost);
        category.setWeightCapacity(weightCapacity);
        category.setVolumeCapacity(volumeCapacity);
        return category;
    }

    public static double getHaversineDistance(Location from, Location to) {
        int radius = 6371; // radius of earth in Km
        double lat1 = from.getLatitude();
        double lat2 = to.getLatitude();
        double lon1 = from.getLongitude();
        double lon2 = to.getLongitude();
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return radius * c * 1000.0;
    }
}
