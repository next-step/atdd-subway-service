package study.jgraph;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("그래프 탐색 라이브러리 테스트")
public class JgraphTest {

    @Test
    @DisplayName("다익스트라 알고리즘 최단거리")
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
        GraphPath path = dijkstraShortestPath.getPath(source, target);

        List<String> shortestPath = path.getVertexList();

        assertAll(
            () -> assertThat(shortestPath.size()).isEqualTo(3),
            () -> assertThat(path.getWeight()).isEqualTo(4),
            () -> assertThat(shortestPath).containsExactly("v3", "v2", "v1")
        );
    }

    @Test
    @DisplayName("그래프 최단거리")
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

        List<GraphPath> paths = new KShortestPaths(graph, 2).getPaths(source, target);

        assertThat(paths).hasSize(2);
        paths.stream()
                .forEach(it -> {
                    assertThat(it.getVertexList()).startsWith(source);
                    assertThat(it.getVertexList()).endsWith(target);
                });
    }
}
