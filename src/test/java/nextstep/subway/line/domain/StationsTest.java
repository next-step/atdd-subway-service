package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

class StationsTest {

    @Test
    void containsAll() {
        Stations stations = Stations.from(Arrays.asList(
            Station.of(1L, "station1"),
            Station.of(2L, "station2")
        ));

        boolean actual = stations.containsAll(Arrays.asList(
            Station.of(1L, "station1"),
            Station.of(2L, "station2")
        ));

        assertThat(actual).isTrue();
    }

    @Test
    void containsAll_failed() {
        Stations stations = Stations.from(Arrays.asList(
            Station.of(1L, "station1"),
            Station.of(2L, "station2")
        ));

        boolean actual = stations.containsAll(Arrays.asList(
            Station.of(1L, "station1"),
            Station.of(2L, "station2"),
            Station.of(3L, "station3")
        ));

        assertThat(actual).isFalse();
    }
}
