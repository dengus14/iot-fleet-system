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
    Map<Integer, List<Integer>> allNeighbors;


    private List<List<Integer>> adjList = new ArrayList<>();

    public UndirectedGraph(Integer V) {
        allNodes = new HashMap<>();
        adjList = new ArrayList<>(V);
        for(int i = 0; i < nodes.size(); i++){
            allNodes.put(i, nodes.get(i));
            adjList.add(new ArrayList<>());
        }
    }


    public void addEdge(Integer u, Integer v) {
        if(adjList.get(u).contains(v)){
            return;
        }
        adjList.get(u).add(v);
        adjList.get(v).add(u);
    }

    public void createEdges(List<List<Integer>> adjList){
        for(UndirectedNode node: nodes){
            for(Integer id: node.getNeighbors()){
                addEdge(node.getId(), id);
            }
        }
    }

    public List<Integer> getNeighbors(int u) {
        return adjList.get(u);
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
