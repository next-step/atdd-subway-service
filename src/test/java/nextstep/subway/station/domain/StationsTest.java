package nextstep.subway.station.domain;

import nextstep.subway.line.domain.SimpleSection;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class StationsTest {

    @Test
    void getSimpleSection() {
        Station first = new Station("FIRST");
        Station second = new Station("SECOND");
        Station third = new Station("THIRD");
        Station fourth = new Station("FOURTH");

        Stations stations = new Stations(Arrays.asList(first, second, third, fourth));

        assertThat(stations.getSimpleSection())
                .containsExactly(
                        new SimpleSection(first, second),
                        new SimpleSection(second, third),
                        new SimpleSection(third, fourth)
                );
    }
}