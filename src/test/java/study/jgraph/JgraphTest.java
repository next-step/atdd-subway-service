package study.jgraph;

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
import static org.junit.jupiter.api.Assertions.assertThrows;

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
     * --- *2호선* ---
     * 강남역 - 교대역 - 잠실역 - 사당역
     */
    @DisplayName("노선 하나의 지하철역에서 최단거리를 구한다.")
    @Test
    void singleLineShortestPath() {
        // given
        Station 강남역 = new Station("강남역");
        Station 교대역 = new Station("교대역");
        Station 잠실역 = new Station("잠실역");
        Station 사당역 = new Station("사당역");

        WeightedMultigraph<Station, DefaultEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        graph.addVertex(강남역);
        graph.addVertex(교대역);
        graph.addVertex(잠실역);
        graph.addVertex(사당역);
        graph.setEdgeWeight(graph.addEdge(강남역, 교대역), 10);
        graph.setEdgeWeight(graph.addEdge(교대역, 잠실역), 5);
        graph.setEdgeWeight(graph.addEdge(잠실역, 사당역), 3);

        DijkstraShortestPath<Station, DefaultEdge> dijkstraShortestPath = new DijkstraShortestPath(graph);
        GraphPath<Station, DefaultEdge> shortestPath = dijkstraShortestPath.getPath(교대역, 사당역);

        // then
        List<Station> paths = shortestPath.getVertexList();
        double weight = shortestPath.getWeight();
        assertThat(paths).containsExactly(교대역, 잠실역, 사당역);
        assertThat(weight).isEqualTo(8);
    }

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* --- 양재역
     */
    @DisplayName("노선 여러개의 지하철역에서 최단거리를 구한다.")
    @Test
    void multiLineShortestPath() {
        // given
        Station 교대역 = new Station("교대역");
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Station 남부터미널역 = new Station("남부터미널역");

        WeightedMultigraph<Station, DefaultEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        graph.addVertex(강남역);
        graph.addVertex(교대역);
        graph.addVertex(양재역);
        graph.addVertex(남부터미널역);
        graph.setEdgeWeight(graph.addEdge(교대역, 강남역), 10);
        graph.setEdgeWeight(graph.addEdge(강남역, 양재역), 10);
        graph.setEdgeWeight(graph.addEdge(남부터미널역, 양재역), 5);
        graph.setEdgeWeight(graph.addEdge(교대역, 남부터미널역), 3);

        DijkstraShortestPath<Station, DefaultEdge> dijkstraShortestPath = new DijkstraShortestPath(graph);

        // when
        GraphPath<Station, DefaultEdge> shortestPath = dijkstraShortestPath.getPath(강남역, 남부터미널역);

        // then
        List<Station> paths = shortestPath.getVertexList();
        double weight = shortestPath.getWeight();
        assertThat(paths).containsExactly(강남역, 교대역, 남부터미널역);
        assertThat(weight).isEqualTo(13);
    }

    @DisplayName("출발역과 도착역이 같은 경우를 확인한다.")
    @Test
    void sameStation() {
        // given
        Station 교대역 = new Station("교대역");
        Station 강남역 = new Station("강남역");

        WeightedMultigraph<Station, DefaultEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        graph.addVertex(강남역);
        graph.addVertex(교대역);
        graph.setEdgeWeight(graph.addEdge(교대역, 강남역), 10);

        DijkstraShortestPath<Station, DefaultEdge> dijkstraShortestPath = new DijkstraShortestPath(graph);

        // when
        GraphPath<Station, DefaultEdge> path = dijkstraShortestPath.getPath(교대역, 교대역);

        // then
        List<Station> stations = path.getVertexList();
        double weight = path.getWeight();
        int length = path.getLength();
        assertThat(stations).containsExactly(교대역);
        assertThat(weight).isZero();
        assertThat(length).isZero();
    }

    /**
     * 교대역    --- *2호선* ---   강남역
     * <p>
     * 남부터미널역  --- *3호선* --- 양재역
     */
    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우 Null 을 반환한다.")
    @Test
    void notConnectedStation() {
        Station 교대역 = new Station("교대역");
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Station 남부터미널역 = new Station("남부터미널역");

        WeightedMultigraph<Station, DefaultEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        graph.addVertex(강남역);
        graph.addVertex(교대역);
        graph.addVertex(양재역);
        graph.addVertex(남부터미널역);
        graph.setEdgeWeight(graph.addEdge(교대역, 강남역), 10);
        graph.setEdgeWeight(graph.addEdge(남부터미널역, 양재역), 5);

        DijkstraShortestPath<Station, DefaultEdge> dijkstraShortestPath = new DijkstraShortestPath(graph);

        // when
        GraphPath<Station, DefaultEdge> shortestPath = dijkstraShortestPath.getPath(강남역, 남부터미널역);

        // then
        assertThat(shortestPath).isNull();
    }

    /**
     * 남부터미널역  --- *3호선* --- 양재역
     */
    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우 예외처리 한다.")
    @Test
    void notExistedStation() {
        Station 교대역 = new Station("교대역");
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Station 남부터미널역 = new Station("남부터미널역");

        WeightedMultigraph<Station, DefaultEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        graph.addVertex(양재역);
        graph.addVertex(남부터미널역);
        graph.setEdgeWeight(graph.addEdge(남부터미널역, 양재역), 5);

        DijkstraShortestPath<Station, DefaultEdge> dijkstraShortestPath = new DijkstraShortestPath(graph);

        // when / then
        assertThrows(IllegalArgumentException.class, () -> dijkstraShortestPath.getPath(교대역, 강남역));
        assertThrows(IllegalArgumentException.class, () -> dijkstraShortestPath.getPath(교대역, 양재역));
        assertThrows(IllegalArgumentException.class, () -> dijkstraShortestPath.getPath(양재역, 강남역));
    }
}
