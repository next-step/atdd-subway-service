package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
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
    private Station 구로디지털단지역;

    private PathFinder pathFinder;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        // given
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        구로디지털단지역 = new Station("구로디지털단지역");

        신분당선 = new Line("신분당선", "bg-red-300", 강남역, 양재역, 10);
        이호선 = new Line("이호선", "bg-yellow-420", 교대역, 강남역, 10);
        삼호선 = new Line("삼호선", "bg-green-500", 교대역, 양재역, 5);
        삼호선.addSection(new Section(삼호선, 교대역, 남부터미널역, 3));

        pathFinder = new PathFinder(Arrays.asList(신분당선, 이호선, 삼호선));
    }

    @DisplayName("최단 경로 조회한 경우")
    @Test
    void shortest_path() {
        // when
        Path shortestPath = pathFinder.getShortestPath(양재역, 교대역);

        // then
        assertAll(
            () -> assertThat(shortestPath.getDistance()).isEqualTo(5),
            () -> assertThat(shortestPath.getStations().size()).isEqualTo(3)
        );
    }

    @DisplayName("출발역과 도착역이 같은 경우 경로 조회 불가")
    @Test
    void same_start_arrive_section() {
        // when && then
        assertThatThrownBy(() -> pathFinder.getShortestPath(교대역, 교대역))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("출발역과 도착역이 같은 경우 최단 경로를 조회할 수 없습니다.");
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우 경로 조회 불가")
    @Test
    void not_connected_station_line() {
        // when && then
        assertThatThrownBy(() -> pathFinder.getShortestPath(강남역, 구로디지털단지역))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("출발역과 도착역은 서로 연결이 되어있어야 최단 경로를 조회할 수 있습니다.");
    }

}
