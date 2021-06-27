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

    private List<Section> sections = new ArrayList<>();

    @BeforeEach
    void setUp() {
        /**
         * 교대역    --- *2호선* ---   강남역
         * |                        |
         * *3호선*                   *신분당선*
         * |                        |
         * 남부터미널역  --- *3호선* ---   양재
         */
        교대역 = new Station(1L, "교대역");
        남부터미널역 = new Station(1L, "남부터미널역");
        강남역 = new Station(1L, "강남역");
        양재역 = new Station(1L, "양재역");

        Line 이호선 = new Line("2호선", "green");
        sections.add(new Section(이호선, 교대역, 강남역, 10));

        Line 삼호선 = new Line("3호선", "orange");
        sections.add(new Section(삼호선, 교대역, 남부터미널역, 3));
        sections.add(new Section(삼호선, 남부터미널역, 양재역, 2));

        Line 신분당선 = new Line("신분당선", "red");
        sections.add(new Section(신분당선, 강남역, 양재역, 10));
    }

    @DisplayName("최단 거리 경로를 찾아 유효한 그래프 객체를 반환한다.")
    @Test
    void findShortestPath() {
        PathFinder pathFinder = PathFinder.of(sections);
        GraphPath<Station, DefaultWeightedEdge> shortestPath = pathFinder.findShortestPath(강남역, 남부터미널역);

        assertThat(shortestPath).isNotNull();
        assertThat(shortestPath.getWeight()).isEqualTo(12);
        assertThat(shortestPath.getVertexList()).containsExactlyElementsOf(Arrays.asList(강남역, 양재역, 남부터미널역));
    }
}
