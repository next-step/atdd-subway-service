package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static nextstep.subway.exception.ErrorMessage.NOT_CONNECT_START_ARRIVE_STATION;
import static nextstep.subway.exception.ErrorMessage.NOT_SEARCH_SAME_START_ARRIVE_STATION;
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
     * 교대역      --- *2호선(50)* ---   강남역
     * |                              |
     * *3호선(15)*                      *신분당선(50)*
     * |                              |
     * 남부터미널역  --- *3호선(10)*  ---   양재
     */
    @BeforeEach
    public void setUp() {
        // given
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        구로디지털단지역 = new Station("구로디지털단지역");

        신분당선 = new Line("신분당선", "bg-red-300", 강남역, 양재역, 50, 1000);
        이호선 = new Line("이호선", "bg-yellow-420", 교대역, 강남역, 50, 200);
        삼호선 = new Line("삼호선", "bg-green-500", 교대역, 양재역, 25, 300);
        삼호선.addSection(new Section(삼호선, 교대역, 남부터미널역, 15));

        pathFinder = new PathFinder(Arrays.asList(신분당선, 이호선, 삼호선));
    }

    @DisplayName("최단 경로 조회한 경우")
    @Test
    void shortest_path() {
        // when
        Path shortestPath = pathFinder.getShortestPath(교대역, 양재역);

        // then
        assertAll(
                () -> assertThat(shortestPath.getDistance()).isEqualTo(25),
                () -> assertThat(shortestPath.getStations().size()).isEqualTo(3),
                () -> assertThat(shortestPath.getExtraFare()).isEqualTo(1850)
        );
    }

    @DisplayName("출발역과 도착역이 같은 경우 경로 조회 불가")
    @Test
    void same_start_arrive_section() {
        // when && then
        assertThatThrownBy(() -> pathFinder.getShortestPath(교대역, 교대역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(NOT_SEARCH_SAME_START_ARRIVE_STATION.getMessage());
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우 경로 조회 불가")
    @Test
    void not_connected_station_line() {
        // when && then
        assertThatThrownBy(() -> pathFinder.getShortestPath(강남역, 구로디지털단지역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(NOT_CONNECT_START_ARRIVE_STATION.getMessage());
    }

    @DisplayName("2개의 노선을 탄 경우 노선 중 비싼 요금으로 요금이 부과된다.")
    @Test
    void line_fare_check() {
        // when
        Path shortestPath = pathFinder.getShortestPath(강남역, 남부터미널역);

        // then
        assertAll(
                () -> assertThat(shortestPath.getDistance()).isEqualTo(60),
                () -> assertThat(shortestPath.getStations().size()).isEqualTo(3),
                () -> assertThat(shortestPath.getExtraFare()).isEqualTo(3250)
        );

    }

    @DisplayName("노선의 추가요금과 거리에 다라 요금이 달라진다.")
    @Test
    void line_fare_test() {
        // given
        Line 테스트노선1 = new Line("테스트노선1", "bg-red-300", 강남역, 양재역, 8, 900);
        Line 테스트노선2 = new Line("테스트노선2", "bg-red-300", 강남역, 구로디지털단지역, 12, 900);

        pathFinder = new PathFinder(Arrays.asList(테스트노선1, 테스트노선2));
        // when
        Path shortestPath = pathFinder.getShortestPath(강남역, 양재역);
        Path shortestPath1 = pathFinder.getShortestPath(강남역, 구로디지털단지역);

        // then
        assertAll(
                () -> assertThat(shortestPath.getDistance()).isEqualTo(8),
                () -> assertThat(shortestPath.getStations().size()).isEqualTo(2),
                () -> assertThat(shortestPath.getExtraFare()).isEqualTo(2150),
                () -> assertThat(shortestPath1.getDistance()).isEqualTo(12),
                () -> assertThat(shortestPath1.getStations().size()).isEqualTo(2),
                () -> assertThat(shortestPath1.getExtraFare()).isEqualTo(2250)
        );

    }

}
