package googlemaps.openapiApplication;

public class API {

    private static String get_url = "https://maps.googleapis.com/maps/api/distancematrix/json?destinations=";

    public void getMatrix(double[][] matrix_points) {
        if (matrix_points[0].length!=2) {
            throw new RuntimeException("The Matrix needs to be 2d");
        }



    }
}
