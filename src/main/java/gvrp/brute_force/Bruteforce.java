package gvrp.brute_force;

import data_import.DatasetLoader;
import gvrp.shared.BestDistance;

import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Bruteforce {
    DatasetLoader loader;
    String[] available_nodes;
    ArrayList<String> route;
    HashMap<String, Boolean> marked;
    ThreadPoolExecutor executor;
    int i;

    public Bruteforce() {
        this.loader = new DatasetLoader();
        this.loader.loadDataset();
        available_nodes = this.loader.getIDs();
        // set up route to test --> is array list because it is added over time
        route = new ArrayList<>();
        // this is the marked hashpmpa --> markes if it is already used
        marked = new HashMap<>();
        // set up thread pool
        int cores = Runtime.getRuntime().availableProcessors();
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(cores);
    }

    public int do_bruteforce() {
        int a = recursive_func(0);
        executor.shutdown();
        try {
            // waitr until everything is terminated
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return a;
    }

    /**
     * Resursive Function to test out all combinations with n! --> if it is larger than 1*10^9 it automatically returns
     * <p>
     * It Works that the first iteration marks one of and so all other following recurive functions mark the next one of
     * if then all have been marked of once it automatically starta new worker --> then it releases the last one --> with
     * this technique it automatically gets all
     */
    public int recursive_func(int current_try) {
        // if all have been selected --> so it the same length as all ids which makes it possible to test
        if (route.size() == available_nodes.length) {
            // start a brute force worker that calcualtes the distance for the one route
            BruteForceWorker worker = new BruteForceWorker(this.loader, route);
            executor.submit(worker);
            // if it is more than one milliarde it is returning -1 elsewise the new updated
            return current_try == 1000000000 ? -1 : current_try + 1;
        } else {
            for (String availableNode : available_nodes) {
                // if the element is already in the route it is supposed to be not marked
                if (marked.get(availableNode) != null) {
                    continue;
                }
                // add the current route to mark of another array
                route.add(availableNode);
                marked.put(availableNode, true);
                // go into the next so it is perpetually going through all the ids
                current_try = recursive_func(current_try);
                if (current_try == -1) {
                    return -1;
                }
                // remove the last one for the next round so the next recurisve function can get the next round
                String id = route.get(route.size() - 1);
                route.remove(id);
                marked.remove(id);
            }
        }
        // return the final try
        return current_try;
    }

}
