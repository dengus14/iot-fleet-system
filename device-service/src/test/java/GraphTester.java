import model.UndirectedGraph;
import org.junit.jupiter.api.Test;

public class GraphTester {

    @Test
    public void testGraphCreation() {
        UndirectedGraph graph = new UndirectedGraph(5);
        graph.createEdges(null);

        // Add assertions here
        System.out.println("Testing graph:");
        graph.printGraph();
    }
}