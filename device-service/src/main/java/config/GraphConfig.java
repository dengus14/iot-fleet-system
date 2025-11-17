package config;

import model.UndirectedGraph;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GraphConfig {

    @Bean
    public UndirectedGraph undirectedGraph() {
        UndirectedGraph graph = new UndirectedGraph(7);
        graph.createEdges();
        return graph;
    }
}