package com.iotfleet.routeservice.controller;


import com.iotfleet.routeservice.model.UndirectedGraph;
import com.iotfleet.routeservice.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
@RequiredArgsConstructor
public class RouteController {

    private final RouteService routeService;
    private final UndirectedGraph graph;


    @GetMapping("/shortest-path")
    public ResponseEntity<List<Integer>> getShortestPath(
            @RequestParam Integer from,
            @RequestParam Integer to) {
        List<Integer> path = routeService.dijkstraShortestPathUpdated(from, to);
        if(path.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(path);

    }

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateRoute(@RequestBody List<Integer> route) {
        return ResponseEntity.ok(routeService.validateRoute(route));
    }


    @PostMapping("/execute")
    public ResponseEntity<String> executeRoute(
            @RequestParam Integer deviceNumber,
            @RequestParam Integer currentLocation,
            @RequestParam Integer destination) {
        List<Integer> path = routeService.dijkstraShortestPathUpdated(currentLocation, destination);
        if(path.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        String id = routeService.executeRoute(deviceNumber, path);
        return ResponseEntity.ok(id);
    }

    @GetMapping("/available-moves/{nodeId}")
    public ResponseEntity<List<Integer>> getAvailableMoves(@PathVariable Integer nodeId) {
        List<Integer> availableMoves = graph.getNeighbors(nodeId);
        if(availableMoves.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(availableMoves);
    }
}
