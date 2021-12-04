package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class PathFinderTest {

    @DisplayName("경로를 검색한다")
    @Test
    void testFind() {
        // given
        Station 강남역 = new Station("강남역");
        Station 남부터미널 = new Station("남부터미널");
        Station 양재역 = new Station("양재역");
        Station 교대역 = new Station("교대역");

        Line 신분당선 = new Line("신분당선", "green", 강남역, 양재역, 10);
        Line 이호선 = new Line("이호선", "green", 교대역, 강남역, 10);
        Line 삼호선 = new Line("삼호선", "green", 교대역, 양재역, 5);

        삼호선.addSection(교대역, 남부터미널, 3);

        Set<Line> lines = new HashSet<>(Arrays.asList(신분당선, 이호선, 삼호선));

        // when
        GraphPath<Station, DefaultWeightedEdge> shortCut = PathFinder.findShortCut(lines, 교대역, 양재역);

        // then
        assertThat(shortCut.getWeight()).isEqualTo(5);
        assertThat(shortCut.getVertexList())
                .map(Station::getName)
                .containsExactly("교대역", "남부터미널", "양재역");
    }
}
