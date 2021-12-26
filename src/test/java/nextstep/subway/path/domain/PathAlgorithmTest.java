package nextstep.subway.path.domain;

import nextstep.subway.path.domain.fixture.SubwayGraphFixture;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.fixture.StationFixture;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PathAlgorithmTest {
    private SubwayGraph subwayGraph = SubwayGraphFixture.지하철_노선도;


    @DisplayName("Dijkstra 알고리즘 주입 테스트")
    @Test
    void Dijkstra_알고리즘_주입_테스트() {
        // when
        PathAlgorithm pathAlgorithm = PathAlgorithm.of(new DijkstraShortestPath<>(subwayGraph.getGraph()));

        // then
        GraphPath<Station, DefaultWeightedEdge> shortestPath = pathAlgorithm.findShortestPath(StationFixture.강남역, StationFixture.양재역);

        // given
        assertThat(shortestPath.getWeight()).isEqualTo(2);
    }
}
