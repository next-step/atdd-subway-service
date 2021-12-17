package nextstep.subway.path.domain;

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
     * (3) *3호선*                   *신분당선*  (10)
     *     |                        |
     *     남부터미널역  --- *3호선* ---   양재
     *                      (2)
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

        신분당선 = new Line("신분당선", "red", 강남역, 양재역, 10);
        이호선 = new Line("신분당선", "green", 교대역, 강남역, 10);
        삼호선 = new Line("신분당선", "orange", 교대역, 양재역, 5);
        오호선 = new Line("신분당선", "purple", 화곡역, 우장산역, 5);

        삼호선.addSection(교대역, 남부터미널역, 3);

        List<Line> 전체노선 = Arrays.asList(신분당선, 이호선, 삼호선, 오호선);
        pathFinder = new PathFinder(전체노선);
    }

    @DisplayName("지하철 최단 경로 기능 확인")
    @Test
    void findBestPath() {
        // given

        // when

        // then
    }

    @DisplayName("출발역과 도착역이 같은 경우")
    @Test
    void invalidSameStartEndPoint() {
        // given

        // when

        // then
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우")
    @Test
    void invalidNotFoundPath() {
        // given

        // when

        // then
    }

    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우")
    @Test
    void invalidNotFoundStation() {
        // given

        // when

        // then
    }

}
