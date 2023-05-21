package gvrp.brute_force;

import data_import.DatasetLoader;
import gvrp.shared.BestDistance;

import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Bruteforce {
    DatasetLoader loader;
    String[] available_nodes;
    ArrayList<String> route;
    HashMap<String,Boolean> marked;
    ThreadPoolExecutor executor;

    Bruteforce() {
        this.loader = new DatasetLoader();
        available_nodes =this.loader.getIDs();
        route = new ArrayList<>();
        marked = new HashMap<>();
        int cores = Runtime.getRuntime().availableProcessors();
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(cores);
    }

    public void start_new_thread() {



    }


    public int recursive_func (int current_try ) {
        if (route.size()==available_nodes.length) {
            start_new_thread();
            BruteForceWorker worker = new BruteForceWorker(this.loader,route);
            executor.submit(worker);
            route.clear();
            marked.clear();
            return current_try==1000000000 ? -1 : current_try+1;
        }
        for (String availableNode : available_nodes) {
            // if the element is already in the route it is supposed to be not marked
            if (marked.get(availableNode)!=null) {
                continue;
            }
            route.add(availableNode);
            marked.put(availableNode,true);
            current_try = recursive_func(current_try);
            if (current_try == -1) {
                return -1;
            }
        }
        return current_try;


    }

}
