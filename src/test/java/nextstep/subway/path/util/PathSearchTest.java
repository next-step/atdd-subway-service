package nextstep.subway.path.util;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.PathDestination;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class PathSearchTest {
    private PathSearch pathSearch;

    private Line line = new Line("2호선", "green");
    private Line line2 = new Line("3호선", "orange");
    private Line line3 = new Line("분당선", "red");

    private Station station1 = new Station("강남역");
    private Station station2 = new Station("교대역");
    private Station station3 = new Station("남부터미널역");
    private Station station4 = new Station("양재역");

    @BeforeEach
    void setUp() {
        pathSearch = new PathSearch();

        line.addSection(station1, station2, 20);

        line2.addSection(station2, station3, 5);
        line2.addSection(station3, station4, 5);

        line3.addSection(station1, station4, 100);
    }

    @Test
    void findPath() {
        Lines lines = new Lines(Arrays.asList(line, line2, line3));

        PathResponse paths = pathSearch.findPaths(lines, new PathDestination(station1, station4));

        assertThat(paths.getStations()).containsExactly(
                StationResponse.of(station1), StationResponse.of(station2),
                StationResponse.of(station3), StationResponse.of(station4)
        );
    }
}
