package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LineTest {
    private Line line;
    private Station firstStation;
    private Station secondStation;
    private Station thirdStation;

    @BeforeEach
    void setUp() {
        firstStation = new Station("1번역");
        secondStation = new Station("2번역");
        line = new Line("1번노선", "색깔", firstStation, secondStation, 10, 0);
        thirdStation = new Station("3번역");
    }

    @Test
    void getStations() {
        assertThat(line.getStations().size()).isEqualTo(2);
    }

    @Test
    void addLineStation() {
        line.addLineStation(secondStation, thirdStation, 4);
        assertThat(line.getStations().size()).isEqualTo(3);
    }

    @Test
    void removeLineStation() {
        line.addLineStation(secondStation, thirdStation, 4);
        line.removeLineStation(firstStation);
        assertThat(line.getStations().size()).isEqualTo(2);
    }
}