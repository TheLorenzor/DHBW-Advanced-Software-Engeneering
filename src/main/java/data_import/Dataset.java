package data_import;

public interface Dataset {

    void loadDataset();
    double getDistance(String point1,String point2);

    NodeTypes getTypeForId(String id);

    String[] getIDs();
}
