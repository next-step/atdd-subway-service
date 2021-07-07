package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class PathFinderTest {

    private Station 교대역;
    private Station 남부터미널역;
    private Station 강남역;
    private Station 양재역;
    private Station 신도림역;
    private Station 서울역;

    private List<Section> sections = new ArrayList<>();

    @BeforeEach
    void setUp() {
        교대역 = new Station(1L, "교대역");
        남부터미널역 = new Station(2L, "남부터미널역");
        강남역 = new Station(3L, "강남역");
        양재역 = new Station(4L, "양재역");
        신도림역 = new Station(5L, "신도림역");
        서울역 = new Station(6L, "서울역");

        Line 일호선 = new Line("1호선", "blue");
        sections.add(new Section(일호선, 신도림역, 서울역, 10));

        Line 이호선 = new Line("2호선", "green");
        sections.add(new Section(이호선, 교대역, 강남역, 10));

        Line 삼호선 = new Line("3호선", "orange");
        sections.add(new Section(삼호선, 교대역, 남부터미널역, 3));
        sections.add(new Section(삼호선, 남부터미널역, 양재역, 2));

        Line 신분당선 = new Line("신분당선", "red");
        sections.add(new Section(신분당선, 강남역, 양재역, 10));
    }

    @DisplayName("최단 거리 경로 찾아 그래프 객체 반환")
    @Test
    void findShortestPath() {
        // given
        PathFinder pathFinder = PathFinder.of(sections);

        // when
        ShortestPath shortestPath = pathFinder.findShortestPath(강남역, 남부터미널역);

        // then
        assertThat(shortestPath.getDistance()).isEqualTo(12);
        assertThat(shortestPath.getStations()).containsExactlyElementsOf(Arrays.asList(강남역, 양재역, 남부터미널역));
        assertThat(shortestPath.calculateFareWithPolicy()).isEqualTo(1350);
    }

    @DisplayName("출발역과 도착역이 같은 경우")
    @Test
    void findShortestPath_exception() {
        // given
        PathFinder pathFinder = PathFinder.of(sections);

        // when & then
        assertThatThrownBy(() -> pathFinder.findShortestPath(강남역, 강남역))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("출발역과 도착역이 같습니다.");
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우")
    @Test
    void findShortestPath_exception2() {
        // given
        PathFinder pathFinder = PathFinder.of(sections);

        // when & then
        assertThatThrownBy(() -> pathFinder.findShortestPath(서울역, 강남역))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("출발역과 도착역이 연결이 되어 있지 않습니다.");
    }
}
