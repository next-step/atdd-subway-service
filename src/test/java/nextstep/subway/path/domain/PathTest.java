package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.StationTest;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class PathTest {

    private static final String NOT_CONNECT_EXCEPTION = "출발역과 도착역이 연결되어 있지 않습니다.";
    private static final String STATION_NOT_EXIST_IN_THE_LINES_EXCEPTION = "존재하는 노선안에 해당 역이 존재하지 않습니다.";
    private static final String SAME_STATIONS_EXCEPTION = "출발역과 도착역이 같아 경로를 찾을 수 없습니다.";

    private Line 이호선;
    private Line 일호선;

    void setUp() {
        이호선 = new Line("이호선", "green", StationTest.STATION_4, StationTest.STATION_5, 10);
        이호선.addStation(StationTest.STATION_3, StationTest.STATION_5, 4);
        일호선 = new Line("일호선", "navy", StationTest.STATION_4, StationTest.STATION_5, 2);
    }

    @Test
    void getDijkstraShortestPath() {
        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addVertex("v3");
        graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
        graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
        graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        List<String> shortestPath = dijkstraShortestPath.getPath("v3", "v1").getVertexList();
        assertThat(shortestPath.size()).isEqualTo(3);
    }

    @DisplayName("경로 정보를 담은 path를 생성한다.")
    @Test
    void create_path() {
        //given
        setUp();

        //when
        Path path = Path.of(Arrays.asList(이호선, 일호선));

        //then
        Arrays.asList(StationTest.STATION_3, StationTest.STATION_4, StationTest.STATION_5).stream()
            .forEach(station -> assertThat(path.containsVertex(station)).isTrue());
        assertThat(path.containsEdge(StationTest.STATION_3, StationTest.STATION_5)).isTrue();
    }

    @DisplayName("경로를 이용하여 최단 경로를 찾는다.")
    @Test
    void find_shortest_path() {
        //given
        setUp();
        int expected = 2;
        Path path = Path.of(Arrays.asList(이호선, 일호선));

        //when
        PathResult pathResult = path.getShortestPath(new LoginMember(), StationTest.STATION_4, StationTest.STATION_5);

        //then
        assertThat(pathResult.getDistance()).isEqualTo(expected);
        assertThat(pathResult.getStations()).isEqualTo(Arrays.asList(StationTest.STATION_4, StationTest.STATION_5));
    }

    @DisplayName("출발역과 도착역이 같으면 최단경로를 조회할 수 없다.")
    @Test
    public void find_shortest_path_with_same_stations_is_invalid() {
        setUp();
        Path path = Path.of(Arrays.asList(이호선, 일호선));

        assertThatThrownBy(() -> path.getShortestPath(new LoginMember(), StationTest.STATION_4, StationTest.STATION_4))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(SAME_STATIONS_EXCEPTION);
    }


    @DisplayName("출발역과 도착역이 모든 노선안에 존재하지 않으면 최단경로를 조회할 수 없다.")
    @Test
    void station_not_exist_in_the_lines() {
        setUp();
        Path path = Path.of(Arrays.asList(이호선, 일호선));

        assertThatThrownBy(() -> path.getShortestPath(new LoginMember(), StationTest.STATION_1, StationTest.STATION_5))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(STATION_NOT_EXIST_IN_THE_LINES_EXCEPTION);
    }

    @DisplayName("출발역에서 도착역으로 향하는 경로를 찾지 못한 경우 예외를 발생시킨다.")
    @Test
    void cannot_find_shortest_path() {
        setUp();
        Line 삼호선 = new Line("삼호선", "blue", StationTest.STATION_1, StationTest.STATION_2, 8);
        Path path = Path.of(Arrays.asList(이호선, 일호선, 삼호선));

        assertThatThrownBy(() -> path.getShortestPath(new LoginMember(), StationTest.STATION_1, StationTest.STATION_5))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(NOT_CONNECT_EXCEPTION);
    }

}
