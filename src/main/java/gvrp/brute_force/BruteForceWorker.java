package gvrp.brute_force;

import data_import.DatasetLoader;
import gvrp.shared.BestDistance;
import gvrp.shared.Distance;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BruteForceWorker implements Runnable {
    DatasetLoader dataset;
    ArrayList<String> route;

    BruteForceWorker(DatasetLoader dataset, ArrayList<String> route) {
        this.dataset = dataset;
        this.route = route;
    }
        @Override
    public void run() {
            // convert to string array
            Object[] obj_arr = route.toArray();
            String[] routes = new String[obj_arr.length];
            for (int i =0;i<obj_arr.length;++i) {
                routes[i] = (String) obj_arr[i];
            }
            // Create Distance and calculate the distance for one iteration
            Distance t = new Distance(routes,dataset);
            double dist =t.getDistance();
            // check if the distance is the new best if so it writes a new file
            if (BestDistance.update_distance(dist)) {
                write_new_line(t);
            }

    }
    private void write_new_line(Distance dist) {
        // get the file
        String root_directory = System.getProperty("user.dir");
        Path path_to_log= Paths.get(root_directory,"dataset","bruteforce_gvrp.log");
        File log = new File(path_to_log.toUri());
        try {
            log.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(log));
            // write as the specification specified
            String line = java.time.LocalTime.now() + " | "+route.toString()+ " | #" +dist.vehicles_tours +
            " #"+dist.vehicles_tours + " | "+ dist.getTime() + " | "+ dist.getDistance();
            // write the line
            writer.write(line);
            writer.newLine();
            writer.close();
        } catch (IOException io) {
            io.printStackTrace();
        }


    }
}
