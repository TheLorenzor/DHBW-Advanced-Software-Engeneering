package test_gvrp.brute_force;

import data_import.DatasetLoader;
import gvrp.aco.ACO_SingleThread;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestAcoSingleThread {
    private DatasetLoader loader;
    @BeforeEach
    public void before_each(){
        loader = new DatasetLoader();
        loader.loadDataset();
    }
    @Test
    public void test_create_aco() {
        ACO_SingleThread aco = new ACO_SingleThread(loader);
    }

    @Test
    public void test_run_aco() {
        ACO_SingleThread aco = new ACO_SingleThread(loader);
        aco.run();
    }
}
