package gvrp.brute_force;

import data_import.DatasetLoader;

import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Bruteforce {
    DatasetLoader loader;
    String[] available_nodes;
    ArrayList<String> route;
    HashMap<String,Boolean> marked;


    Bruteforce() {
        this.loader = new DatasetLoader();
        available_nodes =this.loader.getIDs();
        route = new ArrayList<>();
        marked = new HashMap<>();
    }

    public void start_new_thread() {



    }


    public int recursive_func (int current_try ) {
        for (String availableNode : available_nodes) {
            // if the element is already in the route it is supposed to be not marked
            if (marked.get(availableNode)!=null) {
                continue;
            }
            route.add(availableNode);
            marked.put(availableNode,true);
            int is_more = recursive_func(current_try);
            if (is_more == -1) {
                return -1;
            } else {
                current_try = is_more;
            }
        }
        start_new_thread();
        route.clear();
        marked.clear();
        return 0;
    }

}
