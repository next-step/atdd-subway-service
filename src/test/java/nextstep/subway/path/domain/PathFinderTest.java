package nextstep.subway.path.domain;

import static nextstep.subway.path.LineFixtures.강남역;
import static nextstep.subway.path.LineFixtures.남부터미널역;
import static nextstep.subway.path.LineFixtures.모든_노선;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("경로 찾기")
class PathFinderTest {
    private List<Line> lines;

    @BeforeEach
    void setUp() {
        lines = 모든_노선;
    }

    @Test
    @DisplayName("주어진 노선 정보로 두 역의 최단 경로를 찾을 수 있다.")
    void 최단경로() {
        // When
        PathFinder pathFinder = new PathFinder(lines);
        Path 최단경로= pathFinder.findShortestPath(강남역, 남부터미널역);

        // Then
        assertAll(() -> assertThat(최단경로.getStations()).hasSize(3),
                () -> assertThat(최단경로.getDistance()).isEqualTo(Distance.from(12)));
    }
}
