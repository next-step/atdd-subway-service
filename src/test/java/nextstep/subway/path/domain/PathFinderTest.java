package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.common.IllegalStationException;
import nextstep.subway.common.PathNotFoundException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PathFinderTest {
    private List<Line> lines;

    private Line 신분당선;

    private Station 강남역;
    private Station 양재역;
    private Station 판교역;

    PathFinder pathFinder;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        판교역 = new Station("판교역");

        신분당선 = new Line("신분당선", "bg-red", 강남역, 양재역, 5);

        lines = Arrays.asList(신분당선);

        pathFinder = PathFinder.of(lines);
    }

    @DisplayName("경로를 조회할 때 출발역과 도착역이 동일한 경우 예외가 발생한다.")
    @Test
    void testFindPathSameStation() {
        assertThatThrownBy(() -> pathFinder.findShortestPath(강남역, 강남역))
            .isInstanceOf(IllegalStationException.class);
    }

    @DisplayName("경로를 조회할 때 출발역과 도착역이 연결되어 있지 않은 경우 예외가 발생한다.")
    @Test
    void testFindPathNotLinkedStation() {
        assertThatThrownBy(() -> pathFinder.findShortestPath(강남역, 판교역))
            .isInstanceOf(PathNotFoundException.class);
    }
}