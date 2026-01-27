package com.iotfleet.deviceservice.model;

import java.util.*;


public class UndirectedGraph {


    // 0: Chicago - connects to Milwaukee, Indianapolis, Detroit
    List<Integer> chicagoRefs = new ArrayList<>(Arrays.asList(1, 2, 3));

    // 1: Milwaukee - connects to Chicago, Madison
    List<Integer> milwaukeeRefs = new ArrayList<>(Arrays.asList(0, 4));

    // 2: Indianapolis - connects to Chicago, Cincinnati, Columbus
    List<Integer> indianapolisRefs = new ArrayList<>(Arrays.asList(0, 5, 6));

    // 3: Detroit - connects to Chicago, Columbus
    List<Integer> detroitRefs = new ArrayList<>(Arrays.asList(0, 6));

    // 4: Madison - connects to Milwaukee, Minneapolis
    List<Integer> madisonRefs = new ArrayList<>(Arrays.asList(1, 5));

    // 5: Minneapolis - connects to Madison, Cincinnati
    List<Integer> minneapolisRefs = new ArrayList<>(Arrays.asList(4, 2));

    // 6: Columbus - connects to Indianapolis, Detroit
    List<Integer> columbusRefs = new ArrayList<>(Arrays.asList(2, 3));

    List<UndirectedNode> nodes = new ArrayList<>(Arrays.asList(
            new UndirectedNode(0, "Chicago", chicagoRefs, "Major transportation hub in Illinois"),
            new UndirectedNode(1, "Milwaukee", milwaukeeRefs, "Largest city in Wisconsin"),
            new UndirectedNode(2, "Indianapolis", indianapolisRefs, "Capital of Indiana"),
            new UndirectedNode(3, "Detroit", detroitRefs, "Motor City in Michigan"),
            new UndirectedNode(4, "Madison", madisonRefs, "Capital of Wisconsin"),
            new UndirectedNode(5, "Minneapolis", minneapolisRefs, "Twin Cities in Minnesota"),
            new UndirectedNode(6, "Columbus", columbusRefs, "Capital of Ohio")
    ));


    Map<Integer, UndirectedNode> allNodes;
    Map<Integer, Map<Integer, Double>> edgeWeights;

    private Random random = new Random();
    private List<List<Integer>> adjList = new ArrayList<>();

    public UndirectedGraph(Integer V) {
        allNodes = new HashMap<>();
        adjList = new ArrayList<>(V);
        edgeWeights = new HashMap<>();
        for(int i = 0; i < V; i++){
            allNodes.put(i, nodes.get(i));
            adjList.add(new ArrayList<>());
        }
    }

    public void createEdges(){
        addWeightedEdge(0, 1, 148.0);  // Chicago - Milwaukee
        addWeightedEdge(0, 2, 290.0);  // Chicago - Indianapolis
        addWeightedEdge(0, 3, 382.0);  // Chicago - Detroit
        addWeightedEdge(1, 4, 121.0);  // Milwaukee - Madison
        addWeightedEdge(2, 5, 821.0);  // Indianapolis - Minneapolis
        addWeightedEdge(2, 6, 270.0);  // Indianapolis - Columbus
        addWeightedEdge(3, 6, 263.0);  // Detroit - Columbus
        addWeightedEdge(4, 5, 422.0);  // Madison - Minneapolis
    }

    public List<Integer> getNeighbors(Integer u) {
        if(isValidNode(u)){
            return adjList.get(u);
        }
        return null;
    }

    /**
     * @param u - first node ID
     * @param v - second node ID
     * @return true if they share an edge, false otherwise
     */
    public boolean areNeighbors(Integer u, Integer v) {
        if(!isValidNode(u) || !isValidNode(v)){
            return false;
        }
        if(adjList.get(u).contains(v)){
            return true;
        }
        return false;
    }

    public Double getRandomDistance(int min, int max){
        int randomNumber = random.nextInt(max + 1 - min) + min;
        return Double.valueOf(randomNumber);
    }


    /**
     * @param u - first node
     * @param v - second node
     * @param weight - distance/weight of the edge
     */
    public void addWeightedEdge(Integer u, Integer v, Double weight) {
        if(adjList.get(u).contains(v)){
            return;  // Edge already exists, skip everything
        }

        adjList.get(u).add(v);
        adjList.get(v).add(u);

        edgeWeights.putIfAbsent(u, new HashMap<>());
        edgeWeights.get(u).put(v, weight);
        edgeWeights.putIfAbsent(v, new HashMap<>());
        edgeWeights.get(v).put(u, weight);
    }



    /**
     * @param u - first node
     * @param v - second node
     * @return the weight, or null if not neighbors
     */
    public Double getEdgeWeight(Integer u, Integer v) {
        if(edgeWeights.containsKey(u)){
            if(edgeWeights.get(u).containsKey(v)){
                return edgeWeights.get(u).get(v);
            }
        }
        return null;
    }

    public boolean isValidNode(Integer nodeId) {
        return allNodes.containsKey(nodeId);
    }

    public UndirectedNode getNode(Integer id) {
        return allNodes.get(id);
    }

    public void printGraph() {
        for (int i = 0; i < adjList.size(); i++) {
            System.out.print("Node " + i + ": ");
            for (int j : adjList.get(i)) {
                System.out.print(j + " ");
            }
            System.out.println();
        }
    }




}
