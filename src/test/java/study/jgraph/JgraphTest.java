package study.jgraph;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.Test;

public class JgraphTest {
    @Test
    public void getDijkstraShortestPath() {
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
    public void getKShortestPaths() {
        String source = "v3";
        String target = "v1";

        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addVertex("v3");
        graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
        graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
        graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);

        List<GraphPath> paths = new KShortestPaths(graph, 100).getPaths(source, target);

        assertThat(paths).hasSize(2);
        paths.stream()
                .forEach(it -> {
                    assertThat(it.getVertexList()).startsWith(source);
                    assertThat(it.getVertexList()).endsWith(target);
                });
    }

    @Test
    void getDijkstraShortestPath_station() {
        String source = "교대역";
        String target = "양재역";

        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        graph.addVertex("교대역");
        graph.addVertex("양재역");
        graph.addVertex("강남역");
        graph.addVertex("남부터미널역");
        graph.setEdgeWeight(graph.addEdge("양재역", "강남역"), 8);
        graph.setEdgeWeight(graph.addEdge("교대역", "강남역"), 7);
        graph.setEdgeWeight(graph.addEdge("교대역", "남부터미널역"), 5);
        graph.setEdgeWeight(graph.addEdge("양재역", "남부터미널역"), 9);

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        List<String> shortestPath = dijkstraShortestPath.getPath(source, target).getVertexList();

        assertThat(shortestPath).containsExactly("교대역", "남부터미널역", "양재역");
    }

    @Test
    void getKShortestPaths_station() {
        String source = "교대역";
        String target = "양재역";

        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        graph.addVertex("교대역");
        graph.addVertex("양재역");
        graph.addVertex("강남역");
        graph.addVertex("남부터미널역");
        graph.setEdgeWeight(graph.addEdge("양재역", "강남역"), 8);
        graph.setEdgeWeight(graph.addEdge("교대역", "강남역"), 7);
        graph.setEdgeWeight(graph.addEdge("교대역", "남부터미널역"), 5);
        graph.setEdgeWeight(graph.addEdge("양재역", "남부터미널역"), 9);

        List<GraphPath> paths = new KShortestPaths(graph, 100).getPaths(source, target);

        assertThat(paths).hasSize(2);
        paths.stream()
                .forEach(it -> {
                    assertThat(it.getVertexList()).startsWith(source);
                    assertThat(it.getVertexList()).endsWith(target);
                });
    }
}
