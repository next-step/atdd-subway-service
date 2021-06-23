package nextstep.subway.path;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.ShortestPath;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PathFinderTest {

    private Line line1;
    private Line line2;
    private Line line3;
    private Line line4;
    private Station 일번역;
    private Station 이번역;
    private Station 삼번역;
    private Station 사번역;
    private int basicFare = ShortestPath.BASIC_FARE;
    private int additionalFareOfLine1 = 800;
    private int additionalFareOfLine2 = 1300;
    private int additionalFareOfLine3 = 300;
    private int additionalFareOfLine4 = 1000;
    private int distanceFromStation1to2 = 1;
    private int distanceFromStation2to3 = 50;
    private int distanceFromStation3to4 = 5;
    private int distanceFromStation4to1 = 7;

    /**
     * 1번역      ---      *1호선(+800)*    --- 2번역
     * |                 distance: 1           |
     * *4호선(+1000)*                       *2호선(+1300)*
     * | distance: 7                       distance: 3
     * |                                        |
     * 4번역      ---      *3호선(+300)*    ---  3번역
     *                    distance: 5
     */

    @BeforeEach
    void setUp() {
        일번역 = Station.of("일번역");
        이번역 = Station.of("이번역");
        삼번역 = Station.of("삼번역");
        사번역 = Station.of("사번역");

        line1 = new Line("1호선", "파란색", additionalFareOfLine1, 일번역, 이번역, distanceFromStation1to2);
        line2 = new Line("2호선", "빨간색", additionalFareOfLine2, 이번역, 삼번역, distanceFromStation2to3);
        line3 = new Line("3호선", "노란색", additionalFareOfLine3, 삼번역, 사번역, distanceFromStation3to4);
        line4 = new Line("4호선", "보라색", additionalFareOfLine4, 사번역, 일번역, distanceFromStation4to1);
    }

    @Test
    void kShortest_를_이용한_최단거리역_적용_테스트() {

        // When
        PathFinder pathFinder = new PathFinder(Arrays.asList(line1, line2, line3, line4));
        List<ShortestPath> shortestPaths = pathFinder.executeKShortest(일번역, 삼번역);

        // Then
        for (ShortestPath shortestPath : shortestPaths) {
            assertThat(shortestPath.stations()).startsWith(일번역);
            assertThat(shortestPath.stations()).endsWith(삼번역);
        }

    }

    @Test
    void dijkstraShortest_를_이용한_최단거리역_적용_테스트() {

        // When
        PathFinder pathFinder = new PathFinder(Arrays.asList(line1, line2, line3, line4));
        ShortestPath shortestPath = pathFinder.executeDijkstra(일번역, 삼번역);

        // Then
        assertThat(shortestPath.stations()).containsExactly(일번역, 사번역, 삼번역);

        // And
        assertThat(shortestPath.totalDistance()).isEqualTo(distanceFromStation4to1 + distanceFromStation3to4);
        final int overFare = 100;
        assertThat(shortestPath.fare()).isEqualTo(basicFare + overFare + additionalFareOfLine4);
    }

}
