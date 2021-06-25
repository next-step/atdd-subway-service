package nextstep.subway.station.domain;

import nextstep.subway.line.domain.StationPair;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class StationsTest {

    @Test
    void getSectionPair() {
        Station first = new Station("FIRST");
        Station second = new Station("SECOND");
        Station third = new Station("THIRD");
        Station fourth = new Station("FOURTH");

        Stations stations = new Stations(Arrays.asList(first, second, third, fourth));

        assertThat(stations.getSectionPairs())
                .containsExactly(
                        new StationPair(first, second),
                        new StationPair(second, third),
                        new StationPair(third, fourth)
                );
    }
}