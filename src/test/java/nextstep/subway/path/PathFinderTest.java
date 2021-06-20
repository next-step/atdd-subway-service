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
    private Section section1;
    private Station source;
    private Station mid1;
    private Station target;

    @BeforeEach
    void setUp() {
        source = Station.of("출발역");
        mid1 = Station.of("중간역");
        target = Station.of("도착역");

        line1 = new Line("1호선", "파란색", source, target, 100);
        section1 = new Section(line1, source, mid1, 50);
        line1.addSection(section1);
    }

    @Test
    void kShortest_를_이용한_최단거리역_적용_테스트() {

        // When
        PathFinder pathFinder = new PathFinder(Arrays.asList(line1));
        List<ShortestPath> shortestPaths = pathFinder.executeKShortest(source, target);

        System.out.println("getKShortestPaths");
        for (ShortestPath shortestPath : shortestPaths) {
            assertThat(shortestPath.stations()).startsWith(source);
            assertThat(shortestPath.stations()).endsWith(target);
        }
    }

    @Test
    void dijkstraShortest_를_이용한_최단거리역_적용_테스트() {

        // When
        PathFinder pathFinder = new PathFinder(Arrays.asList(line1));
        ShortestPath shortestPath = pathFinder.executeDijkstra(source, target);

        // Then
        assertThat(shortestPath.stations()).containsExactly(source, mid1, target);
    }

}
