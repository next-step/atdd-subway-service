package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PathFinderTest {

    private PathFinder pathFinder;

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Line 팔호선;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Station 부산역;
    private Station 신림역;
    private Station 서울대역;
    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        신림역 = new Station("신림역");
        서울대역 = new Station("서울대역");
        부산역 = new Station("부산역");

        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = new Line("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = new Line("삼호선", "bg-red-600", 교대역, 양재역, 5);
        팔호선 = new Line("팔호선", "bg-red-600", 신림역, 서울대역, 2);
        삼호선.addLineStation(new Section(삼호선, 교대역, 남부터미널역, 3));

        pathFinder = new PathFinder();
    }

    @DisplayName("최단 경로를 조회한다.")
    @Test
    void find_shortest_path() {
        Path path = pathFinder.findShortestPath(Arrays.asList(신분당선, 이호선, 삼호선, 팔호선), 교대역, 양재역);

        assertThat(path.getStations()).containsExactly(교대역, 남부터미널역, 양재역);
        assertThat(path.getDistance()).isEqualTo(5);
    }

    @DisplayName("출발지와 목적지가 같은 최단 경로를 조회한다.")
    @Test
    void find_shortest_path_same_station() {
        assertThatThrownBy(() ->
            pathFinder.findShortestPath(Arrays.asList(신분당선, 이호선, 삼호선, 팔호선), 교대역, 교대역)
        ).isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("출발지와 목적지가 같을 수 없습니다.");
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 최단 경로를 조회한다.")
    @Test
    void find_shortest_path_no_connect_station() {
        assertThatThrownBy(() ->
            pathFinder.findShortestPath(Arrays.asList(신분당선, 이호선, 삼호선, 팔호선), 강남역, 부산역)
        ).isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("출발역과 도착역이 연결이 되어 있지 않습니다.");
    }
}
