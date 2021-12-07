package nextstep.subway.path.domain;

import nextstep.subway.exception.InputDataErrorCode;
import nextstep.subway.exception.InputDataErrorException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.assertj.core.api.Assertions;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Path 테스트")
class PathTest {

    @Test
    @DisplayName("시작역과 도착역이 같을때 예외처리")
    void sameStartAndEndStationTest(){
        Station dangsanStation = new Station("당산역");
        Station hapJeongStation = new Station("합정역");
        Station ehwaStation = new Station("이대역");

        Line line = new Line("2호선", "green", dangsanStation, ehwaStation, 10);

        Section section = new Section(line, dangsanStation, hapJeongStation, 3);
        line.addSection(section);

        Assertions.assertThatThrownBy(() -> {
                    new Path(dangsanStation, dangsanStation);
                }).isInstanceOf(InputDataErrorException.class)
                .hasMessageContaining(InputDataErrorCode.THERE_IS_SAME_STATIONS.errorMessage());
    }

    @Test
    @DisplayName("최단거리 외부라이브러리 테스트")
    public void getDijkstraShortestPath() {
        WeightedMultigraph<String, DefaultWeightedEdge> graph
                = new WeightedMultigraph(DefaultWeightedEdge.class);
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addVertex("v3");
        graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
        graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
        graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);

        DijkstraShortestPath dijkstraShortestPath
                = new DijkstraShortestPath(graph);
        List<String> shortestPath
                = dijkstraShortestPath.getPath("v3", "v1").getVertexList();

        assertThat(shortestPath.size()).isEqualTo(3);
    }

}
