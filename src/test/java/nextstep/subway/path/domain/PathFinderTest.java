package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PathFinderTest {
    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 정자역;
    private Station 남부터미널역;
    private Line 이호선;
    private Line 신분당선;
    private Line 삼호선;
    private PathFinder pathFinder;

    /**
     * Feature: 지하철 경로 검색
     * Scenario: 두 역의 최단 거리 경로를 조회
     *   Given 지하철역이 등록되어있음
     *   And 지하철 노선이 등록되어있음
     *   And 지하철 노선에 지하철역이 등록되어있음
     *
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재역
     *                          |
     *                          정자역
     */
    @BeforeEach
    void setUp() {
        교대역 = new Station("교대역");
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        정자역 = new Station("정자역");
        남부터미널역 = new Station("남부터미널역");

        신분당선 = new Line("신분당선", "bg-red-600");
        신분당선.addSection(강남역, 양재역, 10);
        신분당선.addSection(양재역, 정자역, 50);

        이호선 = new Line("2호선", "bg-green-600");
        이호선.addSection(교대역, 강남역, 10);

        삼호선 = new Line("3호선", "bg-orange-600");
        삼호선.addSection(교대역, 남부터미널역, 5);
        삼호선.addSection(남부터미널역, 양재역, 3);

        pathFinder = new PathFinder(new DijkstraShortestPathFinder());
    }

    @Test
    @DisplayName("최단 경로 조회")
    void findShortestPath() {
        // when
        Path path = pathFinder.findShortestPath(Arrays.asList(이호선, 삼호선, 신분당선), 교대역, 양재역);

        // then
        assertThat(path.getStations()).containsExactly(교대역, 남부터미널역, 양재역);
        assertThat(path.getDistance()).isEqualTo(8);
    }

    @Test
    @DisplayName("출발역과 도착역이 같은 경우")
    void findPathSameSourceAndTarget() {
        // when then
        assertThatThrownBy(()  -> pathFinder.findShortestPath(Arrays.asList(이호선, 삼호선, 신분당선), 교대역, 교대역))
            .isInstanceOf(PathException.class);
    }

    @Test
    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우")
    public void notConnectSourceAndTarget() {
        // given
        Station 서울역 = new Station("서울역");
        Station 삼각지역 = new Station("삼각지역");
        Line 사호선 = new Line("신분당선", "bg-blue-600");
        사호선.addSection(서울역, 삼각지역, 5);

        // when then
        assertThatThrownBy(()  -> pathFinder.findShortestPath(Arrays.asList(이호선, 삼호선, 신분당선, 사호선), 교대역, 삼각지역))
            .isInstanceOf(PathException.class);
    }

    @Test
    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우")
    public void findPathNoExistStation() {
        // given
        Station 존재하지않는역 = new Station("존재하지않는역");

        // when then
        assertThatThrownBy(()  -> pathFinder.findShortestPath(Arrays.asList(이호선, 삼호선, 신분당선), 교대역, 존재하지않는역))
            .isInstanceOf(PathException.class);
    }

    @Test
    @DisplayName("거리에 따른 운임비용 - 10Km")
    public void getFareByDistanceBasedWithin10Km() {
        // when
        Path baseFare = pathFinder.findShortestPath(Arrays.asList(이호선, 삼호선, 신분당선), 강남역, 양재역);

        // then
        assertThat(baseFare.getFare()).isEqualTo(1_250);
    }

    @Test
    @DisplayName("거리에 따른 운임비용 - 10km 초과 50km 이내")
    public void getFareByDistanceBasedExcess10Km() {
        // when
        Path excess10KmFare = pathFinder.findShortestPath(Arrays.asList(이호선, 삼호선, 신분당선), 강남역, 남부터미널역);

        // then
        assertThat(excess10KmFare.getFare()).isEqualTo(1_350);
    }

    @Test
    @DisplayName("거리에 따른 운임비용 - 50km 초과")
    public void getFareByDistanceBasedExcess50Km() {
        // when
        Path excess50KmFare = pathFinder.findShortestPath(Arrays.asList(이호선, 삼호선, 신분당선), 강남역, 정자역);

        // then
        assertThat(excess50KmFare.getFare()).isEqualTo(2_250);
    }
}
