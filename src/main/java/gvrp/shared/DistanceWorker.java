package gvrp.shared;

import data_import.Constants;
import data_import.DatasetLoader;
import data_import.NodeTypes;

public class DistanceWorker implements Runnable {
    private final DatasetLoader datasetLoader;
    private final String[] route;
    private final Distance distance_global;
    DistanceWorker(DatasetLoader loader, String[] routes,Distance distance) {
        datasetLoader = loader;
        route = routes;
        distance_global = distance;
    }
    @Override
    public void run() {
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
                synchronized (distance_global.mutex) {
                    distance_global.setDistance(Double.POSITIVE_INFINITY);
                    distance_global.setTime(Double.POSITIVE_INFINITY);

                }
                return;
            }
            // if it is not a customer the tank gets refilled and the waiting time is updated --> this is easier because
            // it doesn't need to check for 2 variables
            if (!(datasetLoader.getTypeForId(route[i]) == NodeTypes.CustomerNode)) {
                if (datasetLoader.getTypeForId(route[i])==NodeTypes.DepotNode && distance_global.getTime()<local_time) {
                    synchronized (distance_global.mutex) {
                        distance_global.setTime(local_time);
                    }
                    local_time = 0;
                }
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
        synchronized (distance_global.mutex) {
            // if it is lower than zero or greater than the 10 3/4 hours it is considered a failure which puts it at infinity
            if (current_tank < 0 || local_time > 10.75) {
                distance_global.setDistance(Double.POSITIVE_INFINITY);
                distance_global.setTime(Double.POSITIVE_INFINITY);
            } else {
                // if it is checked it should check whether it is infinite --> to prevent overflows it adds
                // only to the distance if it is not infinite --> if it is infinite it is considered already a failure
                // because one vehicle failed and so the route is incomplete
                if (!Double.isInfinite(distance_global.getDistance())) {
                    distance_global.setDistance(distance_global.getDistance()+ distance_local);
                }
                // only the highest time is considered aligable for the total time because all vehicles start at the same
                // time because it is able to have an infinite amount of vehicles
                if (local_time > distance_global.getTime()) {
                    distance_global.setTime(local_time);
                }
            }

        }
    }
}
