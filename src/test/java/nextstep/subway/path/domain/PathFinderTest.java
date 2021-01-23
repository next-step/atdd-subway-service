package nextstep.subway.path.domain;

import nextstep.subway.common.exception.CustomException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("경로 찾기에 관련한 기능")
class PathFinderTest {
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널;
    private Line 이호선;
    private Line 신분당선;
    private Line 삼호선;
    private PathFinder pathFinder;

    @BeforeEach
    void beforeEach() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널 = new Station("남부터미널");
        이호선 = new Line("2호선", "bg-green-200", 교대역, 강남역, 1000L);
        삼호선 = new Line("3호선", "bg-yellow-200", 교대역, 양재역, 500L);
        삼호선.addSection(교대역, 남부터미널, 300L);
        신분당선 = new Line("신분당선", "bg-red-200", 강남역, 양재역, 1000L);

        pathFinder = new PathFinder(Arrays.asList(이호선, 삼호선, 신분당선));
    }

    @DisplayName("PathFinder 객체 생성")
    @Test
    void createPathFinder() {
        // When
        PathFinder pathFinder = new PathFinder(Arrays.asList(이호선, 삼호선, 신분당선));
        // Then
        assertThat(pathFinder).isNotNull();
    }

    @DisplayName("가장 짧은 거리인 경로를 찾는다")
    @Test
    void findShortestPath() {
        // When
        pathFinder.findShortestPath(교대역, 양재역);
        ShortestPath shortestPath = pathFinder.getShortestPath();
        // Then
        assertAll(
                () -> assertThat(shortestPath.getStations()).isNotNull(),
                () -> assertThat(shortestPath.getStations())
                        .hasSize(3)
                        .extracting(Station::getName)
                        .containsExactly("교대역", "남부터미널", "양재역"),
                () -> assertThat(shortestPath.getDistance()).isEqualTo(500L)
        );
    }

    @DisplayName("예외 상황 - 출발역과 도착역이 같은 경우")
    @Test
    void exceptionToFindShortestPathOfSameSourceAndTarget() {
        // When & Then
        assertThatThrownBy(() -> pathFinder.findShortestPath(교대역, 교대역))
                .isInstanceOf(CustomException.class)
                .hasMessage("출발역과 도착역이 동일합니다.");
    }

    @DisplayName("예외 상황 - 출발역과 도착역이 연결이 되어 있지 않은 경우")
    @Test
    void exceptionToFindShortestPathOfUnconnectedSourceAndTarget() {
        // Given
        Station 광교역 = new Station("광교역");
        Station 정자역 = new Station("정자역");
        Line 신분당선 = new Line("신분당선", "bg-red-200", 광교역, 정자역, 1000L);
        pathFinder = new PathFinder(Arrays.asList(이호선, 삼호선, 신분당선));
        // When & Then
        assertThatThrownBy(() -> pathFinder.findShortestPath(교대역, 광교역))
                .isInstanceOf(CustomException.class)
                .hasMessage("출발역과 도착역이 연결이 되어 있지 않습니다.");
    }
}
