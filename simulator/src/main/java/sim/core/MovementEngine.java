package sim.core;

import sim.graph.UndirectedGraph;

import java.util.List;

public class MovementEngine {
    private final UndirectedGraph graph;
    public MovementEngine(UndirectedGraph graph){
        this.graph=graph;
    }
    public void update(Device device, double deltaSeconds){

        ensureNextNode(device);



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
