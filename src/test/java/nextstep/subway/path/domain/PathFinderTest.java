package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PathFinderTest {

    private Station upStation;
    private Station downStation;
    private Line line;

    @BeforeEach
    void setUp() {
        upStation = new Station("건대역");
        downStation = new Station("용마산역");
        line = new Line("7호선", "bg-red-600", upStation, downStation, 10);
    }

    @DisplayName("최단거리 경로 검증")
    @Test
    void create() {
        List<Line> lines = Arrays.asList(line);

        PathFinder pathFinder = PathFinder.of(lines);
        Path path = PathFinder.of(lines).findPathBetweenStations(upStation, downStation);

        assertThat(path.getStations().size()).isEqualTo(2);
        assertThat(path.getDistance()).isEqualTo(10);

    }
}
