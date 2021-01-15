package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.*;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PathTest {

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Path path;

    /**
     *              거리 10
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * 거리 3                     거리 10
     * |                        |
     * 남부터미널역  --- *3호선* --- 양재역
     *              거리 2
     */

    @BeforeAll
    public void setUp() {
        교대역 = new Station("교대역");
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        남부터미널역 = new Station("남부터미널역");

        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10, 1000);
        이호선 = new Line("이호선", "bg-red-600", 교대역, 강남역, 10, 0);
        삼호선 = new Line("삼호선", "bg-red-600", 교대역, 양재역, 5, 500);
        Section 남부터미널역_양재역 = new Section(삼호선, 남부터미널역, 양재역, new Distance(2));
        삼호선.add(남부터미널역_양재역);
        path = Path.of(Arrays.asList(신분당선, 이호선, 삼호선));
    }

    @DisplayName("정상 경로 조회")
    @Test
    void findShortestPath() {
        //when
        ShortestPath shortestPath = path.findShortestPath(교대역, 양재역);

        //then
        assertThat(shortestPath.getStations())
                .map(Station::getName)
                .containsExactly("교대역", "남부터미널역", "양재역");
    }

    @DisplayName("출발역과 도착역이 같은 경우")
    @Test
    void findShortestPathSameStation() {
        //when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            path.findShortestPath(교대역, 교대역);
        }).withMessageMatching("조회하려는 출발지와 도착지가 같습니다.");
    }

    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우")
    @Test
    void findShortestPathNotExistStation() {
        //when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            path.findShortestPath(교대역, new Station("명동역"));
        }).withMessageMatching("경로에 포함되어 있지 않은 역입니다.");
    }

}
