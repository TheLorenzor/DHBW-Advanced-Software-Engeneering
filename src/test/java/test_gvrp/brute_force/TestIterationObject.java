package test_gvrp.brute_force;

import data_import.Constants;
import data_import.DatasetLoader;
import gvrp.shared.Distance;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class TestIterationObject {
    private DatasetLoader loader;
    @BeforeEach
    public void before_each(){
        loader = new DatasetLoader();
        loader.loadDataset();
    }
    @Test
    public void test_empty_tank() {
        String[] route = {"C20","C1","F1"};
        Distance iterate = new Distance(route,loader);
        Assertions.assertTrue(Double.isInfinite(iterate.getDistance()));
        Assertions.assertTrue(Double.isInfinite(iterate.getTime()));

    }
    @Test
    public void test_distance_time_one_route() {
        double test_distance =loader.getDistance("D","C1")+ loader.getDistance("C1","F2") +loader.getDistance("F2","C2")+ loader.getDistance("C2","D");
        double test_time= (test_distance / Constants.velocity)+1.5;
        String[] route = {"C1","F2","C2"};
        Distance iterate = new Distance(route,loader);
        Assertions.assertEquals(test_distance,iterate.getDistance());
        double diff = Math.abs(test_time -iterate.getTime());
        Assertions.assertTrue(diff<1e-7);
    }

    @Test
    public  void test_right_routes() {
        String[] route = {"C1","F2","C2"};
        Distance iterate = new Distance(route,loader);
        Assertions.assertFalse(Double.isInfinite(iterate.getDistance()));

        String[] route2 = {"C1","F2","F2","C2","D","D","D"};
        iterate = new Distance(route2,loader);
        Assertions.assertFalse(Double.isInfinite(iterate.getDistance()));


    }

    @Test
    public  void test_wrong_routes_double_kunde() {
        String[] route = {"C1","F2","C1"};
        Distance iterate = new Distance(route,loader);
        Assertions.assertTrue(Double.isInfinite(iterate.getDistance()));
    }

    @Test
    public void test_wrong_routes_non_existing_key() {
        String[] route = {"C200"};
        Distance iterate = new Distance(route,loader);
        Assertions.assertTrue(Double.isInfinite(iterate.getDistance()));
    }
}
