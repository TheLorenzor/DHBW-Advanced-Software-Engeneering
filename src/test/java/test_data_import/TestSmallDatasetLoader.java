package test_data_import;

import data_import.SmallDatasetLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestSmallDatasetLoader {
    private SmallDatasetLoader tester;
    @BeforeEach
    void set_up() {
        tester = new SmallDatasetLoader();

    }
    @Test
    void test_load_dataset() {
        tester.loadDataset();
    }
}
