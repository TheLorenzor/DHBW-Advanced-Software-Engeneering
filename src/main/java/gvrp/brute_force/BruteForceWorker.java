package gvrp.brute_force;

import data_import.DatasetLoader;
import gvrp.shared.Distance;

public class BruteForceWorker implements Runnable {
    DatasetLoader dataset;
    String [] route;

    BruteForceWorker(DatasetLoader dataset,String[] route) {
        this.dataset = dataset;
        this.route = route;
    }
        @Override
    public void run() {
            Distance t = new Distance(route,dataset);

    }
}
