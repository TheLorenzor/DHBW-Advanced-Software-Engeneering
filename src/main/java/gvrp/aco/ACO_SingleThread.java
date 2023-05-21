package gvrp.aco;

import java.lang.module.Configuration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.crypto.spec.DESedeKeySpec;

import data_import.*;
import gvrp.shared.Distance;

public class ACO_SingleThread {

    private DatasetLoader datasetLoader;
    private Random random = new Random();
    
    private final HashMap<String,Double> pheromoneMatrix;        //saves the pheromones for each path
    private final List<Ant> ants = new ArrayList<>();
    private int currentIndex;

    private List<String> bestTourOrder;
    private double bestTourLength;

    public StringBuilder stringBuilder = new StringBuilder();

//region Instance
//---------------------------------------------------------------------------------------
    public ACO_SingleThread()
    {
        datasetLoader = new DatasetLoader();
        datasetLoader.loadDataset();
        pheromoneMatrix = CreatePheromoneMatrix();
        for (int i = 0; i < Constants.numberOfCustomers; i++) {
            ants.add(new Ant(Constants.numberOfCustomers));
        }
    }

    private HashMap<String,Double> CreatePheromoneMatrix() {
        HashMap<String,Double> temp = new HashMap<>();
        for (String xID : datasetLoader.getIDs()) {
           for (String yID : datasetLoader.getIDs()) {
                temp.put(xID+yID,Constants.initialPheromoneValue);
           } 
        }
        return temp;
    }


    public void run() {
        long runtimeStart = System.currentTimeMillis();

        setupAnts();
        clearTrails();

        for (int i = 0; i < Constants.maximumIterations; i++) {
            moveAnts();
            updateTrails();
            updateBest();
        }
        //print result
        
        
        stringBuilder.append("\nbest tour length : ").append((bestTourLength));
        stringBuilder.append("\nbest tour order  : ").append(Arrays.toString(bestTourOrder.toArray()));
        stringBuilder.append("\nruntime          : ").append(System.currentTimeMillis() - runtimeStart).append(" ms");

        System.out.println(stringBuilder);
    }
//---------------------------------------------------------------------------------------
//endregion

//region Ants
//---------------------------------------------------------------------------------------

    private void setupAnts() {
            //each represent a different solution
            for (int i = 0; i < Constants.numberOfAnts; i++) {
                for (Ant ant : ants) {
                    ant.clear();
                    ant.visitDestination("D", NodeTypes.DepotNode,0);  //Set Depot as start point 
                }
            }
            
            currentIndex = 0;
    }

    private void moveAnts() { 
        for (int i = currentIndex; i < Constants.numberOfCustomers - 1; i++) {
            for (Ant ant : ants) {
                Boolean visitedCustomer = false;
                while(!visitedCustomer)
                {
                    String id = selectNextDestination(ant);
                    String[] route = {ant.CurrentPos(),id};
                    double dis = new Distance(route, datasetLoader).getDistance(); 
                    NodeTypes type = datasetLoader.getTypeForId(id);
                    ant.visitDestination(id,type, dis);
                    visitedCustomer = type == NodeTypes.CustomerNode;
                }
            }
            currentIndex++;
        }
    }


    private String selectNextDestination(Ant ant){
        //look for next best destination 
        //getValidTrails
        HashMap<String,Double> destinations = new HashMap<>();
        for (String des : getValidDestintations(ant)) {
            destinations.put(des, 0.);
        } 
        
        //randomness
        int t = random.nextInt(Constants.numberOfCustomers-1);
        if(random.nextDouble() < Constants.randomFactor)
        {
            for (String id : destinations.keySet()) {
                if(id == "C"+t && ant.visitedCustomers(id)){
                    return id;
                }
            }
        }
        calculateProbabilities(ant, destinations);

        double randomNumber = random.nextDouble();
        double total = 0;

        for (String id : destinations.keySet()) {
            total += destinations.get(id);
            if(total >= randomNumber)
            {
                return id;
            }
        }
        return "D";
    }

    private void calculateProbabilities(Ant ant, HashMap<String, Double> destinations) {
            //current position
            //sum pheromones of all not visited Trails 
            //else 
            //numerator is the pheromone of a single  trail 
            //double numerator = Math.pow(trails[i][j], Configuration.INSTANCE.alpha) * Math.pow(1.0 / graph[i][j], Configuration.INSTANCE.beta);
            //probabilities[j] = numerator / pheromone; --> calculates the probality based on the relative pheromones 
            double pheromone = 0.0;
            for (String des : destinations.keySet()) {
                pheromone += Math.pow(pheromoneMatrix.get(ant.CurrentPos()+des),Constants.alpha) 
                           * Math.pow(1.0 / datasetLoader.getDistance(ant.CurrentPos(), des), Constants.beta);
            }

            for (String des : destinations.keySet()) {
                double numerator = Math.pow(pheromoneMatrix.get(ant.CurrentPos()+des),Constants.alpha) 
                                 * Math.pow(1.0 / datasetLoader.getDistance(ant.CurrentPos(), des), Constants.beta);
                destinations.put(des, numerator/pheromone);
            }
    }

    public List<String> getValidDestintations(Ant ant){
       //rules 
       /*
        Start with all Customers 
            not visited and
            there needs to be enough Time and Fuel to reach Customer and Depot
            
            if there is no valid customer
                check if there ist enough time to reach fuel Station and Customer and depot
            if there is no valid station 
                go to depot     
        */

        List<String> Customer = new ArrayList<>();
        List<String> Station = new ArrayList<>();
        List<String> returnList = new ArrayList<>();

        for (String id : datasetLoader.getIDs()) {
            if(datasetLoader.getTypeForId(id) == NodeTypes.CustomerNode && ant.trail.contains(id))
            {
                Customer.add(id);
            } else if (datasetLoader.getTypeForId(id) == NodeTypes.StationNode)
            {
                Station.add(id);
            }
        }
        
        for (String customerID : Customer) {
            List<String> route = new ArrayList<>();

            route.add(ant.CurrentPos());
            route.add(customerID);
            route.add("D");
            double dis = new Distance(route.toArray(new String[0]), datasetLoader).getDistance(); 

            double time = ant.getLocal_time();
            double fuelleft = ant.getCurrent_tank();

            time += (dis * Constants.consumption) + Constants.CustomerTime + Constants.StationTime;
            fuelleft -= (dis / Constants.velocity);

             if(time >= 11 ||  fuelleft >= 0)
             {
                returnList.add(customerID);
             }

        }
        if(returnList.size() == 0){
            for (String stationId : Station) {
                List<String> route = new ArrayList<>();

            route.add(ant.CurrentPos());
            route.add(stationId);
            route.add("D");
            double dis = new Distance(route.toArray(new String[0]), datasetLoader).getDistance(); 

            double time = ant.getLocal_time();
            double fuelleft = ant.getCurrent_tank();

            time += (dis * Constants.consumption) +Constants.CustomerTime + Constants.StationTime;
            fuelleft -= (dis / Constants.velocity);

             if(time >= 11 ||  fuelleft >= 0)
             {
                returnList.add(stationId);
             }
            }
        }
        return returnList;
    }

    public int generateRandomNumber(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }


//---------------------------------------------------------------------------------------
//endregion


//region Trails
//---------------------------------------------------------------------------------------

    private void updateTrails() {
        //evaporate pheromones on all Trails 
        for ( String trail : pheromoneMatrix.keySet()) {
            pheromoneMatrix.put(trail, pheromoneMatrix.get(trail)*Constants.evaporation);
        }
        //increase the pheromones of traveled paths a
        
        for (Ant ant : ants) {
            String[] TrailArray = ant.trail.toArray(new String[0]);
            Distance d = new Distance(TrailArray, datasetLoader);
            double contribution = Constants.q / d.getDistance();
            for (int i = 0; i < TrailArray.length - 1; i++) {
                String key = TrailArray[i]+TrailArray[i + 1];
                Double curValue = pheromoneMatrix.get(key);
                pheromoneMatrix.put(key, (curValue+contribution));
            }

        }
    }

    private void updateBest() {
        //save the best route
        if (bestTourOrder == null) {
            bestTourOrder = ants.get(0).trail;
            bestTourLength = new Distance(ants.get(0).trail.toArray(new String[0]), datasetLoader).getDistance();
        }

        for (Ant ant : ants) {
            double dis = new Distance(ants.get(0).trail.toArray(new String[0]), datasetLoader).getDistance();
            if (dis < bestTourLength) {
                bestTourLength = dis;
                bestTourOrder = new ArrayList<>(ant.trail);
            }
        }
    }

    private void clearTrails() {
        //reset the pheromones to initial values
        pheromoneMatrix.clear();
        for (String trail : pheromoneMatrix.keySet()) {
            pheromoneMatrix.put(trail, Constants.initialPheromoneValue);
        }
    }

//---------------------------------------------------------------------------------------
//endregion    
}