package test_gvrp.brute_force;

import data_import.DatasetLoader;
import gvrp.brute_force.Bruteforce;
import gvrp.shared.BestDistance;
import org.junit.jupiter.api.Test;

public class TestBruteForce {
    public static void main (String[] args) {
        Bruteforce nb = new Bruteforce();
        if(nb.recursive_func(0)==-1) {
            System.out.println("More than a Milliarde");
            System.out.println(BestDistance.getBest_distance());
        }
    }
}
