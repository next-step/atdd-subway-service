package nextstep.subway.station.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StationTest {

    @Test
    @DisplayName("역이 비어있음을 판단")
    void isEmpty() {
        assertAll(
                () -> assertThat(Station.isEmpty(null)).isTrue(),
                () -> assertThat(Station.isEmpty(new Station(""))).isTrue()
        );
    }

}