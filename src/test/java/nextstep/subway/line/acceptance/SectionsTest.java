package nextstep.subway.line.acceptance;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class SectionsTest {

    @Test
    void getStation() {
        Station upStation = new Station("소사역");
        Station downStation = new Station("역곡역");
        Line line = new Line("1호선", "red", upStation, downStation, 10);
        line.addStation(upStation, new Station("부천역"), 5);
        line.addStation(downStation, new Station("온수역"), 5);
        assertThat(line.getStations().stream().map(s -> s.getName()).collect(Collectors.toList())).containsExactly("소사역", "부천역", "역곡역", "온수역");
    }
}
