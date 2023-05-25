package googleMatrixApplication;

import data_import.DatasetLoader;
import googlemaps.openapiApplication.API;
import org.junit.jupiter.api.Test;

public class DistanceMatrix {

    @Test
    public void testAPI() {
        API api = new API();
        DatasetLoader loader = new DatasetLoader();
        loader.loadDataset();
        api.getMatrix(loader.get_all_node_prop());
    }
}
