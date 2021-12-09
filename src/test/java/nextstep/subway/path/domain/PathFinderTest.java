package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PathFinderTest {

    @Test
    @DisplayName("최단 경로를 리턴한다.")
    public void findShortestPath() throws Exception {
        // given
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Station 판교역 = new Station("판교역");

        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<Station, DefaultWeightedEdge>(DefaultWeightedEdge.class);
        graph.addVertex(강남역);
        graph.addVertex(양재역);
        graph.addVertex(판교역);
        graph.setEdgeWeight(graph.addEdge(강남역, 양재역), 2);
        graph.setEdgeWeight(graph.addEdge(양재역, 판교역), 2);
        graph.setEdgeWeight(graph.addEdge(강남역, 판교역), 100);

        PathFinder pathFinder = new PathFinder(graph);

        // when
        List<Station> result = pathFinder.findShortestPath(판교역, 강남역);

        // then
        assertThat(result).containsExactly(판교역, 양재역, 강남역);
    }

    @Test
    @DisplayName("최단 경로 - 출발역과 도착역이 같다.")
    public void findShortestPath_same() throws Exception {
        // given
        Station 판교역 = new Station("판교역");
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<Station, DefaultWeightedEdge>(DefaultWeightedEdge.class);
        PathFinder pathFinder = new PathFinder(graph);

        // when, then
        assertThatThrownBy(() -> pathFinder.findShortestPath(판교역, 판교역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("출발역과 도착역이 같습니다.");
    }

    @Test
    @DisplayName("최단 경로 - 출발역과 도착역이 연결되어 있지 않다.")
    public void findShortestPath_not_connect() throws Exception {
        // given
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Station 판교역 = new Station("판교역");

        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<Station, DefaultWeightedEdge>(DefaultWeightedEdge.class);
        graph.addVertex(강남역);
        graph.addVertex(양재역);
        graph.addVertex(판교역);
        graph.setEdgeWeight(graph.addEdge(강남역, 양재역), 2);

        PathFinder pathFinder = new PathFinder(graph);

        // when, then
        assertThatThrownBy(() -> pathFinder.findShortestPath(판교역, 강남역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("출발역과 도착역이 연결되어 있지 않습니다.");
    }

    @Test
    @DisplayName("최단 경로의 총 거리를 리턴한다.")
    public void findShortestDistance() throws Exception {
        // given
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Station 판교역 = new Station("판교역");

        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<Station, DefaultWeightedEdge>(DefaultWeightedEdge.class);
        graph.addVertex(강남역);
        graph.addVertex(양재역);
        graph.addVertex(판교역);
        graph.setEdgeWeight(graph.addEdge(강남역, 양재역), 2);
        graph.setEdgeWeight(graph.addEdge(양재역, 판교역), 2);
        graph.setEdgeWeight(graph.addEdge(강남역, 판교역), 100);

        PathFinder pathFinder = new PathFinder(graph);

        // when
        int result = pathFinder.findShortestDistance(판교역, 강남역);

        // then
        assertThat(result).isEqualTo(4);
    }

    @Test
    @DisplayName("최단 경로의 총 거리 - 출발역과 도착역이 같다.")
    public void findShortestDistance_same() throws Exception {
        // given
        Station 판교역 = new Station("판교역");
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<Station, DefaultWeightedEdge>(DefaultWeightedEdge.class);
        PathFinder pathFinder = new PathFinder(graph);

        // when, then
        assertThatThrownBy(() -> pathFinder.findShortestDistance(판교역, 판교역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("출발역과 도착역이 같습니다.");
    }

    @Test
    @DisplayName("최단 경로의 총 거리 - 출발역과 도착역이 연결되어 있지 않다.")
    public void findShortestDistance_not_connect() throws Exception {
        // given
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Station 판교역 = new Station("판교역");

        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<Station, DefaultWeightedEdge>(DefaultWeightedEdge.class);
        graph.addVertex(강남역);
        graph.addVertex(양재역);
        graph.addVertex(판교역);
        graph.setEdgeWeight(graph.addEdge(강남역, 양재역), 2);

        PathFinder pathFinder = new PathFinder(graph);

        // when, then
        assertThatThrownBy(() -> pathFinder.findShortestDistance(판교역, 강남역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("출발역과 도착역이 연결되어 있지 않습니다.");
    }
}