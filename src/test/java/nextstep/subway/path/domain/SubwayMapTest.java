package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.exception.InvalidFindShortestPathException;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SubwayMapTest {
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Station 신도림역;
    private Station 영등포역;
    private SubwayMap subwayMap;

    /**
     * 신도림역 ---- *1호선* ---- 영등포역
     *
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* --- 양재역
     */
    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        신도림역 = new Station("신도림역");
        영등포역 = new Station("영등포역");

        Line 신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
        Line 일호선 = new Line("일호선", "bg-blue-600", 신도림역, 영등포역, 20);
        Line 이호선 = new Line("이호선", "bg-red-600", 교대역, 강남역, 10);
        Line 삼호선 = new Line("삼호선", "bg-red-600", 교대역, 양재역, 5);
        삼호선.addSection(new Section(삼호선, 교대역, 남부터미널역, new Distance(3)));

        subwayMap = new SubwayMap(Arrays.asList(신분당선, 일호선, 이호선, 삼호선));
    }

    @Test
    void findShortestPath() {
        Path path = subwayMap.findShortestPath(교대역, 양재역);
        assertThat(path.getStations()).containsExactlyElementsOf(Arrays.asList(교대역, 남부터미널역, 양재역));
        assertThat(path.getDistance()).isEqualTo(5);
    }

    @DisplayName("출발역과 도착역이 같은 경우 조회하지 못한다.")
    @Test
    void findShortestPathException1() {
        assertThatThrownBy(() -> subwayMap.findShortestPath(교대역, 교대역))
                .isInstanceOf(InvalidFindShortestPathException.class)
                .hasMessage("출발역과 도착역이 같으면 조회 불가능합니다.");
    }

    @DisplayName("출발역과 도착역이 연결되어 있지 않은 경우, 조회하지 못한다.")
    @Test
    void findShortestPathException2() {
        assertThatThrownBy(() -> subwayMap.findShortestPath(교대역, 신도림역))
                .isInstanceOf(InvalidFindShortestPathException.class)
                .hasMessage("출발역과 도착역이 연결이 되어 있지 않습니다.");
    }

    @DisplayName("존재하지 않은 출발역이나 도착역을 조회할 경우, 조회하지 못한다.")
    @Test
    void findShortestPathException3() {
        assertThatThrownBy(() -> subwayMap.findShortestPath(교대역, new Station("노량진역")))
                .isInstanceOf(InvalidFindShortestPathException.class)
                .hasMessage("출발역이나 도착역이 존재하지 않습니다.");
    }
}
