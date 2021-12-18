package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public class PathFinderTest {
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Line 오호선;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Station 화곡역;
    private Station 우장산역;
    private PathFinder pathFinder;

    /**                   (10)
     *     교대역    --- *2호선* ---   강남역
     *     |                        |
     * (4) *3호선*                   *신분당선*  (10)
     *     |                        |
     *     남부터미널역  --- *3호선* ---   양재
     *                      (6)
     *
     *
     *     화곡역 --- *5호선* --- 우장산역
     *                (10)
     */
    @BeforeEach
    void setUp() {
        강남역 = new Station(1L, "강남역");
        양재역 = new Station(2L, "양재역");
        교대역 = new Station(3L, "교대역");
        남부터미널역 = new Station(4L, "남부터미널역");
        화곡역 = new Station(5L, "화곡역");
        우장산역 = new Station(6L, "우장산역");

        신분당선 = new Line(1L, "신분당선", "red", 강남역, 양재역, 10);
        이호선 = new Line(2L, "이호선", "green", 교대역, 강남역, 10);
        삼호선 = new Line(3L, "삼호선", "orange", 교대역, 양재역, 10);
        오호선 = new Line(4L, "오호선", "purple", 화곡역, 우장산역, 10);

        삼호선.addSection(교대역, 남부터미널역, 4);

        List<Line> 전체노선 = Arrays.asList(신분당선, 이호선, 삼호선, 오호선);
        pathFinder = new PathFinder(전체노선);
    }

    @DisplayName("지하철 최단 경로 기능 확인")
    @Test
    void findBestPath() {
        // given
        Path path = pathFinder.findShortestPath(강남역, 남부터미널역);

        // when
        List<Station> stations = path.getStations();
        Station source = stations.get(0);
        Station target = stations.get(stations.size() - 1);
        int distance = path.getDistance();

        // then
        assertThat(source).isEqualTo(강남역);
        assertThat(target).isEqualTo(남부터미널역);
        assertThat(distance).isEqualTo(14);
    }

    @DisplayName("출발역과 도착역이 같은 경우")
    @Test
    void invalidSameStartEndPoint() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> {
                // when
                Path path = pathFinder.findShortestPath(강남역, 강남역);
            }).withMessageMatching(PathFinder.ERROR_DUPLICATE_STATION);
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우")
    @Test
    void invalidPathNotFound() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> {
                // when
                Path path = pathFinder.findShortestPath(강남역, 화곡역);
            }).withMessageMatching(PathFinder.ERROR_PATH_NOT_FOUND);
    }

    @DisplayName("존재하지 않은 출발역을 조회 할 경우")
    @Test
    void invalidStartStationNotFound() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> {
                // when
                Path path = pathFinder.findShortestPath(new Station(-1L, "없음"), 강남역);
            }).withMessageMatching(PathFinder.ERROR_START_STATION_NOT_FOUND);
    }

    @DisplayName("존재하지 않은 도착역을 조회 할 경우")
    @Test
    void invalidEndStationNotFound() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> {
                // when
                Path path = pathFinder.findShortestPath(강남역, new Station(-1L, "없음"));
            }).withMessageMatching(PathFinder.ERROR_END_STATION_NOT_FOUND);
    }

}
