package com.iotfleet.deviceservice.config;

import lombok.extern.slf4j.Slf4j;
import com.iotfleet.deviceservice.model.UndirectedGraph;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class GraphConfig {

    @Bean
    public UndirectedGraph undirectedGraph() {
        log.info("Initializing UndirectedGraph bean...");
        UndirectedGraph graph = new UndirectedGraph(7);
        graph.createEdges();
        log.info("UndirectedGraph bean created successfully with 7 cities");
        return graph;
    }
}