package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.StationTest;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class PathTest {

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

    @Test
    void find_shortest_path() {
        //given
        setUp();
        int expected = 2;
        Path path = Path.of(Arrays.asList(이호선, 일호선));

        //when
        PathResult pathResult = path.getShortestPath(StationTest.STATION_4, StationTest.STATION_5);

        //then
        assertThat(pathResult.getDistance()).isEqualTo(expected);
        assertThat(pathResult.getStations()).isEqualTo(Arrays.asList(StationTest.STATION_4, StationTest.STATION_5));
    }

}
