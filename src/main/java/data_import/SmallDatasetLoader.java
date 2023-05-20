package data_import;

import java.awt.image.AreaAveragingScaleFilter;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SmallDatasetLoader implements Dataset{
    private Random random;
    private boolean is_open;
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
        ArrayList<Object[]> list = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(dataset));
            String dataset_line = reader.readLine();
            dataset_line =reader.readLine();
            while(dataset_line!=null) {
                dataset_line = reader.readLine();
                int first_tab = dataset_line.indexOf('\t');
                String id =dataset_line.substring(0,first_tab);
                System.out.println(id);
            }
            reader.close();
        } catch (Exception e) {
            is_open = false;
            return;
        }





    }

    @Override
    public double getDistance(String point1, String point2) {
        return 0;
    }
}
