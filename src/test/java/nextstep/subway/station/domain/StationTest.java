package nextstep.subway.station.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("역")
class StationTest {
    @Test
    @DisplayName("지하철역을 생성할 수 있다.")
    void 생성() {
        assertThat(Station.from("강남역")).isNotNull();
    }
}
