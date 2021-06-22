package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SubwayNavigationTest {

    /**
     * 교대역   --- *2호선*(10)  ---     강남역
     * |                                 |
     * *3호선*(7)                     *신분당선* (4)
     * |                                |
     * 남부터미널역 --- *3호선*(10) --- 양재
     * <p>
     * 춘천역 -----*춘천강원선*(20)------- 강원역
     */

    private Station 교대역;
    private Station 남부터미널역;
    private Station 양재역;
    private Station 강남역;
    private Station 춘천역;
    private Station 강원역;
    private Line 이호선;
    private Line 삼호선;
    private Line 신분당선;
    private Line 춘천강원선;
    private List<Line> 노선도;

    @BeforeEach
    void setUp() {
        교대역 = initStation("교대역", 1L);
        남부터미널역 = initStation("남부터미널역", 2L);
        양재역 = initStation("양재역", 3L);
        강남역 = initStation("강남역", 4L);
        춘천역 = initStation("춘천역", 5L);
        강원역 = initStation("강원역", 6L);

        이호선 = new Line("2호선", "green", 교대역, 강남역, 10);
        삼호선 = new Line("3호선", "orange", 교대역, 양재역, 17);
        신분당선 = new Line("신분당선", "red", 강남역, 양재역, 4);
        춘천강원선 = new Line("춘천강원선", "sky", 춘천역, 강원역, 20);

        삼호선.addSection(new Section(삼호선, 교대역, 남부터미널역, 7));
        노선도 = Arrays.asList(이호선, 삼호선, 신분당선, 춘천강원선);
    }

    @DisplayName("경로 내의 지하철역들을 조회한다.")
    @Test
    void getPaths() {
        SubwayMapData subwayMapData = new SubwayMapData(노선도, SectionEdge.class);

        SubwayNavigation subwayNavigation = new SubwayNavigation(subwayMapData.initData());

        List<Station> stations = subwayNavigation.getPaths(강남역, 남부터미널역);
        assertThat(stations).containsExactly(강남역, 양재역, 남부터미널역);
    }

    @DisplayName("경로 내의 지하철역 조회시 예외발생 - case 1 : 출발역과 도착역이 같은 경우")
    @Test
    void getPaths_exception_1() {
        SubwayMapData subwayMapData = new SubwayMapData(노선도, SectionEdge.class);

        SubwayNavigation subwayNavigation = new SubwayNavigation(subwayMapData.initData());

        assertThatThrownBy(() -> subwayNavigation.getPaths(강남역, 강남역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("같은 역으로 경로를 조회할 수 없습니다.");
    }

    @DisplayName("경로 내의 지하철역 조회시 예외발생 - case 2 : 출발역과 도착역이 연결되어 있지 않은 경우")
    @Test
    void getPaths_exception_2() {
        SubwayMapData subwayMapData = new SubwayMapData(노선도, SectionEdge.class);

        SubwayNavigation subwayNavigation = new SubwayNavigation(subwayMapData.initData());

        assertThatThrownBy(() -> subwayNavigation.getPaths(강남역, 강원역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("노선이 연결되어 있지 않습니다.");
    }

    @DisplayName("경로 내의 지하철역 조회시 예외발생 - case 3 : 경로상에 존재하지 않는 역을 조회할 경우")
    @Test
    void getPaths_exception_3() {
        SubwayMapData subwayMapData = new SubwayMapData(노선도, SectionEdge.class);
        Station 처음보는역 = new Station("처음보는역");
        Station 못보던역 = new Station("못보던역");

        SubwayNavigation subwayNavigation = new SubwayNavigation(subwayMapData.initData());

        assertThatThrownBy(() -> subwayNavigation.getPaths(처음보는역, 못보던역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("graph must contain the source vertex");
    }

    @DisplayName("경로 내의 지하철역 조회시 예외발생 - case 4 : 없는(null) 역을 조회할 경우")
    @Test
    void getPaths_exception_4() {
        SubwayMapData subwayMapData = new SubwayMapData(노선도, SectionEdge.class);
        Station 없는역1= null;
        Station 없는역2 = null;

        SubwayNavigation subwayNavigation = new SubwayNavigation(subwayMapData.initData());

        assertThatThrownBy(() -> subwayNavigation.getPaths(없는역1, 없는역2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("기존에 등록되지 않은 역입니다.");
    }

    @Test
    void getDistance() {

    }

    private Station initStation(String name, Long id) {
        Station station = new Station(name);
        ReflectionTestUtils.setField(station, "id", id);
        return station;
    }
}