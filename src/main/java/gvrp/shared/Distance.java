package gvrp.shared;

import data_import.Constants;
import data_import.NodeTypes;
import data_import.DatasetLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class Distance {

    public int vehicles_tours;
    private ArrayList<String[]> routes;
    private double time;
    private double distance;
    public final Object mutex;
    private final DatasetLoader datasetLoader;

    /**
     * @param dataset the dataset which contains the ids and the distance matrix
     * @param route2d a 2d route --> needs to be arraylist because it isn't guaranteed that the routes of the
     *                different vehicles are the same length
     *
     * */
    public Distance(ArrayList<String[]> route2d, DatasetLoader dataset) {
        datasetLoader = dataset;
        mutex = new Object();
        distance = 0;
        time = 0;
        routes = route2d;
        vehicles_tours = routes.size();
        String[] array_route = route2d.stream().flatMap(Stream::of).toArray(String[]::new);
        test_right(array_route);
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(3);

        for (String[] route : routes) {
            DistanceWorker worker = new DistanceWorker(datasetLoader,route,this);
            executor.submit(worker);
        }
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Distance(String[] route, DatasetLoader dataset) {
        datasetLoader = dataset;
        mutex = new Object();
        distance = 0;
        time = 0;
        vehicles_tours = 1;
        test_right(route);
        DistanceWorker worker = new DistanceWorker(datasetLoader,route,this);
        worker.run();
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
    public void setTime(double time_input) {
        time = time_input;
    }
    public void setDistance(double dist_input) {
        distance = dist_input;
    }
}
