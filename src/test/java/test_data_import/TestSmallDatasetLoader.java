package test_data_import;

import data_import.NodeTypes;
import data_import.DatasetLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class TestSmallDatasetLoader {
    private DatasetLoader tester;
    @BeforeEach
    void set_up() {
        tester = new DatasetLoader();

    }
    @Test
    void test_load_dataset() {
        tester.loadDataset();
        Assertions.assertEquals(NodeTypes.DepotNode,tester.getTypeForId("D"));
    }

    @Test
    void test_get_IDs() {
        tester.loadDataset();
        String[] ids=tester.getIDs();
        Assertions.assertEquals(31,ids.length);
    }

    @Test
    void test_right_calculated() {

        tester.loadDataset();
        // function test manually
        double radiusOfEarth = 3959;
        double dLat = Math.toRadians(36.796046-37.6085124);
        double dLon = Math.toRadians(-77.49439265+76.338677);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(Math.toRadians(37.6085124)) * Math.cos(Math.toRadians(36.796046)) * Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double radius =  radiusOfEarth * c;

        Assertions.assertEquals(radius,tester.getDistance("D","F1"));
    }

}
