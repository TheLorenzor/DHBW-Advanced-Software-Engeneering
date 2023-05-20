package data_import;

import java.awt.image.AreaAveragingScaleFilter;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SmallDatasetLoader implements Dataset{
    private Random random;
    private boolean is_open;

    private HashMap<String,Constants> ids;
    public SmallDatasetLoader() {
        random = new Random();
        is_open = false;
    }
    @Override
    public void loadDataset() {
        // open the project root = github root and get files
        String root_directory = System.getProperty("user.dir");
        Path path_to_dataset = Paths.get(root_directory,"dataset");
        File directory = new File(path_to_dataset.toUri());
        // filter all the one that have not large in front
        List<String> names = Stream.of(directory.listFiles()).filter(file -> {
            String name_of_file =file.getName();
            String is_large = name_of_file.substring(0,5);
            return !is_large.equals(new String("Large"));
        }).map(File::getName).collect(Collectors.toList());
        // get a random small path
        path_to_dataset = path_to_dataset.resolve(names.get(random.nextInt(0,names.size())));
        File dataset = new File(path_to_dataset.toUri());
        ArrayList<Object[]> list = new ArrayList<>(25);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(dataset));
            String dataset_line = reader.readLine();
            dataset_line =reader.readLine();
            while(dataset_line!=null) {
                Object[] line_info = new Object[4];
                int end_tab = dataset_line.indexOf('\t');
                if (end_tab>-1) {
                    // first one is ID
                    String information =dataset_line.substring(0,end_tab);
                    line_info[0] = information;
                    int start_tab = end_tab+1;
                    // add another one to skip the first tab
                    end_tab = dataset_line.indexOf('\t',start_tab);
                    information = dataset_line.substring(start_tab,end_tab);
                    switch (information) {
                        case "c" -> line_info[1] = Constants.CustomerNode;
                        case "f" -> line_info[1] = Constants.StationNode;
                        default -> line_info[1] = Constants.Depot;
                    }
                    // get the longitude
                    start_tab = end_tab+1;
                    end_tab = dataset_line.indexOf('\t',start_tab);
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
        } catch (Exception e) {
            is_open = false;
            return;
        }
        if (is_open) {
            ids = new HashMap<>();
            for (int i =0;i<list.size();++i) {
                ids.put((String)list.get(i)[0],(Constants) list.get(i)[1]);
            }
            System.out.println(ids.get("D"));

        }





    }

    @Override
    public double getDistance(String point1, String point2) {
        return 0;
    }
}
