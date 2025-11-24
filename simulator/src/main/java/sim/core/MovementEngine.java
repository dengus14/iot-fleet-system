package sim.core;

import sim.graph.UndirectedGraph;

import java.util.List;

import static sim.core.DeviceState.*;

public class MovementEngine {
    private final UndirectedGraph graph;
    public MovementEngine(UndirectedGraph graph){
        this.graph=graph;
    }
    public void update(Device device, double deltaSeconds){
        if (device.getState()==EngineOff) return;
        ensureNextNode(device);
        double kmPerSecond = device.getSpeed() / 3600;

        double kmThisTick  = kmPerSecond * deltaSeconds;
        //get distance to next node
        double edgeDistanceKm = getEdgeWeight(device);
        //how much is left
        double progressIncrement = kmThisTick / edgeDistanceKm;

        device.setProgressOnEdge(device.getProgressOnEdge()+progressIncrement);

        if(device.getProgressOnEdge() >= 1.0) {
            //change nodes
            device.setCurrentNodeId(device.getNextNodeId());
            device.setNextNodeId(chooseNextNode(device));
            device.setProgressOnEdge(0);

        }



        device.setFuelLevel(device.getFuelLevel()- device.getSpeed()*0.0001);
        if (device.getFuelLevel() <= 0) {
            device.setFuelLevel(0.0);
            device.setState(EngineOff);
            device.setSpeed(0.0);
        }


    }
    private Integer chooseNextNode(Device device){
        List<Integer> neighbors = graph.getNeighbors(device.getCurrentNodeId());
        if(neighbors==null||neighbors.isEmpty())
            return device.getCurrentNodeId();
        int randomIdx = device.getRandom().nextInt(neighbors.size());
        return neighbors.get(randomIdx);
    }

    private void ensureNextNode(Device device){
        if(device.getNextNodeId()==null){
            device.setNextNodeId(chooseNextNode(device));
        }
    }

    private double getEdgeWeight(Device device){

//handle next node ==null
//        if(device.getNextNodeId()==null)
//            return 0;

        //handle not neighbors
        int u = device.getCurrentNodeId();
        int v = device.getNextNodeId();
        return graph.getEdgeWeight(device.getCurrentNodeId(), device.getNextNodeId());


    }

}
