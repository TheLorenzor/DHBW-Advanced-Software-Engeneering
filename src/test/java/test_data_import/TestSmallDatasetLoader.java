package test_data_import;

import data_import.Constants;
import data_import.DatasetLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestSmallDatasetLoader {
    private DatasetLoader tester;
    @BeforeEach
    void set_up() {
        tester = new DatasetLoader();

    }
    @Test
    void test_load_dataset() {
        tester.loadDataset();
        Assertions.assertEquals(Constants.Depot,tester.getTypeForId("D"));

    }

    @Test
    void test_get_IDs() {
        tester.loadDataset();
        tester.getIDs();
    }
}
