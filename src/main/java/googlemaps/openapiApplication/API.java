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
    public Object getMatrix(double[][] matrix_points)  {
        // if it is not length 2 (so minimum origin and destination) and has 2 points per array entry
        if (matrix_points.length<2 || matrix_points[0].length!=2) {
            throw new RuntimeException("The Matrix needs to be 2d");
        }
        // add the url together
        StringBuilder requests = new StringBuilder(get_url+matrix_points[0][1] + "," + matrix_points[0][1] + "&destination=");
        for (int i =1;i<matrix_points.length;++i) {
            // append all the destinations to get the shortest route
            requests.append(matrix_points[i][1]).append(",").append(matrix_points[i][0]).append("|");

        }
        // add the final parts
        requests.deleteCharAt(requests.length()-1);
        requests.append("&units=metric").append("&key=").append(API_key);
        if (requests.length()>0) {
            return null;
        }

        try {
            // start url request
            URL url = new URL(requests.toString());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Java Agent");
            //do the url request
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder result = new StringBuilder();
            // read all lines
            for (String line; (line=reader.readLine())!=null;) {
                result.append(line);
            }
            // convert to new json object und return it on request
            Gson object = new Gson();
            return object.fromJson(result.toString(),Object.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;


    }
}
