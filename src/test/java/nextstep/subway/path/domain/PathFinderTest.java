package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.exception.NotConnectedStationsException;
import nextstep.subway.path.exception.NotFoundStationsInLineException;
import nextstep.subway.path.exception.SameStartEndStationException;
import nextstep.subway.station.domain.Station;
import org.assertj.core.api.ThrowableAssert;
import org.jgrapht.GraphPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PathFinderTest {
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Station 강남역;
    private Station 삼성역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private PathFinder pathFinder;

    @BeforeEach
    public void setUp() {
        강남역 = new Station("강남역");
        삼성역 = new Station("삼성역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");

        신분당선 = new Line("신분당선", "yellow", 강남역, 양재역, 5);
        이호선 = new Line("이호선", "green", 교대역, 강남역, 5);
        삼호선 = new Line("삼호선", "red", 교대역, 남부터미널역, 3);

        이호선.addSection(new Section(이호선, 강남역, 삼성역, 10));
        삼호선.addSection(new Section(삼호선, 남부터미널역, 양재역, 2));

        pathFinder = PathFinder.of(Arrays.asList(이호선, 삼호선, 신분당선));
    }

    /**
     * 교대역    --- *2호선* 거리5 ---     강남역 ---  거리10  --- 삼성역
     * |                                  |
     * *3호선* 거리3                    *신분당선* 거리10
     * |                                  |
     * 남부터미널역  --- *3호선* 거리2 ---   양재역
     */

    @DisplayName("최단경로 찾기")
    @Test
    void findShortestPath() {
        GraphPath<Station, SectionWeightedEdge> shortestPath = pathFinder.findShortestPath(교대역, 양재역);

        assertThat(shortestPath.getVertexList()).containsExactly(교대역, 남부터미널역, 양재역);
        assertThat(shortestPath.getWeight()).isEqualTo(5);
    }

    @DisplayName("출발역과 도착역이 같은 경우")
    @Test
    void sameStations() {
        ThrowableAssert.ThrowingCallable throwingCallable = () -> pathFinder.findShortestPath(교대역, 교대역);

        assertThatThrownBy(throwingCallable)
                .isInstanceOf(SameStartEndStationException.class);
    }

    @DisplayName("출발역과 도착역이 연결되지 않은 경우")
    @Test
    void notConnectedStations() {
        Station 오이도역 = new Station("오이도역");
        Station 과천역 = new Station("과천역");
        Line 사호선 = new Line("사호선", "blue", 과천역, 오이도역, 100);

        pathFinder = PathFinder.of(Arrays.asList(이호선, 삼호선, 신분당선, 사호선));

        ThrowableAssert.ThrowingCallable throwingCallable = () -> pathFinder.findShortestPath(강남역, 과천역);

        assertThatThrownBy(throwingCallable)
                .isInstanceOf(NotConnectedStationsException.class);
    }

    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우")
    @Test
    void notExistStartAndEndStation() {
        Station 과천역 = new Station("과천역");

        ThrowableAssert.ThrowingCallable throwingCallable = () -> pathFinder.findShortestPath(강남역, 과천역);

        assertThatThrownBy(throwingCallable)
                .isInstanceOf(NotFoundStationsInLineException.class);
    }


}
