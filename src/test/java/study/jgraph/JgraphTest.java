package study.jgraph;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.jgrapht.graph.builder.GraphBuilder;
import org.junit.jupiter.api.Test;

public class JgraphTest {
    @Test
    void getDijkstraShortestPath() {
        String source = "v3";
        String target = "v1";
        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addVertex("v3");
        graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
        graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
        graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        List<String> shortestPath = dijkstraShortestPath.getPath(source, target).getVertexList();

        assertThat(shortestPath.size()).isEqualTo(3);
    }

    @Test
    void getDijkstraShortestPathWithBuilder() {
        String source = "v3";
        String target = "v1";

        GraphBuilder<String, DefaultWeightedEdge, WeightedMultigraph<String, DefaultWeightedEdge>> graphBuilder =
            new GraphBuilder<>(new WeightedMultigraph<>(DefaultWeightedEdge.class));

        graphBuilder.addVertex("v1");
        graphBuilder.addVertex("v2");
        graphBuilder.addVertex("v3");
        graphBuilder.addEdge("v1", "v2", 2);
        graphBuilder.addEdge("v2", "v3", 2);
        graphBuilder.addEdge("v1", "v3", 100);

        Graph<String, DefaultWeightedEdge> graph = graphBuilder.buildAsUnmodifiable();

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        List<String> shortestPath = dijkstraShortestPath.getPath(source, target).getVertexList();

        assertThat(shortestPath.size()).isEqualTo(3);
    }
}
