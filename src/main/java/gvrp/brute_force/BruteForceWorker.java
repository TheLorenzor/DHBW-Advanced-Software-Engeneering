package gvrp.brute_force;

import data_import.DatasetLoader;
import gvrp.shared.BestDistance;
import gvrp.shared.Distance;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class BruteForceWorker implements Runnable {
    DatasetLoader dataset;
    ArrayList<String> route;

    BruteForceWorker(DatasetLoader dataset, ArrayList<String> route) {
        this.dataset = dataset;
        this.route = route;
    }
        @Override
    public void run() {
            Object[] obj_arr = route.toArray();
            String[] routes = new String[obj_arr.length];
            for (int i =0;i<obj_arr.length;++i) {
                routes[i] = (String) obj_arr[i];
            }

            Distance t = new Distance(routes,dataset);
            double dist =t.getDistance();
            if (BestDistance.update_distance(dist)) {
                // put the whole write to file shit in here
            }

    }
}
