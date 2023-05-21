package gvrp.brute_force;

import data_import.DatasetLoader;

import java.net.DatagramSocket;

public class Bruteforce {
    DatasetLoader loader;
    Bruteforce() {
        this.loader = new DatasetLoader();

    }

    public void do_bruteforce() {
        String [] available_nodes = this.loader.getIDs();

    }

}
