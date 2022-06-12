package nextstep.subway.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PathFinderTest {
    private Station 강남역, 양재역, 교대역, 남부터미널역;
    private Line 신분당선, 이호선, 삼호선;
    private PathFinder pathFinder;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");

        신분당선 = new Line("신분당선", "red", 강남역, 양재역, Distance.of(10));
        이호선 = new Line("이호선", "green", 교대역, 강남역, Distance.of(10));
        삼호선 = new Line("삼호선", "yellow", 교대역, 양재역, Distance.of(5));

        삼호선.addSection(new Section(삼호선, 교대역, 남부터미널역, Distance.of(3)));

        pathFinder = new PathFinder();
        pathFinder.init(Arrays.asList(신분당선, 이호선, 삼호선));
    }

    @Test
    @DisplayName("최단 거리 경로, 거리 확인")
    void verifyShortestPathAndDistance() {
        List<Station> shortestPathStation = pathFinder.getShortestPath(강남역, 남부터미널역);
        int shortestDistance = pathFinder.getShortestDistance(강남역, 남부터미널역);

        assertAll(
                () -> assertThat(shortestDistance).isEqualTo(12),
                () -> assertThat(shortestPathStation).containsExactly(강남역, 양재역, 남부터미널역)
        );
    }
}
