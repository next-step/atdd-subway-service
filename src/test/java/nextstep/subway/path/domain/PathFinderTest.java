package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("PathFinder 클래스 테스트")
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
     * <pre>
     * 교대역  ---   *2호선*   ---   강남역
     *   |                             |
     * *3호선*                    *신분당선*
     *   |                             |
     * 남부터미널역  --- *3호선* ---  양재
     * </pre>
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

    @DisplayName("최단 경로를 조회한다.")
    @Test
    void findShortestPath() {
        Path path = pathFinder.findShortestPath(교대역, 양재역);

        assertThat(path.getStations()).containsExactly(교대역, 남부터미널역, 양재역);
        assertThat(path.getDistance()).isEqualTo(8);
        assertThat(path.getLines()).containsExactly(삼호선);
    }

    @DisplayName("출발지와 목적지가 같은 최단 경로를 조회한다.")
    @Test
    void findShortestPathWithSameStation() {
        assertThatThrownBy(() -> {
            pathFinder.findShortestPath(교대역, 교대역);
        }).isInstanceOf(InvalidPathFindException.class)
        .hasMessageContaining("출발지와 목적지가 같을 수 없습니다.");
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 최단 경로를 조회한다.")
    @Test
    void findShortestPathWithNoConnectStation() {
        assertThatThrownBy(() -> {
            pathFinder.findShortestPath(강남역, 연결되지않는역);
        }).isInstanceOf(InvalidPathFindException.class)
        .hasMessageContaining("목적지가 출발지와 연결되어 있지 않습니다.");
    }
}
