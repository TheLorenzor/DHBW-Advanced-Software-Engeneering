package gvrp.shared;

import data_import.Constants;
import data_import.NodeTypes;
import data_import.DatasetLoader;

import java.util.HashMap;
import java.util.stream.Stream;

public class Distance {

    public int vehicles_tours;
    private String[] routes;
    private double time;
    private double distance;
    private final Object mutex;
    private final DatasetLoader datasetLoader;

    public Distance(String[][] routes, DatasetLoader dataset) {
        datasetLoader = dataset;
        mutex = new Object();
        distance = 0;
        time = 0;
        vehicles_tours = routes.length;
        String[] long_route = Stream.of(routes).flatMap(Stream::of).toArray(String[]::new);
        test_right(long_route);
        for (String[] route : routes) {
            calculate_time_and_distance(route);
        }
    }

    public Distance(String[] route, DatasetLoader dataset) {
        datasetLoader = dataset;
        mutex = new Object();
        distance = 0;
        time = 0;
        vehicles_tours = 1;
        test_right(route);
        calculate_time_and_distance(route);
    }
    private void calculate_time_and_distance(String[] route) {
        //initiate the starting parameter

        // add already 15 minutes because on leaving the depot it automatically needs 15 minutes
        double local_time = 0.25;
        double distance_local = 0;
        double current_tank = Constants.max_tank;
        double new_distance = 0;
        // iterate through the journey and
        for (int i = 0; i < route.length; ++i) {
            if (i == 0) {
                //if i =0 it adds the distance_local from the D because the depots are left out at the distnce
                new_distance = datasetLoader.getDistance("D", route[0]);
            } else {
                new_distance = datasetLoader.getDistance(route[i - 1], route[i]);
            }
            // calculate the new tank
            current_tank = current_tank - (new_distance * Constants.consumption);
            distance_local = distance_local + new_distance;
            local_time = local_time + (new_distance / Constants.velocity);
            // check instantly whether it is <0 because the vehicle might go out before it is able to finish
            if (current_tank < 0) {
                synchronized (mutex) {
                    distance = Double.POSITIVE_INFINITY;
                    time = Double.POSITIVE_INFINITY;
                }
                return;
            }
            // if it is not a customer the tank gets refilled and the waiting time is updated --> this is easier because
            // it doesn't need to check for 2 variables
            if (!(datasetLoader.getTypeForId(route[i]) == NodeTypes.CustomerNode)) {
                local_time = local_time + 0.25;
                current_tank = Constants.max_tank;
            } else {
                // if it is a customer node it adds 0.5
                local_time = local_time + 0.5;
            }
        }

        // calculate the way for the last return back to the depot
        new_distance = datasetLoader.getDistance(route[route.length - 1], "D");
        current_tank = current_tank - (new_distance * Constants.consumption);
        distance_local = distance_local + new_distance;
        local_time = local_time + (new_distance / Constants.velocity);
        // synchronization to make it thread safe
        synchronized (mutex) {
            // if it is lower than zero or greater than the 10 3/4 hours it is considered a failure which puts it at infinity
            if (current_tank < 0 || local_time > 10.75) {
                distance = Double.POSITIVE_INFINITY;
                time = Double.POSITIVE_INFINITY;
            } else {
                // if it is checked it should check whether it is infinite --> to prevent overflows it adds
                // only to the distance if it is not infinite --> if it is infinite it is considered already a failure
                // because one vehicle failed and so the route is incomplete
                if (!Double.isInfinite(distance)) {
                    distance = distance + distance_local;
                }
                // only the highest time is considered aligable for the total time because all vehicles start at the same
                // time because it is able to have an infinite amount of vehicles
                if (local_time > time) {
                    time = local_time;
                }
            }

        }
    }
    public void test_right(String[] route) {
        String [] compare_ids =datasetLoader.getIDs();
        HashMap<String,Integer> validator = new HashMap<>();
        for (String id :compare_ids) {
            validator.put(id,0);
        }
        for (String id:route) {
            Integer count= validator.get(id);
            if (count==null ||(datasetLoader.getTypeForId(id)==NodeTypes.CustomerNode && count==1)) {
                distance = Double.POSITIVE_INFINITY;
                time = Double.POSITIVE_INFINITY;
                return;
            }
            ++count;
            validator.put(id,count);
        }

    }
    public double getDistance() {
        return distance;
    }

    public double getTime() {
        return time;
    }
}
