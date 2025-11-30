//import com.iotfleet.deviceservice.model.UndirectedGraph;
//import com.iotfleet.deviceservice.model.UndirectedNode;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class UndirectedGraphTest {
//
//    private UndirectedGraph graph;
//
//    @BeforeEach
//    public void setUp() {
//        graph = new UndirectedGraph(7);
//        graph.createEdges();
//    }
//
//    @Test
//    public void testGraphCreation() {
//        System.out.println("=== Test: Graph Creation ===");
//        assertNotNull(graph);
//        graph.printGraph();
//    }
//
//    @Test
//    public void testGetNeighbors() {
//        System.out.println("\n=== Test: Get Neighbors ===");
//
//        // Spain (0) should have neighbors: Russia(1), USA(2), Mexico(3)
//        List<Integer> neighbors = graph.getNeighbors(0);
//        assertNotNull(neighbors);
//        System.out.println("Spain's neighbors: " + neighbors);
//        assertTrue(neighbors.contains(1)); // Russia
//        assertTrue(neighbors.contains(2)); // USA
//        assertTrue(neighbors.contains(3)); // Mexico
//
//        // Russia (1) should have neighbor: Spain(0)
//        List<Integer> russiaNeighbors = graph.getNeighbors(1);
//        System.out.println("Russia's neighbors: " + russiaNeighbors);
//        assertTrue(russiaNeighbors.contains(0));
//    }
//
//    @Test
//    public void testAreNeighbors() {
//        System.out.println("\n=== Test: Are Neighbors ===");
//
//        // Spain (0) and Russia (1) are neighbors
//        assertTrue(graph.areNeighbors(0, 1));
//        System.out.println("Spain and Russia are neighbors: true");
//
//        // Russia (1) and Mexico (3) are NOT neighbors
//        assertFalse(graph.areNeighbors(1, 3));
//        System.out.println("Russia and Mexico are neighbors: false");
//    }
//
//    @Test
//    public void testEdgeWeights() {
//        System.out.println("\n=== Test: Edge Weights ===");
//
//        // Get weight between Spain (0) and Russia (1)
//        Double weight01 = graph.getEdgeWeight(0, 1);
//        assertNotNull(weight01);
//        assertTrue(weight01 >= 30 && weight01 <= 600);
//        System.out.println("Distance Spain -> Russia: " + weight01);
//
//        // Get weight between Spain (0) and USA (2)
//        Double weight02 = graph.getEdgeWeight(0, 2);
//        assertNotNull(weight02);
//        assertTrue(weight02 >= 30 && weight02 <= 600);
//        System.out.println("Distance Spain -> USA: " + weight02);
//
//        // Verify bidirectional weights are the same
//        Double weight10 = graph.getEdgeWeight(1, 0);
//        assertEquals(weight01, weight10);
//        System.out.println("Distance Russia -> Spain: " + weight10 + " (should match)");
//
//        // Non-neighbors should return null
//        Double weightNonNeighbor = graph.getEdgeWeight(1, 3);
//        assertNull(weightNonNeighbor);
//        System.out.println("Distance Russia -> Mexico (not neighbors): " + weightNonNeighbor);
//    }
//
////    @Test
////    public void testIsValidNode() {
////        System.out.println("\n=== Test: Valid Node ===");
////
////        // Valid nodes
////        assertTrue(graph.isValidNode(0));
////        assertTrue(graph.isValidNode(4));
////        System.out.println("Node 0 is valid: true");
////        System.out.println("Node 4 is valid: true");
////
////        // Invalid nodes
////        assertFalse(graph.isValidNode(5));
////        assertFalse(graph.isValidNode(-1));
////        System.out.println("Node 5 is valid: false");
////        System.out.println("Node -1 is valid: false");
////    }
//
////    @Test
////    public void testGetNode() {
////        System.out.println("\n=== Test: Get Node ===");
////
////        UndirectedNode spain = graph.getNode(0);
////        assertNotNull(spain);
////        assertEquals("Spain", spain.getLocationName());
////        assertEquals(0, spain.getId());
////        System.out.println("Node 0: " + spain.getLocationName());
////
////        UndirectedNode canada = graph.getNode(4);
////        assertNotNull(canada);
////        assertEquals("Canada", canada.getLocationName());
////        System.out.println("Node 4: " + canada.getLocationName());
////    }
//
//    @Test
//    public void testAllEdgesHaveWeights() {
//        System.out.println("\n=== Test: All Edges Have Weights ===");
//
//        for (int i = 0; i < 5; i++) {
//            List<Integer> neighbors = graph.getNeighbors(i);
//            UndirectedNode node = graph.getNode(i);
//            System.out.println("\n" + node.getLocationName() + " connections:");
//
//            for (Integer neighbor : neighbors) {
//                Double weight = graph.getEdgeWeight(i, neighbor);
//                assertNotNull(weight, "Edge " + i + "->" + neighbor + " should have a weight");
//                assertTrue(weight >= 30 && weight <= 600, "Weight should be between 30 and 600");
//
//                UndirectedNode neighborNode = graph.getNode(neighbor);
//                System.out.println("  -> " + neighborNode.getLocationName() + ": " + weight + " units");
//            }
//        }
//    }
//
//    @Test
//    public void testNoDuplicateEdges() {
//        System.out.println("\n=== Test: No Duplicate Edges ===");
//
//        for (int i = 0; i < 5; i++) {
//            List<Integer> neighbors = graph.getNeighbors(i);
//            long uniqueCount = neighbors.stream().distinct().count();
//            assertEquals(neighbors.size(), uniqueCount,
//                    "Node " + i + " should not have duplicate neighbors");
//        }
//        System.out.println("No duplicate edges found!");
//    }
//
//    @Test
//    public void testRandomDistanceRange() {
//        System.out.println("\n=== Test: Random Distance Range ===");
//
//        // Test that getRandomDistance generates values in range
//        for (int i = 0; i < 10; i++) {
//            Double distance = graph.getRandomDistance(30, 600);
//            assertTrue(distance >= 30 && distance <= 600,
//                    "Distance should be between 30 and 600, got: " + distance);
//            System.out.println("Random distance " + (i+1) + ": " + distance);
//        }
//    }
//}