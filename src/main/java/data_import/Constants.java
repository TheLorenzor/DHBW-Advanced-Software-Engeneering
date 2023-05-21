package data_import;

public class Constants {

    //data
    public static final double max_tank= 60;
    public static final double consumption = 0.2; // galon per mile
    public static final double tour_length = 11;
    public static final double velocity = 40; // miles per hour
    public static final int numberOfCustomers = 20;
    public static final int numberOfRefuelingStation = 10;
    public static final int numberOfNodes = 31;
    // algorithm
    public static final double initialPheromoneValue = 1.0;
    public static final double alpha = 2;              // pheromone importance
    public static final double beta = 2;               // distance priority
    public static final double evaporation = 0.05;
    public static final double q = 500;                // pheromone left on trail per ant
    public static final double randomFactor = 0.01;    // introducing randomness
    public static final int maximumIterations = 1000;
    public static final int numberOfAnts  = 20;
}

