package nextstep.subway.station.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StationTest {

    @Test
    void create() {
        Station station = new Station("서울역");
        assertThat(station).isNotNull();
    }
}
