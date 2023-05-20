package data_import;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class DatasetLoader implements Dataset{
    private Random random;
    private boolean is_open;

    private HashMap<String,Constants> ids;
    private HashMap<String,Double> distance_matrix;
    public DatasetLoader() {
        random = new Random();
        is_open = false;
    }
    @Override
    public void loadDataset() {
        // open the project root = github root and get files
        String root_directory = System.getProperty("user.dir");
        Path path_to_dataset = Paths.get(root_directory,"dataset");
        path_to_dataset = path_to_dataset.resolve("dataset.csv");
        File dataset = new File(path_to_dataset.toUri());
        ArrayList<Object[]> list = new ArrayList<>(25);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(dataset));
            String dataset_line = reader.readLine();
            dataset_line =reader.readLine();
            while(dataset_line!=null) {
                Object[] line_info = new Object[4];
                int end_tab = dataset_line.indexOf(';');
                if (end_tab>-1) {
                    // first one is ID
                    String information =dataset_line.substring(0,end_tab);
                    line_info[0] = information;
                    int start_tab = end_tab+1;
                    // add another one to skip the first tab
                    end_tab = dataset_line.indexOf(';',start_tab);
                    information = dataset_line.substring(start_tab,end_tab);
                    switch (information) {
                        case "c" -> line_info[1] = Constants.CustomerNode;
                        case "f" -> line_info[1] = Constants.StationNode;
                        default -> line_info[1] = Constants.Depot;
                    }
                    // get the longitude
                    start_tab = end_tab+1;
                    end_tab = dataset_line.indexOf(";",start_tab);
                    information = dataset_line.substring(start_tab,end_tab);
                    line_info[2] = Double.parseDouble(information);
                    // get the latitude
                    start_tab = end_tab+1;
                    information = dataset_line.substring(start_tab);
                    line_info[3] = Double.parseDouble(information);
                    // add to arraylist
                    list.add(line_info);
                }
                dataset_line = reader.readLine();
            }
            reader.close();
            is_open =true;
        } catch (IOException io) {
            is_open = false;
            return;
        }
        ids = new HashMap<>();
        distance_matrix = new HashMap<>();
        // iterate over the array list to add all the ids and types to the class attributes
        for (int i =0;i<list.size();++i) {
            // add teh attributes and it will be accassible via the ID --> is for later to get the next id

            ids.put((String)list.get(i)[0],(Constants) list.get(i)[1]);
            for (int j=0;j<list.size();++j) {
                String id_i = (String) list.get(i)[0];
                String id_j = (String) list.get(j)[0];
                if (i==j) {
                    distance_matrix.put(id_i+":"+id_j, 0.0);
                    continue;
                }
                double lat_i = (Double) list.get(i)[3];
                double lon_i  =(Double) list.get(i)[2];
                double lat_j =  (Double) list.get(j)[3];
                double lon_j =  (Double) list.get(j)[2];

                Double distance = Math.abs(calculate_distance(lat_i, lat_j, lon_i, lon_j));
                distance_matrix.put(id_i+":"+id_j,distance);

            }
        }
    }

    private double calculate_distance(double lat1,double lat2,double lon1,double lon2) {
        double radiusOfEarth = 3959 ; // miles, 6371km;
        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(lon2-lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return radiusOfEarth * c;
    }
    @Override
    public double getDistance(String point1, String point2) {
        Double distance = distance_matrix.get(point1+":"+point2);
        return Objects.requireNonNullElse(distance, -1.0);
    }
    ;
    @Override
    public Constants getTypeForId(String id) {
        return ids.get(id);
    }

    @Override
    public String[] getIDs() {
        Object[] obj_arr = ids.keySet().toArray();
        String[] ids_string = new String[obj_arr.length];
        for (int i =0;i< obj_arr.length;++i) {
            ids_string[i] = (String) obj_arr[i];
        }
        return ids_string;
    }

}
