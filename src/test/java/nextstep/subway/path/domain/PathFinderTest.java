package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PathFinderTest {

    /**
     * 교대역  --- *2호선* 10 -----   강남역
     * |                           |
     * *3호선* 3                 *신분당선* 10
     * |                           |
     * 남부터미널역  --- *3호선* 2 --- 양재
     */
    @Test
    @DisplayName("지하철 최단 경로 조회")
    void getShortestList() {
        Station 교대역 = new Station("교대역");
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Station 남부터미널역 = new Station("남부터미널역");

        Line 신분당선 = Line.of("신분당선", "red", 강남역, 양재역, 10);
        Line 이호선 = Line.of("이호선", "green", 교대역, 강남역, 10);
        Line 삼호선 = Line.of("삼호선", "orange", 교대역, 남부터미널역, 3);
        삼호선.addLineStation(Section.create(남부터미널역, 양재역, Distance.valueOf(2)));

        PathFinder graph = PathFinder.of(Arrays.asList(신분당선, 이호선, 삼호선));

        List<Station> shortestPaths = graph.findShortestPath(교대역, 양재역);

        assertThat(shortestPaths).extracting(Station::getName).containsExactly("교대역", "남부터미널역", "양재역" );
    }
}
