package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class PathFinderTest {


    private Station 신도림역 = Station.from("신도림역");
    private Station 가디역 = Station.from("가디역");
    private Station 대림역 = Station.from("대림역");

    private Line 일호선 = Line.of("1호선", "blue", 가디역, 신도림역, Distance.from(30), 0);
    private Line 이호선 = Line.of("2호선", "green", 신도림역, 대림역, Distance.from(10), 0);
    private Line 칠호선 = Line.of("7호선", "deep-green", 가디역, 대림역, Distance.from(10), 0);


    @Test
    @DisplayName("최단거리 전략을 이용하여 역들과 거리를 구한다")
    void strategyGetStationsAndWeight() {
        PathStrategy strategy = new DijkstraShortestPathStrategy(Arrays.asList(일호선, 이호선));

        PathFinder result = strategy.getShortPath(신도림역, 대림역);

        assertThat(result.getDistance()).isEqualTo(10);
        assertThat(result.getStations()).containsAll(Arrays.asList(신도림역, 대림역));
    }
}