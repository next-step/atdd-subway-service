package study.jgraph;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.fixture.StationFixture;
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
    @DisplayName("Dijkstra 최단거리 조회 테스트")
    @Test
    public void getDijkstraShortestPath() {
        String source = "v3";
        String target = "v1";
        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addVertex("v3");
        graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
        graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
        graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);

        DijkstraShortestPath<String, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        List<String> shortestPath = dijkstraShortestPath.getPath(source, target).getVertexList();

        assertThat(shortestPath.size()).isEqualTo(3);
    }

    @DisplayName("K 최단거리 조회 테스트")
    @Test
    public void getKShortestPaths() {
        String source = "v3";
        String target = "v1";

        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addVertex("v3");
        graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
        graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
        graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);

        List<GraphPath<String, DefaultWeightedEdge>> paths = new KShortestPaths<>(graph, 2).getPaths(source, target);

        assertThat(paths).hasSize(2);
        paths.forEach(it -> {
            assertThat(it.getVertexList()).startsWith(source);
            assertThat(it.getVertexList()).endsWith(target);
        });
    }

    @DisplayName("동일한 Station 을 추가하는 경우 테스트")
    @Test
    public void addVertexTest() {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        graph.addVertex(StationFixture.강남역);
        graph.addVertex(StationFixture.강남역);
        graph.addVertex(StationFixture.강남역);
        graph.addVertex(StationFixture.양재역);

        DefaultWeightedEdge edge = graph.addEdge(StationFixture.강남역, StationFixture.양재역);
        graph.setEdgeWeight(edge, 10);

        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(StationFixture.강남역, StationFixture.양재역);

        assertThat((int)path.getWeight()).isEqualTo(10);
    }

    @DisplayName("동일한 Edge 를 추가하는 경우 테스트")
    @Test
    public void addEdgeTest() {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        graph.addVertex(StationFixture.강남역);
        graph.addVertex(StationFixture.양재역);
        DefaultWeightedEdge edge = graph.addEdge(StationFixture.강남역, StationFixture.양재역);

        graph.setEdgeWeight(edge, 10);
        graph.setEdgeWeight(edge, 9);
        graph.setEdgeWeight(edge, 8);

        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(StationFixture.강남역, StationFixture.양재역);

        assertThat((int)path.getWeight()).isEqualTo(8);
    }
}
