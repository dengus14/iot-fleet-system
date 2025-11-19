package com.iotfleet.routeservice.service;

import com.iotfleet.routeservice.dto.RouteCommandDTO;
import com.iotfleet.routeservice.kafka.RouteCommandProducer;
import com.iotfleet.routeservice.model.UndirectedGraph;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class RouteService {
    private final RouteCommandProducer routeCommandProducer;
    private final UndirectedGraph undirectedGraph;


    public boolean validateRoute(List<Integer> route){
        for(int i = 0; i < route.size() - 1; i++){
            if(!undirectedGraph.areNeighbors(route.get(i), route.get(i+1))){
                return false;
            }
        }
        return true;
    }

    public List<Integer> dijkstraShortestPathUpdated(Integer fromNode, Integer toNode){
        Map<Integer,Double> distances = new HashMap<>();
        distances.put(fromNode, 0.0);
        Map<Integer,Integer> previousNode = new HashMap<>();
        Set<Integer> unvisited = new HashSet<>();

        for(Integer i = 0; i < 7; i++){
            unvisited.add(Integer.valueOf(i));
        }
        for (Integer nodeId : unvisited) {
            if (!nodeId.equals(fromNode)) {
                distances.put(nodeId, Double.MAX_VALUE);
            }
        }

        while(!unvisited.isEmpty()){
            Integer smallestCurrentNode = findMinDistanceNode(distances, unvisited);
            if(smallestCurrentNode == toNode){
                break;
            }

            List<Integer> neighbours = undirectedGraph.getNeighbors(smallestCurrentNode);
            for(Integer neighborId : neighbours){
                if(!unvisited.contains(neighborId)){
                    continue;
                }
                Double edgeWeight = undirectedGraph.getEdgeWeight(smallestCurrentNode, neighborId);
                Double newDistance = distances.get(smallestCurrentNode) + edgeWeight;
                if(newDistance < distances.get(neighborId)){
                    distances.put(neighborId, newDistance);
                    previousNode.put(neighborId, smallestCurrentNode);
                }

            }
            unvisited.remove(smallestCurrentNode);
        }
        if(!previousNode.containsKey(toNode) && !toNode.equals(fromNode)){
            return Collections.emptyList();
        }
        List<Integer> path = new ArrayList<>();
        Integer current = toNode;
        while(current != null){
            path.add(current);
            current = previousNode.get(current);
        }
        Collections.reverse(path);
        return path;
    }

    public Integer findMinDistanceNode(Map<Integer, Double> distances, Set<Integer> unvisited){
        Double minDistance = Double.MAX_VALUE;
        Integer minNodeId = null;
        for(Integer nodeId : unvisited){
            Double distance = distances.get(nodeId);
            if(distance < minDistance){
                minDistance = distance;
                minNodeId = nodeId;
            }
        }
        return minNodeId;
    }

    public String executeRoute(Integer deviceNumber, List<Integer> route){
        if(!validateRoute(route)){
            throw new IllegalArgumentException("Route validation failed: consecutive nodes are not neighbors");
        }
        String uniqueId = UUID.randomUUID().toString();
        RouteCommandDTO routeCommandDTO = RouteCommandDTO.builder()
                .commandId(uniqueId)
                .deviceNumber(deviceNumber)
                .plannedRoute(route)
                .commandType("EXECUTE_ROUTE")
                .timestamp(System.currentTimeMillis())
                .build();
        routeCommandProducer.sendRouteCommand(routeCommandDTO);
        return uniqueId;
    }
}
