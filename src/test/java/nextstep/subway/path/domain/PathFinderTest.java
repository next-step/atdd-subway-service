package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Lines;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.path.application.PathServiceTest.getLines;
import static nextstep.subway.path.application.PathServiceTest.getStation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("최단 경로 찾기")
class PathFinderTest {
    private Lines lines;
    private PathFinder pathFinder;
    private Station upStation;
    private Station downStation;

    @BeforeEach
    void setUp() {
        lines = new Lines(getLines());
        pathFinder = new PathFinder();
        upStation = getStation(1L, "강남역");
        downStation = getStation(4L, "교대역");
    }

    @Test
    void 최단_경로_목록을_반환한다() {
        // when
        GraphPath<Station, DefaultWeightedEdge> result = pathFinder.findShortestPath(lines, upStation, downStation);

        // then
        assertAll(
                () -> assertThat(result.getVertexList()).hasSize(4),
                () -> assertThat(result.getWeight()).isEqualTo(9)
        );
    }
    
    @Test
    void 출발역과_도착역이_같을_경우_최단경로를_구할_수_없다() {
        // when & then
        assertThatThrownBy(() ->
            pathFinder.findShortestPath(lines, upStation, upStation)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
