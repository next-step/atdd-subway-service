package nextstep.subway.path.domain;

import com.sun.tools.javac.util.List;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class PathFinderTest {

    private Line 이호선;
    private Line 삼호선;
    private Line 신분당선;
    private Station 강남역 = new Station(1L, "강남역");
    private Station 교대역 = new Station(2L, "강남역");
    private Station 남부터미널역 = new Station(3L, "강남역");
    private Station 양재역 = new Station(4L, "강남역");

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setup() {
        이호선 = new Line("이호선", "파랑색", 교대역, 강남역, 10, 0);
        삼호선 = new Line("삼호선", "초록색", 교대역, 양재역, 20, 0);
        신분당선 = new Line("신분당선", "빨간색", 양재역, 강남역, 10, 0);

        삼호선.addLineSection(남부터미널역, 양재역, new Distance(10));
    }

    @DisplayName("최단경로 조회")
    @Test
    public void findShortestPath() {
        PathFinder pathFinder = new DijkstraPathFinder(List.of(이호선, 삼호선, 신분당선));

        ShortestPath shortestPath = pathFinder.findShortestPath(교대역, 양재역);

        assertAll(
                () -> assertThat(shortestPath.getDistance().getValue()).isEqualTo(20),
                () -> assertThat(shortestPath.getStations()).containsExactly(교대역, 강남역, 양재역)
        );
    }

    @DisplayName("출발역 또는 도착역을 null로 전달할 경우 에러발생")
    @ParameterizedTest
    @NullSource
    public void findShortestPath_출발역_또는_도착역_null_에러발생(Station station) {
        PathFinder pathFinder = new DijkstraPathFinder(List.of(이호선, 삼호선, 신분당선));

        assertThatThrownBy(() -> pathFinder.findShortestPath(교대역, station))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> pathFinder.findShortestPath(station, 교대역))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("출발역과 도착역이 같은 경우 에러발생")
    @Test
    public void findShortestPath_출발역과_도착역이_같은_경우_에러_발생() {
        PathFinder pathFinder = new DijkstraPathFinder(List.of(이호선, 삼호선, 신분당선));

        assertThatThrownBy(() -> pathFinder.findShortestPath(교대역, 교대역))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("출발역과 도착역이 연결되지 않은 경우 에러발생")
    @Test
    public void findShortestPath_출발역과_도착역이_연결되지_않은_경우_에러_발생() {
        Station 오이도역 = new Station(5L, "오이도역");
        Station 당고개역 = new Station(5L, "당고개역");
        Line 사호선 = new Line("사호선", "하늘색", 당고개역, 오이도역, 30, 0);
        PathFinder pathFinder = new DijkstraPathFinder(List.of(이호선, 삼호선, 신분당선, 사호선));

        assertThatThrownBy(() -> pathFinder.findShortestPath(교대역, 당고개역))
                .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("존재하지 않는 출발역이나 도착열을 조회할 경우 에러발생")
    @Test
    public void findShortestPath_존재하지_않는_출발역과_도착역을_조회할_경우_에러_발생() {
        Station 오이도역 = new Station(5L, "오이도역");
        PathFinder pathFinder = new DijkstraPathFinder(List.of(이호선, 삼호선, 신분당선));

        assertThatThrownBy(() -> pathFinder.findShortestPath(교대역, 오이도역))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
