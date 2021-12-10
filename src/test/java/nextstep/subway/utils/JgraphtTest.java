package nextstep.subway.utils;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("jgrapht 라이브러리 사용법 테스트")
public class JgraphtTest {

    @Test
    void jgraphtTest() {
        WeightedMultigraph<String, DefaultWeightedEdge> graph
                = new WeightedMultigraph(DefaultWeightedEdge.class);

        graph.addVertex("남부터미널역");
        graph.addVertex("양재역");
        graph.addVertex("강남역");
        graph.addVertex("교대역");
        graph.setEdgeWeight(graph.addEdge("교대역", "남부터미널역"), 3);
        graph.setEdgeWeight(graph.addEdge("강남역", "양재역"), 10);
        graph.setEdgeWeight(graph.addEdge("교대역", "강남역"), 100);
        graph.setEdgeWeight(graph.addEdge("교대역", "양재역"), 5);

        DijkstraShortestPath dijkstraShortestPath
                = new DijkstraShortestPath(graph);
        GraphPath path = dijkstraShortestPath.getPath("남부터미널역", "강남역");

        org.junit.jupiter.api.Assertions.assertAll(
                () -> assertThat(path.getVertexList()).containsAll(Arrays.asList("남부터미널역", "교대역", "양재역", "강남역")),
                () -> assertThat(path.getWeight()).isEqualTo(18)
        );
    }
}
