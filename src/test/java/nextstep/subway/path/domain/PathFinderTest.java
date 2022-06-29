package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class PathFinderTest {

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Station 연결되지않는역;

    private PathFinder pathFinder;

    /**
     * 교대역  ---   *2호선*   ---   강남역
     *   |                             |
     * *3호선*                    *신분당선*
     *   |                             |
     * 남부터미널역  --- *3호선* ---  양재역
     */
    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        연결되지않는역 = new Station("연결되지않는역");

        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = new Line("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = new Line("삼호선", "bg-red-600", 남부터미널역, 양재역, 5);
        삼호선.addSection(교대역, 남부터미널역, 3);

        pathFinder = new PathFinder(Arrays.asList(신분당선, 이호선, 삼호선));
    }

    @DisplayName("최단 경로 조회")
    @Test
    void findShortestPath() {
        // when
        Path shortestPath = pathFinder.findShortestPath(교대역, 양재역);

        // then
        assertAll(
                () -> assertThat(shortestPath.getStations()).containsExactly(교대역, 남부터미널역, 양재역),
                () -> assertThat(shortestPath.getDistance()).isEqualTo(8),
                () -> assertThat(shortestPath.getFare()).isEqualTo(new Fare(1250))
        );
    }

    @Test
    void 출발역과_도착역이_같은_경우() {
        // when & then
        assertThatThrownBy(() -> pathFinder.findShortestPath(교대역, 교대역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("출발역과 도착역은 같을 수 없습니다.");
    }

    @Test
    void 출발역과_도착역이_연결이_되어_있지_않은_경우() {
        // when & then
        assertThatThrownBy(() -> pathFinder.findShortestPath(연결되지않는역, 교대역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("출발역과 도착역이 연결되어 있지 않습니다.");
    }

}