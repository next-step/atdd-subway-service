package study.jgraph;

import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JgraphTest {
    @Test
    @DisplayName("지하철역 최단거리 Jraph 테스트")
    public void station_short_path() {
        // given
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        final Station start = new Station("강남역");
        final Station destination = new Station("신림역");

        graph.addVertex(new Station("강남역"));
        graph.addVertex(new Station("이촌역"));
        graph.addVertex(new Station("사당역"));
        graph.addVertex(new Station("잠실역"));
        graph.addVertex(new Station("신림역"));
        graph.addVertex(new Station("봉천역"));

        graph.setEdgeWeight(graph.addEdge(new Station("강남역"), new Station("사당역")), 100);
        graph.setEdgeWeight(graph.addEdge(new Station("사당역"), new Station("잠실역")), 5);
        graph.setEdgeWeight(graph.addEdge(new Station("잠실역"), new Station("신림역")), 3);
        graph.setEdgeWeight(graph.addEdge(new Station("신림역"), new Station("봉천역")), 8);
        graph.setEdgeWeight(graph.addEdge(new Station("강남역"), new Station("이촌역")), 8);
        graph.setEdgeWeight(graph.addEdge(new Station("이촌역"), new Station("신림역")), 33);
        // when
        final GraphPath graphPath = new DijkstraShortestPath(graph).getPath(start, destination);
        // then
        assertThat(graphPath.getVertexList()).hasSize(3);
        assertThat(graphPath.getWeight()).isEqualTo(41.0);
    }

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
}
