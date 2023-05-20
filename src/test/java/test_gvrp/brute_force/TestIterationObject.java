package test_gvrp.brute_force;

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
        ArrayList<String> list =new ArrayList<>(Arrays.asList(loader.getIDs()));
        String[] route = {"C20","C1","F1"};
        Distance iterate = new Distance(route,loader);
        Assertions.assertTrue(Double.isInfinite(iterate.getDistance()));
    }
}
