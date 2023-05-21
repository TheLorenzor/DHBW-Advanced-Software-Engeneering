package gvrp.shared;

public class BestDistance {

    static private final Object mutex = new Object();
    static private double best_distance;


    public static boolean update_distance(double best_distance) {
        synchronized (mutex) {
            if (!(best_distance<BestDistance.best_distance)){
                return false;
            }
            BestDistance.best_distance = best_distance;
            return true;
        }
    }
}
