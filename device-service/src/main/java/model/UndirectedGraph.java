package model;

import java.util.*;


public class UndirectedGraph {

    List<Integer> loc1Refs = new ArrayList<>(Arrays.asList(1, 2, 3));
    List<Integer> loc2Refs = new ArrayList<>(Arrays.asList(0));
    List<Integer> loc3Refs = new ArrayList<>(Arrays.asList(0, 1));
    List<Integer> loc4Refs = new ArrayList<>(Arrays.asList(2, 4));
    List<Integer> loc5Refs = new ArrayList<>(Arrays.asList(0, 2, 3));
    List<UndirectedNode> nodes = new ArrayList<>(Arrays.asList(new UndirectedNode(0,"Spain", loc1Refs, "Cool Country00"),
    new UndirectedNode(1,"Russia", loc2Refs, "Cool Country0"),
    new UndirectedNode(2,"USA", loc3Refs, "Cool Country1"),
    new UndirectedNode(3,"Mexico", loc4Refs, "Cool Country2"),
    new UndirectedNode(4,"Canada", loc5Refs, "Cool Country3")));


    Map<Integer, UndirectedNode> allNodes;
    Map<Integer, Map<Integer, Double>> edgeWeights;

    private Random random = new Random();
    private List<List<Integer>> adjList = new ArrayList<>();

    public UndirectedGraph(Integer V) {
        allNodes = new HashMap<>();
        adjList = new ArrayList<>(V);
        edgeWeights = new HashMap<>();
        for(int i = 0; i < nodes.size(); i++){
            allNodes.put(i, nodes.get(i));
            adjList.add(new ArrayList<>());
        }
    }

    public void createEdges(){
        for(UndirectedNode node: nodes){
            for(Integer id: node.getNeighbors()){
                Double weight = getRandomDistance(30,600);
                addWeightedEdge(node.getId(), id, weight);
            }
        }
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
     * TODO: Add a weighted edge between two nodes
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
     * TODO: Get the distance/weight between two neighboring nodes
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
