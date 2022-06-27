package nextstep.subway.path.domain;

import static nextstep.subway.path.LineFixtures.강남역;
import static nextstep.subway.path.LineFixtures.남부터미널역;
import static nextstep.subway.path.LineFixtures.모든_노선;
import static nextstep.subway.path.LineFixtures.서울역;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import nextstep.subway.line.domain.Distance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("경로 찾기")
class PathFinderTest {
    private PathFinder pathFinder;

    @BeforeEach
    void setUp() {
        pathFinder = new PathFinder(모든_노선);
    }

    @Test
    @DisplayName("주어진 노선 정보로 두 역의 최단 경로를 찾을 수 있다.")
    void 최단경로() {
        // When
        Path 최단경로 = pathFinder.findShortestPath(강남역, 남부터미널역);

        // Then
        assertAll(() -> assertThat(최단경로.getStations()).hasSize(3),
                () -> assertThat(최단경로.getDistance()).isEqualTo(Distance.from(12)));
    }

    @Test
    @DisplayName("출발역과 도착역이 동일하면 최단 경로를 찾을 수 없으며 예외를 발생시킨다.")
    void 출발역_도착역이_같은_경우_최단_경로_찾기() {
        assertThatThrownBy(() -> pathFinder.findShortestPath(강남역, 강남역)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우 예외를 발생시킨다.")
    void 출발역과_도착역이_연결이_되어_있지_않은_경우_최단_경로_찾기() {
        assertThatThrownBy(() -> pathFinder.findShortestPath(강남역, 서울역)).isInstanceOf(IllegalStateException.class);
    }
}
