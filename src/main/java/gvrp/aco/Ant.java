package gvrp.aco;

import java.util.ArrayList;
import java.util.List;

import data_import.Constants;
import data_import.NodeTypes;

public class Ant {
    protected List<String> trail; //trail uses id for each point
    public List<String> getTrail() {
        return trail;
    }

    protected List<String> visitedCustomers;        //visited speichert einen bool für jeden customer 
    protected double current_tank;
    protected double local_time;

    public Ant(int tourSize) {
        trail = new ArrayList<String>();
        visitedCustomers = new ArrayList<String>();
        current_tank = Constants.max_tank;
        local_time = Constants.tour_length;
    }

    public void visitDestination(String des, NodeTypes nType) {
        trail.add(des);
        //if is customer
        if (nType == NodeTypes.CustomerNode) {
            visitedCustomers.add(des);
            //use 0.5time
            local_time -= 0.5;
        }
        if (nType == NodeTypes.StationNode) {
            //reset fuel 
            current_tank = Constants.max_tank;
            //use 0.25time
            local_time -= 0.25;
        }
        if (nType == NodeTypes.DepotNode) {
            //reset vehicle 
            local_time = Constants.tour_length;
            current_tank = Constants.max_tank;
            //use 0.25time
            local_time -= 0.25;
        }
    }

    public void clear() {
        trail.clear();
    }

    public boolean visitedCustomers(String id) {
        return visitedCustomers.contains(id);
    }

    public String CurrentPos()
    {
        return trail.get(trail.size()-1);
    }


}
