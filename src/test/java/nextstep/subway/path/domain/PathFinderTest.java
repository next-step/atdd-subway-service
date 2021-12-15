package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철 경로 도메인 관련 기능")
public class PathFinderTest {

    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;
    private Station 교대역;
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Line 사호선;

    @BeforeEach
    public void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");

        남부터미널역 = new Station("남부터미널역");
        교대역 = new Station("교대역");

        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = new Line("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = new Line("삼호선", "bg-red-600", 양재역, 남부터미널역, 5);
        사호선 = new Line("사호선", "bg-red-600", 교대역, 남부터미널역, 4);
    }

    /**
     * 교대역    --- *2호선* ---   강남역
     *                            |
     *                           *신분당선*
     *                            |
     *                         양재
     */
    @DisplayName("경로 노드 내 역이 존재하는 지 확인하다")
    @Test
    void checkContainsStation() {
        // given
        PathFinder pathFinder = new PathFinder(Arrays.asList(
            신분당선, 이호선
        ));

        // when, then
        assertThat(pathFinder.containsStation(교대역)).isTrue();
        assertThat(pathFinder.containsStation(남부터미널역)).isFalse();
    }

    /**
     * 교대역    --- *2호선* ---   강남역
     *
     * 남부터미널역  --- *3호선* ---   양재
     */
    @DisplayName("Station A와 B가 경로로 연결되어 있는지 확인한다")
    @Test
    void checkStationConnectedTest() {
        // given
        PathFinder pathFinder = new PathFinder(Arrays.asList(
            이호선, 삼호선
        ));

        // when, then
        assertThat(pathFinder.stationsConnected(남부터미널역, 양재역)).isTrue();
        assertThat(pathFinder.stationsConnected(남부터미널역, 강남역)).isFalse();
    }

    /**
     * 교대역  --- *2호선(10)* --- 강남역
     * |                           |
     * *4호선(4)*                  *신분당선(10)*
     * |                           |
     * 남부터미널역 --- *3호선(5)* --- 양재
     */
    @DisplayName("역과 역 사이 가장 가까운 경로를 찾는다")
    @Test
    void getShortestPathTest() {
        // given
        PathFinder pathFinder = new PathFinder(Arrays.asList(
            신분당선, 이호선, 삼호선, 사호선
        ));

        // when
        Path path = pathFinder.getShortestPath(남부터미널역, 강남역);

        // then
        assertThat(path.getPath()).containsExactly(남부터미널역, 교대역, 강남역);
    }
}
