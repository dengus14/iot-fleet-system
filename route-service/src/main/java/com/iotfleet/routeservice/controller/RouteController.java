package com.iotfleet.routeservice.controller;


import com.iotfleet.routeservice.dto.EdgeDTO;
import com.iotfleet.routeservice.dto.GraphDTO;
import com.iotfleet.routeservice.dto.NodeDTO;
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

    @GetMapping("/graph")
    public ResponseEntity<GraphDTO> getGraph() {
        List<NodeDTO> nodes = List.of(
                NodeDTO.builder().id(0).name("Chicago").x(200.0).y(300.0).build(),
                NodeDTO.builder().id(1).name("Milwaukee").x(210.0).y(200.0).build(),
                NodeDTO.builder().id(2).name("Indianapolis").x(300.0).y(350.0).build(),
                NodeDTO.builder().id(3).name("Detroit").x(350.0).y(250.0).build(),
                NodeDTO.builder().id(4).name("Madison").x(150.0).y(180.0).build(),
                NodeDTO.builder().id(5).name("Minneapolis").x(100.0).y(100.0).build(),
                NodeDTO.builder().id(6).name("Columbus").x(380.0).y(320.0).build()
        );

        List<EdgeDTO> edges = List.of(
                EdgeDTO.builder().fromId(0).toId(1).distance(148.0).build(),
                EdgeDTO.builder().fromId(0).toId(2).distance(290.0).build(),
                EdgeDTO.builder().fromId(0).toId(3).distance(382.0).build(),
                EdgeDTO.builder().fromId(1).toId(4).distance(121.0).build(),
                EdgeDTO.builder().fromId(2).toId(5).distance(821.0).build(),
                EdgeDTO.builder().fromId(2).toId(6).distance(270.0).build(),
                EdgeDTO.builder().fromId(3).toId(6).distance(263.0).build(),
                EdgeDTO.builder().fromId(4).toId(5).distance(422.0).build()
        );

        GraphDTO graphDTO = GraphDTO.builder()
                .nodes(nodes)
                .edges(edges)
                .build();

        return ResponseEntity.ok(graphDTO);
    }
}
