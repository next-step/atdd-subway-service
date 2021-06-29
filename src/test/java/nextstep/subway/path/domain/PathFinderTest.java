package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PathFinderTest {

    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 역삼역;
    private Station 남부터미널역;
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private PathFinder pathFinder;
    private Station 계양역;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        역삼역 = new Station("역삼역");
        남부터미널역 = new Station("남부터미널역");
        계양역 = new Station("계양역");
        신분당선 = new Line("신분당선", "red", 양재역, 강남역, 7);
        이호선 = new Line("이호선", "green", 교대역, 강남역, 3);
        삼호선 = new Line("삼호선", "orange", 교대역, 남부터미널역, 5);

        이호선.addSection(new Section(이호선, 강남역, 역삼역, 6));
        pathFinder = new PathFinder(Arrays.asList(신분당선, 이호선, 삼호선));
    }

    @Test
    @DisplayName("출발역과 도착역이 같은 경우 예외가 발생한다")
    void sameSourceTargetTest() {
        assertThatThrownBy(() -> pathFinder.findPaths(역삼역, 역삼역))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("출발역과 도착역이 연결되어 있지 않은 경우 예외가 발생한다")
    void notConnectedSourceTargetTest() {
        assertThatThrownBy(() -> pathFinder.findPaths(역삼역, 계양역))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("최단경로를 조회한다")
    void findPathTest() {
        // when
        List<StationResponse> paths = pathFinder.findPaths(남부터미널역, 역삼역);

        // then
        assertThat(paths).hasSize(4);
    }

    @Test
    @DisplayName("최단거리를 조회한다")
    void findPathsDistanceTest() {
        // when
        double pathsDistance = pathFinder.getPathsDistance(남부터미널역, 역삼역);

        // then
        assertThat(pathsDistance).isEqualTo(14);
    }
}