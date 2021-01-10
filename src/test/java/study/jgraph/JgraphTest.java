package study.jgraph;

import nextstep.subway.station.StationFixtures;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

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

    /**
     *      거리 10     거리 5     거리 3
     * 천호역 ---- 잠실역 ---- 문정역 ---- 산성역
     */
    @DisplayName("하나의 노선에서 지하철역 최단거리")
    @Test
    void findShortestPathInSingleLine() {
        // given
        WeightedMultigraph<Station, DefaultEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        graph.addVertex(StationFixtures.천호역);
        graph.addVertex(StationFixtures.잠실역);
        graph.addVertex(StationFixtures.문정역);
        graph.addVertex(StationFixtures.산성역);

        graph.setEdgeWeight(graph.addEdge(StationFixtures.천호역, StationFixtures.잠실역), 10);
        graph.setEdgeWeight(graph.addEdge(StationFixtures.잠실역, StationFixtures.문정역), 5);
        graph.setEdgeWeight(graph.addEdge(StationFixtures.문정역, StationFixtures.산성역), 3);

        DijkstraShortestPath<Station, DefaultEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

        // when
        GraphPath<Station, DefaultEdge> shortestPath = dijkstraShortestPath.getPath(StationFixtures.잠실역, StationFixtures.산성역);

        // then
        List<Station> paths = shortestPath.getVertexList();
        double weight = shortestPath.getWeight();
        assertThat(paths).containsExactly(StationFixtures.잠실역, StationFixtures.문정역, StationFixtures.산성역);
        assertThat(weight).isEqualTo(8);
    }

    /**
     *              거리 10
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * 거리 3                     거리 10
     * |                        |
     * 남부터미널역  --- *3호선* --- 양재역
     *                거리 5
     */
    @DisplayName("여러개 노선에서 지하철역 최단거리")
    @Test
    void findShortestPathInMultiLine() {
        // given
        WeightedMultigraph<Station, DefaultEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        graph.addVertex(StationFixtures.강남역);
        graph.addVertex(StationFixtures.교대역);
        graph.addVertex(StationFixtures.양재역);
        graph.addVertex(StationFixtures.남부터미널역);

        graph.setEdgeWeight(graph.addEdge(StationFixtures.교대역, StationFixtures.강남역), 10);
        graph.setEdgeWeight(graph.addEdge(StationFixtures.강남역, StationFixtures.양재역), 10);
        graph.setEdgeWeight(graph.addEdge(StationFixtures.교대역, StationFixtures.남부터미널역), 3);
        graph.setEdgeWeight(graph.addEdge(StationFixtures.남부터미널역, StationFixtures.양재역), 5);

        DijkstraShortestPath<Station, DefaultEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

        // when
        GraphPath<Station, DefaultEdge> shortestPath = dijkstraShortestPath.getPath(StationFixtures.강남역, StationFixtures.남부터미널역);

        // then
        List<Station> paths = shortestPath.getVertexList();
        double weight = shortestPath.getWeight();
        assertThat(paths).containsExactly(StationFixtures.강남역, StationFixtures.교대역, StationFixtures.남부터미널역);
        assertThat(weight).isEqualTo(13);
    }

    @DisplayName("출발역과 도착역이 같은 경우")
    @Test
    void findSameStations() {
        // given
        WeightedMultigraph<Station, DefaultEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        graph.addVertex(StationFixtures.천호역);
        graph.addVertex(StationFixtures.잠실역);

        DijkstraShortestPath<Station, DefaultEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

        // when
        GraphPath<Station, DefaultEdge> path = dijkstraShortestPath.getPath(StationFixtures.잠실역, StationFixtures.잠실역);

        // then
        List<Station> stations = path.getVertexList();
        double weight = path.getWeight();
        int length = path.getLength();
        assertThat(stations).containsExactly(StationFixtures.잠실역);
        assertThat(weight).isZero();
        assertThat(length).isZero();
    }

    /**             거리 10
     * 교대역    --- *2호선* ---   강남역
     *
     * 남부터미널역  --- *3호선* --- 양재역
     *                 거리 5
     */
    @DisplayName("출발역과 도착역이 연결되어 있지 않은 경우")
    @Test
    void findNotConnectStations() {
        // given
        WeightedMultigraph<Station, DefaultEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        graph.addVertex(StationFixtures.강남역);
        graph.addVertex(StationFixtures.교대역);
        graph.addVertex(StationFixtures.남부터미널역);
        graph.addVertex(StationFixtures.양재역);

        graph.setEdgeWeight(graph.addEdge(StationFixtures.강남역, StationFixtures.교대역), 10);
        graph.setEdgeWeight(graph.addEdge(StationFixtures.남부터미널역, StationFixtures.양재역), 5);

        DijkstraShortestPath<Station, DefaultEdge> dijkstraShortestPath = new DijkstraShortestPath(graph);

        // when
        GraphPath<Station, DefaultEdge> shortestPath = dijkstraShortestPath.getPath(StationFixtures.강남역, StationFixtures.남부터미널역);

        // then
        assertThat(shortestPath).isNull();
    }

    /**            거리 10
     * 교대역  --- *3호선* --- 강남역
     */
    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우")
    @Test
    void findNotExistStations() {
        // given
        WeightedMultigraph<Station, DefaultEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        graph.addVertex(StationFixtures.교대역);
        graph.addVertex(StationFixtures.강남역);
        graph.setEdgeWeight(graph.addEdge(StationFixtures.교대역, StationFixtures.강남역), 10);

        DijkstraShortestPath<Station, DefaultEdge> dijkstraShortestPath = new DijkstraShortestPath(graph);

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            dijkstraShortestPath.getPath(StationFixtures.강남역, StationFixtures.양재역);
            dijkstraShortestPath.getPath(StationFixtures.교대역, StationFixtures.남부터미널역);
            dijkstraShortestPath.getPath(StationFixtures.남부터미널역, StationFixtures.양재역);
        });
    }

}
