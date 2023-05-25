package googlemaps.openapiApplication;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.Gson;
public class API {

    private static String get_url = "https://maps.googleapis.com/maps/api/distancematrix/json?mode=driving&origins=";
    private static String API_key = "";
    /**
     * @param matrix_points: 2d array --> first element: longitude, second latitude
     * */
    public void getMatrix(double[][] matrix_points)  {
        if (matrix_points.length<2 || matrix_points[0].length!=2) {
            throw new RuntimeException("The Matrix needs to be 2d");
        }
        StringBuilder requests = new StringBuilder(matrix_points[0][1] + "," + matrix_points[0][1] + "&destination=");
        for (int i =1;i<matrix_points.length;++i) {
            requests.append(matrix_points[i][1]).append(",").append(matrix_points[i][0]).append("|");

        }
        requests.deleteCharAt(requests.length()-1);
        requests.append("  &units=metric").append("&key=").append(API_key);
        System.out.println(requests);
        if (requests.length()>0) {
            return;
        }

        try {
            URL url = new URL(requests.toString());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Java Agent");
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder result = new StringBuilder();

            for (String line; (line=reader.readLine())!=null;) {
                result.append(line);
            }

            Gson object = new Gson();
            object.fromJson(result.toString(),Object.class);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
