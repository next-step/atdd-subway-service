package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.common.IllegalStationException;
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

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");

        신분당선 = new Line("신분당선", "bg-red", 강남역, 양재역, 5);

        lines = Arrays.asList(신분당선);
    }

    @DisplayName("경로를 조회할 때 출발역과 도착역이 동일한 경우 예외가 발생한다.")
    @Test
    void testFindPathSameStation() {
        PathFinder pathFinder = PathFinder.of(lines);
        assertThatThrownBy(() -> pathFinder.findShortestPath(강남역, 강남역))
            .isInstanceOf(IllegalStationException.class);
    }
}