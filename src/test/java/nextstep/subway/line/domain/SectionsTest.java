package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SectionsTest {
    private Line line;
    private Station firstStation;
    private Station secondStation;
    private Station thirdStation;
    private Station forthStation;

    @BeforeEach
    void setUp() {
        firstStation = new Station("1번역");
        secondStation = new Station("2번역");
        line = new Line("1번노선", "색깔", firstStation, secondStation, 10);
        thirdStation = new Station("3번역");
        forthStation = new Station("4번역");
    }

    @Test
    void add() {
        line.addLineStation(secondStation, thirdStation, 4);
        assertThat(line.getStations()).isEqualTo(Arrays.asList(firstStation, secondStation, thirdStation));
    }

    @Test
    void remove() {
        line.addLineStation(secondStation, thirdStation, 4);
        assertThat(line.getStations()).isEqualTo(Arrays.asList(firstStation, secondStation, thirdStation));
        line.removeLineStation(secondStation);
        assertThat(line.getStations()).isEqualTo(Arrays.asList(firstStation, thirdStation));
    }

    @Test
    void add_existentStation() {
        assertThatThrownBy(() -> line.addLineStation(firstStation, secondStation, 4))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void add_non_existentStation() {
        assertThatThrownBy(() -> line.addLineStation(thirdStation, forthStation, 4))
                .isInstanceOf(RuntimeException.class);
    }
}