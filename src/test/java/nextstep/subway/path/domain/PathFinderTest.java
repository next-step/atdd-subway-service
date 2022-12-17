package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PathFinderTest {

    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Line 신분당선;
    private Line _2호선;
    private Line _3호선;
    private List<Line> 노선목록;

    private Section 구간;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");

        신분당선 = new Line("신분당선", "red", 강남역, 양재역, 10);
        _2호선 = new Line("2호선", "green", 교대역, 강남역, 10);
        _3호선 = new Line("3호선", "orange", 교대역, 양재역, 5);
        노선목록 = Arrays.asList(신분당선, _2호선, _3호선);

        구간 = new Section(_3호선, 교대역, 남부터미널역, 3);
        _3호선.getSections().add(구간);
    }

    @DisplayName("환승하지 않는 지하철 경로 조회")
    @Test
    void 경로_조회() {
        //3호선 교대역 - 남부터미널역 - 양재역
        List<Station> expected = Arrays.asList(교대역, 남부터미널역, 양재역);
        assertThat(PathFinder.findPath(노선목록, 교대역, 양재역).getStations()).containsExactlyElementsOf(expected);
    }

    @DisplayName("환승하는 지하철 경로 조회")
    @Test
    void 환승_필요_경로_조회() {
        //남부터미널역(3호선) - 양재역(환승) - 강남역(2호선)
        List<Station> expected = Arrays.asList(남부터미널역, 양재역, 강남역);
        assertThat(PathFinder.findPath(노선목록, 남부터미널역, 강남역).getStations()).containsExactlyElementsOf(expected);
    }

    @DisplayName("출발역과 도착역이 같은 경로 조회 시 예외처리")
    @Test
    void 출발역과_도착역이_같은_경로_조회() {
        assertThatThrownBy(
                () -> PathFinder.findPath(노선목록, 강남역, 강남역).getStations()
        ).isInstanceOf(RuntimeException.class)
                .hasMessageMatching("출발역과 도착역이 같은 경로는 조회할 수 없습니다.");
    }

    @DisplayName("연결되지 않은 두 역의 경로 조회 시 예외처리")
    @Test
    void 연결되지_않은_두_역_사이_경로_조회() {
        Station 고속터미널역 = new Station("고속터미널역");
        Station 봉은사역 = new Station("봉은사역");
        Line _9호선 = new Line("9호선", "brown", 고속터미널역, 봉은사역, 60);
        노선목록.add(_9호선);

        assertThatThrownBy(
                () -> PathFinder.findPath(노선목록, 남부터미널역, 봉은사역)
        ).isInstanceOf(RuntimeException.class)
                .hasMessageMatching("출발역과 도착역이 연결되어있지 않습니다.");
    }
}