package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.Arrays;
import java.util.stream.Stream;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

@DisplayName("PathFinder 관련 기능")
class PathFinderTest {

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Station 연결되지_않는_역;

    private PathFinder pathFinder;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* --- 양재
     */
    @BeforeEach
    public void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        연결되지_않는_역 = new Station("연결되지_않는_역");

        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = new Line("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = new Line("삼호선", "bg-red-600", 교대역, 양재역, 5);
        삼호선.add(new Section(삼호선, 교대역, 남부터미널역, 3));

        pathFinder = new PathFinder(Arrays.asList(신분당선, 이호선, 삼호선));
    }

    @Test
    @DisplayName("최단 경로를 조회한다.")
    void findPath() {
        Path path = pathFinder.findPath(교대역, 양재역);

        assertThat(path.getStations()).containsExactly(교대역, 남부터미널역, 양재역);
        assertThat(path.getDistance()).isEqualTo(5);
    }

    @TestFactory
    @DisplayName("최단경로 조회의 예외케이스를 검증한다.")
    Stream<DynamicTest> findPath_fail() {
        return Stream.of(
            dynamicTest("출발역과 도착역을 같은 역으로 조회할 경우", () -> 최단_경로_조회시_실패_검증(교대역, 교대역)),
            dynamicTest("출발역과 도착역이 연결이 되어 있지 않은 경우", () -> 최단_경로_조회시_실패_검증(교대역, 연결되지_않는_역)),
            dynamicTest("존재하지 않은 출발역이나 도착역을 조회 할 경우", () -> 최단_경로_조회시_실패_검증(교대역, null))
        );
    }

    private void 최단_경로_조회시_실패_검증(Station sourceStation, Station targetStation) {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> pathFinder.findPath(sourceStation,targetStation));
    }

}
