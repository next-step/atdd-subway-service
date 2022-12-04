package nextstep.subway.path;

import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("최단 경로 라이브러리 테스트")
class JgraphtTest {

    @DisplayName("경로 조회 성공")
    @Test
    void findPath_station_success() {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        Station 삼전역 = Station.from("삼전역");
        Station 종합운동장역 = Station.from("종합운동장역");
        Station 잠실새내역 = Station.from("잠실새내역");
        graph.addVertex(삼전역);
        graph.addVertex(종합운동장역);
        graph.addVertex(잠실새내역);
        graph.setEdgeWeight(graph.addEdge(삼전역, 종합운동장역), 10);
        graph.setEdgeWeight(graph.addEdge(종합운동장역, 잠실새내역), 10);
        graph.setEdgeWeight(graph.addEdge(삼전역, 잠실새내역), 2);

        /* 삼전역 --10m--> 종합운동장역 --10m--> 잠실새내역 */
        /* 삼전역 -----------2m------------> 잠실새내역 */ // 최단 경로

        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        List<Station> 최단경로 = dijkstraShortestPath.getPath(삼전역, 잠실새내역).getVertexList();
        assertThat(최단경로).containsSequence(삼전역, 잠실새내역);
    }
}
