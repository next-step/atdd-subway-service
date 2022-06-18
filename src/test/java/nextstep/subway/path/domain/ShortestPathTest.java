package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ShortestPathTest {
    private WeightedMultigraph<Station, DefaultWeightedEdge> graph;
    private ShortestPath shortestPath;

    @BeforeEach
    void setUp() {
        graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Station 남부터미널역 = new Station("남부터미널역");
        Station 교대역 = new Station("교대역");

        graph.addVertex(강남역);
        graph.addVertex(양재역);
        graph.addVertex(남부터미널역);
        graph.addVertex(교대역);

        graph.setEdgeWeight(graph.addEdge(강남역, 양재역), 3);
        graph.setEdgeWeight(graph.addEdge(양재역, 남부터미널역), 2);
        graph.setEdgeWeight(graph.addEdge(남부터미널역, 교대역), 4);
        graph.setEdgeWeight(graph.addEdge(교대역, 강남역), 10);

        shortestPath = new ShortestPath(graph, 강남역, 교대역);
    }

    @Test
    void 최단_경로를_조회한다() {
        // when
        List<Station> stations = shortestPath.getPath();

        // then
        assertThat(stations).hasSize(4);
    }

    @Test
    void 최단_경로_거리를_조회한다() {
        // when
        int distance = shortestPath.getDistance();

        // then
        assertThat(distance).isEqualTo(9);
    }
}
